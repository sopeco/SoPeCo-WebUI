package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

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

	private static IMetaDataPersistenceProvider metaProvider;

	private List<DatabaseInstanceToDefinition> databaseList;

	/*
	 * get a list of all databases
	 */
	public List<DatabaseDefinition> getAllDatabases() {

		logger.debug("loading databases");

		/*
		 * 
		 * Configuration.getSingleton().getProperty(arg0);
		 * Configuration.getSingleton
		 * ().setProperty("sopeco.config.persistence.server.host",
		 * "deqkal276.qkal.sap.corp") PersistenceConfiguration c; c.get
		 * 
		 * 
		 * IMetaDataPersistenceProvider provider =
		 * PersistenceProviderFactory.getMetaDataPersistenceProvider(); // //
		 * provider.loadAllDatabaseInstances().get(0).; // // DatabaseInstance
		 * // provider.st // // // IPersistenceProvider provider2 =
		 * PersistenceProviderFactory.getPersistenceProvider(); provider2.
		 */

		if (metaProvider == null)
			metaProvider = PersistenceProviderFactory.getMetaDataPersistenceProvider();

		List<DatabaseInstance> list = null;
		try {
			list = metaProvider.loadAllDatabaseInstances();

		} catch (DataNotFoundException e1) {

			e1.printStackTrace();
		}

		// IMetaDataPersistenceProvider provider =
		// PersistenceProviderFactory.getMetaDataPersistenceProvider();
		/*
		 * List<DatabaseDefinition> returnList = new
		 * ArrayList<DatabaseDefinition>();
		 * 
		 * try { // returnList = provider.loadAllDatabaseInstances();
		 * DatabaseInstance b = new DatabaseInstance("asdasd", "asdasd");
		 * DatabaseDefinition dd = new DatabaseDefinition();
		 * dd.setName("test1"); returnList.add(dd);
		 * 
		 * } catch (Exception e) { throw new RuntimeException(e); }
		 */
		/*
		 * DatabaseDefinition d = new DatabaseDefinition(); d.setName("test1");
		 * DatabaseDefinition d2 = new DatabaseDefinition();
		 * d2.setName("test2");
		 * 
		 * returnList.add(d); returnList.add(d2);
		 */
		//createDummyDatabases();

		createDatabaseList(list);

		return getDatabaseDefinitionList();
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
	 * addding a database
	 */
	public boolean addDatabase() {
		logger.debug("database added");

		DatabaseInstance newDatabase = new DatabaseInstance("new database", "");

		metaProvider.store(newDatabase);
		dummyList.add(newDatabase);

		return true;
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
		DatabaseInstanceToDefinition instanceToDefinition = null;

		for (DatabaseInstanceToDefinition dbITD : databaseList) {
			if (dbITD.getId() == databaseDefinition.getId())
				instanceToDefinition = dbITD;
		}

		if (instanceToDefinition == null)
			return false;

		instanceToDefinition.setDefinition(databaseDefinition);

		metaProvider.store(instanceToDefinition.getInstance());

		return true;
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
			definition.setConnectionURL(instance.getConnectionUrl());
			definition.setId(id);
			return definition;
		}

		public void setDefinition(DatabaseDefinition definition) {
			instance.setConnectionUrl(definition.getConnectionURL());
			instance.setDbName(definition.getName());
		}
	}
}
