package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.server.db.FlexiblePersistenceProviderFactory;
import org.sopeco.persistence.IMetaDataPersistenceProvider;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.dataset.DataSetRowBuilder;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatabaseManagerRPCImpl extends RemoteServiceServlet implements DatabaseManagerRPC {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManagerRPCImpl.class);
	private static final long serialVersionUID = 1L;

	private static IMetaDataPersistenceProvider metaDataPersistenceProvider;

	// private List<DatabaseInstanceToDefinition> databaseList;

	/*
	 * 
	 */
	private static IMetaDataPersistenceProvider getMetaProvider() {
		if (metaDataPersistenceProvider == null) {
			metaDataPersistenceProvider = PersistenceProviderFactory.getMetaDataPersistenceProvider();
		}

		return metaDataPersistenceProvider;
	}

	/*
	 * get a list of all databases
	 */
	public List<DatabaseInstance> getAllDatabases() {
		logger.debug("loading databases");

		try {
			List<DatabaseInstance> instances = getMetaProvider().loadAllDatabaseInstances();

			// List<DatabaseDefinition> returnList = new
			// ArrayList<DatabaseDefinition>();
			//
			// for (DatabaseInstance instance : instances) {
			// returnList.add(databaseInstanceToDefinition(instance));
			// }

			return instances;
		} catch (DataNotFoundException e) {
			return new ArrayList<DatabaseInstance>();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * removing the specific database
	 */
	public boolean removeDatabase(DatabaseInstance dbDefinition) {
		logger.debug("deleting database " + dbDefinition.getDbName());

		try {
			DatabaseInstance dbInstance = getRealInstance(dbDefinition);

			if (dbInstance != null) {
				getMetaProvider().remove(dbInstance);
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
		logger.debug("adding new database");

		String dbName = dbInstance.getDbName();
		
		dbName = dbName.replaceAll("[^a-zA-Z0-9]", "_");
		dbInstance.setDbName(dbName);
		
		if (dbInstance.isProtectedByPassword()) {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbInstance.getHost(), dbInstance.getPort(),
					dbInstance.getDbName(), passwd);
		} else {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbInstance.getHost(), dbInstance.getPort(),
					dbInstance.getDbName());
		}

		getMetaProvider().store(dbInstance);

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
			List<DatabaseInstance> instances = getMetaProvider().loadAllDatabaseInstances();

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
		if (!i1.getDbName().equals(i2.getDbName()))
			return false;
		if (!i1.getHost().equals(i2.getHost()))
			return false;
		if (!i1.getId().equals(i2.getId()))
			return false;
		if (!i1.getPort().equals(i2.getPort()))
			return false;
		if (!i1.getUser().equals(i2.getUser()))
			return false;
		if (i1.isProtectedByPassword() != i2.isProtectedByPassword())
			return false;

		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean selectDatabase(DatabaseInstance databaseInstance, String passwd) {
		DatabaseInstance dbInstance = getRealInstance(databaseInstance);

		if (dbInstance == null || databaseInstance == null)
			return false;

		logger.debug("selected database: " + dbInstance.getDbName());
		logger.debug("    host: " + dbInstance.getHost());
		logger.debug("    is pw protected: " + dbInstance.isProtectedByPassword());

		if (dbInstance.isProtectedByPassword()) {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbInstance.getHost(), dbInstance.getPort(),
					dbInstance.getDbName(), passwd);
		} else {
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbInstance.getHost(), dbInstance.getPort(),
					dbInstance.getDbName());
		}

		IPersistenceProvider provider = PersistenceProviderFactory.getPersistenceProvider();

		try {
			DataSetRowBuilder rb = new DataSetRowBuilder();


				provider.store(rb.createDataSet("myId"));
				
				logger.debug("test load: " + provider.loadDataSet("myId").getID());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return true;
	}
}
