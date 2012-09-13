package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.FlexInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.server.db.FlexiblePersistenceProviderFactory;
import org.sopeco.frontend.shared.definitions.DatabaseDefinition;
import org.sopeco.persistence.IMetaDataPersistenceProvider;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.config.PersistenceConfiguration;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatabaseManagerRPCImpl extends RemoteServiceServlet implements
		DatabaseManagerRPC {

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
	public List<DatabaseDefinition> getAllDatabases() {
		logger.debug("loading databases");

		try {
			List<DatabaseInstance> instances = getMetaProvider().loadAllDatabaseInstances();

			List<DatabaseDefinition> returnList = new ArrayList<DatabaseDefinition>();

			for (DatabaseInstance instance : instances) {
				returnList.add(databaseInstanceToDefinition(instance));
			}
			
			return returnList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * removing the specific database
	 */
	public boolean removeDatabase(DatabaseDefinition dbDefinition) {
		logger.debug("deleting database " + dbDefinition.getName());

		try {
			List<DatabaseInstance> instances = getMetaProvider().loadAllDatabaseInstances();

			for (DatabaseInstance dbInstance : instances) {
				if (instanceEqualsDefinition(dbDefinition, dbInstance)) {
					getMetaProvider().remove(dbInstance);
					return true;
				}
			}

			throw new Exception("can't find database " + dbDefinition.getName()
					+ " '" + dbDefinition.getHost() + "'");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * @param 
	 * 
	 */
	public boolean addDatabase(DatabaseDefinition dbDefinition) {
		logger.debug("adding new database");
		DatabaseInstance instance = new DatabaseInstance();

		if ( dbDefinition.getPassword().isEmpty() )
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbDefinition.getHost(), dbDefinition.getPort(), dbDefinition.getName());
		else
			FlexiblePersistenceProviderFactory.createPersistenceProvider(dbDefinition.getHost(), dbDefinition.getPort(), dbDefinition.getName(), dbDefinition.getPassword());
		
		String connectionUrl = PersistenceConfiguration.getSingleton().getServerUrl();
		
		instance.setDbName(dbDefinition.getName());
		instance.setConnectionUrl(connectionUrl);

		getMetaProvider().store(instance);

		//
		return true;
	}

	/*
	 * create DatabaseDefinition object from DatabaseInstance object
	 */
	private DatabaseDefinition databaseInstanceToDefinition(DatabaseInstance instance) {
		DatabaseDefinition definition = new DatabaseDefinition();

		definition.setName(instance.getDbName());
		definition.setHost(instance.getConnectionUrl());

		return definition;
	}

	/*
	 * check if the committed databases are equal
	 */
	private boolean instanceEqualsDefinition(DatabaseDefinition definition, DatabaseInstance instance) {
		if ( !definition.getName().equals(instance.getDbName()) )
			return false;
		
		if ( !definition.getHost().equals(instance.getConnectionUrl()) )
			return false;
		
		return true;
	}
}
