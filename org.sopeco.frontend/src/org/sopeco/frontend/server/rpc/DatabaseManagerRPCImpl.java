package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.shared.definitions.DatabaseDefinition;
import org.sopeco.persistence.IMetaDataPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatabaseManagerRPCImpl extends RemoteServiceServlet implements
		DatabaseManagerRPC {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseManagerRPCImpl.class);
	private static final long serialVersionUID = 1L;

	private static IMetaDataPersistenceProvider metaDataPersistenceProvider;

	private List<DatabaseInstanceToDefinition> databaseList;

	/*
	 * 
	 */
	private static IMetaDataPersistenceProvider getMetaProvider () {
		if ( metaDataPersistenceProvider == null ) {
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
			
			for ( DatabaseInstance instance : instances) {
				returnList.add( databaseInstanceToDefinition(instance) );
			}
			
			return returnList;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	List<DatabaseInstance> dummyList;

	private void createDummyDatabases() {
		if (dummyList == null) {
			logger.debug("create dummy databases");

			dummyList = new ArrayList<DatabaseInstance>();

			dummyList.add(new DatabaseInstance("name1", "url1"));
			dummyList.add(new DatabaseInstance("name2", "url2"));
			dummyList.add(new DatabaseInstance("name3", "url3"));
		}
	}

	/*
	 * creates the list databaseList with databaseInstanceToDefinition items
	 */
	private void createDatabaseList(List<DatabaseInstance> databaseInstanceList) {
		logger.debug("Creates DatabaseInstanceToDefinition objects from DatabaseInstance");

		databaseList = new ArrayList<DatabaseInstanceToDefinition>();

		for (DatabaseInstance instance : databaseInstanceList) {
			DatabaseInstanceToDefinition dbITD = new DatabaseInstanceToDefinition(
					instance);
			databaseList.add(dbITD);
		}

	}

	/*
	 * creates a list of databasedefinitons
	 */
	private List<DatabaseDefinition> getDatabaseDefinitionList() {
		logger.debug("Creates a list of DatabaseDefinitions");

		List<DatabaseDefinition> returnList = new ArrayList<DatabaseDefinition>();

		for (DatabaseInstanceToDefinition dbITD : databaseList) {
			returnList.add(dbITD.createDefinition());
		}

		return returnList;
	}

	/*
	 * removing the specific database
	 */
	public boolean removeDatabase(DatabaseDefinition databaseDefinition) {

		return false;
	}

	/*
	 * update the definition
	 */
	@Override
	public boolean updateDatabase(DatabaseDefinition databaseDefinition) {
//		DatabaseInstanceToDefinition instanceToDefinition = null;
//
//		for (DatabaseInstanceToDefinition dbITD : databaseList) {
//			if (dbITD.getId() == databaseDefinition.getId())
//				instanceToDefinition = dbITD;
//		}
//
//		if (instanceToDefinition == null)
//			return false;
//
//		instanceToDefinition.setDefinition(databaseDefinition);
//
//		//metaProvider.store(instanceToDefinition.getInstance());

		return true;
	}

	/*
	 * add a new database to the database..
	 */
	@Override
	public boolean addDatabase(DatabaseDefinition dbDefinition) {
		DatabaseInstance instance = new DatabaseInstance();

		instance.setDbName(dbDefinition.getName());
		instance.setConnectionUrl(dbDefinition.getHost());
		
		getMetaProvider().store(instance);
		
		return true;
	}
	
	/*
	 * create DatabaseDefinition object from DatabaseInstance object
	 */
	private DatabaseDefinition databaseInstanceToDefinition ( DatabaseInstance instance ) {
		DatabaseDefinition definition = new DatabaseDefinition();
		
		definition.setName(instance.getDbName());
		definition.setHost(instance.getConnectionUrl());
		
		return definition;
	}
	
	/*
	 * class to associate databaseDefinition and databaseInstances
	 */
	private static class DatabaseInstanceToDefinition {
		private DatabaseInstance instance;
		private int id;
		private static int idSequence = 1;

		public DatabaseInstanceToDefinition(DatabaseInstance instance) {
			id = ++idSequence;
			this.instance = instance;
		}

		public DatabaseInstance getInstance() {
			return instance;
		}

		public int getId() {
			return id;
		}

		public DatabaseDefinition createDefinition() {
			DatabaseDefinition definition = new DatabaseDefinition();
			definition.setName(instance.getDbName());
			definition.setHost(instance.getConnectionUrl());
			definition.setId(id);
			return definition;
		}

		public void setDefinition(DatabaseDefinition definition) {
			instance.setConnectionUrl(definition.getHost());
			instance.setDbName(definition.getName());
		}
	}
}
