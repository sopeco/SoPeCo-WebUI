package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.server.db.FlexiblePersistenceProviderFactory;
import org.sopeco.frontend.server.db.PersistenceProvider;
import org.sopeco.frontend.server.db.UIPersistenceProviderFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.exceptions.WrongCredentialsException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * RPC Class for the access on the databases.
 * 
 * @author Marius Oehler
 * 
 */
public class DatabaseManagerRPCImpl extends RemoteServiceServlet implements DatabaseManagerRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManagerRPCImpl.class);
	private static final long serialVersionUID = 1L;

	// /**
	// * Stores the persistence provider in a session attribute.
	// *
	// * @param session
	// * current session
	// * @return IMetaDataPersistenceProvider
	// */
	// private static IMetaDataPersistenceProvider getMetaProvider(HttpSession
	// session) {
	// if (session.getAttribute("metaPersistenceProvider") == null) {
	// session.setAttribute("metaPersistenceProvider",
	// PersistenceProviderFactory.getInstance()
	// .getMetaDataPersistenceProvider(session.getId()));
	// }
	//
	// return (IMetaDataPersistenceProvider)
	// session.getAttribute("metaPersistenceProvider");
	// }

	/**
	 * Returns the databases which are stored in the meta database.
	 * 
	 * @return List of database instances
	 */
	public List<DatabaseInstance> getAllDatabases() {
		LOGGER.debug("loading databases");

		try {
			return PersistenceProvider.getMetaProvider(getThreadLocalRequest().getSession()).loadAllDatabaseInstances();
		} catch (DataNotFoundException e) {
			return new ArrayList<DatabaseInstance>();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Removing the specific database.
	 * 
	 * @param dbDefinition
	 *            database which will be removed
	 * @return true if the removal was successful
	 */
	public boolean removeDatabase(DatabaseInstance dbDefinition) {
		LOGGER.debug("deleting database " + dbDefinition.getDbName());

		try {
			DatabaseInstance dbInstance = getRealInstance(dbDefinition);

			if (dbInstance != null) {
				PersistenceProvider.getMetaProvider(getThreadLocalRequest().getSession()).remove(dbInstance);
				return true;
			}

			throw new Exception("can't find database " + dbDefinition.getDbName() + " '" + dbDefinition.getHost() + "'");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param
	 * 
	 */
	public boolean addDatabase(DatabaseInstance dbInstance, String passwd) {
		LOGGER.debug("adding new database");

		String dbName = dbInstance.getDbName();

		dbName = dbName.replaceAll("[^a-zA-Z0-9]", "_");
		dbInstance.setDbName(dbName);

		if (dbInstance.isProtectedByPassword()) {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
					dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName(), passwd);
		} else {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
					dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName());
		}

		PersistenceProvider.getMetaProvider(getThreadLocalRequest().getSession()).store(dbInstance);

		return true;
	}

	/**
	 * Get the databaseInstacen object of the database which is equals to the
	 * committed databaseInstance
	 * 
	 * @param DatabaseInstance
	 *            search
	 * @return DatabaseInstance result
	 */
	private DatabaseInstance getRealInstance(DatabaseInstance instance) {
		try {
			List<DatabaseInstance> instances = PersistenceProvider
					.getMetaProvider(getThreadLocalRequest().getSession()).loadAllDatabaseInstances();

			for (DatabaseInstance dbInstance : instances) {
				if (instanceEqual(instance, dbInstance)) {
					return dbInstance;
				}
			}

			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Are the instances equal?
	 * 
	 * @param instance
	 *            one
	 * @param instance
	 *            two
	 * @return true if they are equal
	 */
	public boolean instanceEqual(DatabaseInstance i1, DatabaseInstance i2) {
		if (!i1.getDbName().equals(i2.getDbName())) {
			return false;
		}
		if (!i1.getHost().equals(i2.getHost())) {
			return false;
		}
		if (!i1.getId().equals(i2.getId())) {
			return false;
		}
		if (!i1.getPort().equals(i2.getPort())) {
			return false;
		}
		if (!i1.getUser().equals(i2.getUser())) {
			return false;
		}
		if (i1.isProtectedByPassword() != i2.isProtectedByPassword()) {
			return false;
		}

		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean selectDatabase(DatabaseInstance databaseInstance, String passwd) {
		DatabaseInstance dbInstance = getRealInstance(databaseInstance);

		if (dbInstance == null || databaseInstance == null) {
			return false;
		}

		LOGGER.debug("selected database: " + dbInstance.getDbName());
		LOGGER.debug("    host: " + dbInstance.getHost());
		LOGGER.debug("    is pw protected: " + dbInstance.isProtectedByPassword());

		IPersistenceProvider dbConnection = null;

		try {
			if (dbInstance.isProtectedByPassword()) {
				dbConnection = FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest()
						.getSession(), dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName(), passwd);
			} else {
				dbConnection = FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest()
						.getSession(), dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName());
			}
		} catch (WrongCredentialsException e) {
			LOGGER.warn(e.getMessage());
			return false;
		}

		if (dbConnection == null) {
			LOGGER.warn("Can't connect to database..");
			return false;
		}

		HttpSession session = getThreadLocalRequest().getSession();
		
		UIPersistenceProviderFactory.createUIPersistenceProvider(session, dbInstance.getHost(), dbInstance.getPort(),
				dbInstance.getDbName(), passwd);
		
		PersistenceProvider.setPersistenceProvider(dbConnection, getThreadLocalRequest().getSession());

		PersistenceProvider.getUIPersistenceProvider(session).store();
		PersistenceProvider.getUIPersistenceProvider(session).load();
		
		return true;
	}

	/**
	 * Checks if the database accepts the given password.
	 * 
	 * @param instance
	 *            database which should be checked
	 * 
	 * @param passwd
	 *            password for the database
	 * 
	 * @return true if password is correct
	 */
	@Override
	public boolean checkPassword(DatabaseInstance instance, String passwd) {
		if (!instance.isProtectedByPassword()) {
			return true;
		}

		try {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
					instance.getHost(), instance.getPort(), instance.getDbName(), passwd);
		} catch (WrongCredentialsException e) {
			LOGGER.warn(e.getMessage());

			return false;
		}

		return true;
	}
}
