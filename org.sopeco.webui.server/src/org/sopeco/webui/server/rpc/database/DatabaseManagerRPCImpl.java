///**
// * Copyright (c) 2013 SAP
// * All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *     * Redistributions of source code must retain the above copyright
// *       notice, this list of conditions and the following disclaimer.
// *     * Redistributions in binary form must reproduce the above copyright
// *       notice, this list of conditions and the following disclaimer in the
// *       documentation and/or other materials provided with the distribution.
// *     * Neither the name of the SAP nor the
// *       names of its contributors may be used to endorse or promote products
// *       derived from this software without specific prior written permission.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
// * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//package org.sopeco.webui.server.rpc.database;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.sopeco.persistence.IPersistenceProvider;
//import org.sopeco.persistence.exceptions.DataNotFoundException;
//import org.sopeco.persistence.exceptions.WrongCredentialsException;
//import org.sopeco.persistence.metadata.entities.DatabaseInstance;
//import org.sopeco.webui.client.rpc.DatabaseManagerRPC;
//import org.sopeco.webui.server.persistence.FlexiblePersistenceProviderFactory;
//import org.sopeco.webui.server.persistence.UiPersistence;
//import org.sopeco.webui.server.rpc.SuperRemoteServlet;
//import org.sopeco.webui.shared.entities.account.AccountDetails;
//import org.sopeco.webui.shared.helper.Metering;
//
///**
// * RPC Class for the access on the databases.
// * 
// * @author Marius Oehler
// * 
// */
//public class DatabaseManagerRPCImpl extends SuperRemoteServlet implements DatabaseManagerRPC {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManagerRPCImpl.class);
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * Returns the databases which are stored in the meta database.
//	 * 
//	 * @return List of database instances
//	 */
//	public DatabaseInstance getDatabase() {
////		return getUser().getCurrentAccount();
//		return null;
//	}
//
//	/**
//	 * Removing the specific database.
//	 * 
//	 * @param dbDefinition
//	 *            database which will be removed
//	 * @return true if the removal was successful
//	 */
//	@SuppressWarnings("unused")
//	public boolean removeDatabase(DatabaseInstance dbDefinition) {
//		LOGGER.debug("deleting database " + dbDefinition.getDbName());
//
//		try {
//			DatabaseInstance dbInstance = getRealInstance(dbDefinition);
//
//			AccountDetails details = UiPersistence.getUiProvider().loadAccountDetails(0);
//			if (details != null) {
//				UiPersistence.getUiProvider().removeAccountDetails(details);
//			}
//			if (dbInstance != null) {
//				UiPersistence.getMetaProvider().remove(dbInstance);
//				return true;
//			}
//
//			throw new Exception("can't find database " + dbDefinition.getDbName() + " '" + dbDefinition.getHost() + "'");
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * @param
//	 * 
//	 */
//	public boolean addDatabase(DatabaseInstance dbInstance, String passwd) {
//		double metering = Metering.start();
//		LOGGER.debug("adding new database");
//
//		String dbName = dbInstance.getDbName();
//
//		dbName = dbName.replaceAll("[^a-zA-Z0-9]", "_");
//		dbInstance.setDbName(dbName);
//
//		if (accountExists(dbName)) {
//			return false;
//		}
//
//		if (dbInstance.isProtectedByPassword()) {
//			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
//					dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName(), passwd);
//		} else {
//			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
//					dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName());
//		}
//
//		UiPersistence.getMetaProvider().store(dbInstance);
//
//		Metering.stop(metering);
//		return true;
//	}
//
//	/**
//	 * Get the databaseInstacen object of the database which is equals to the
//	 * committed databaseInstance
//	 * 
//	 * @param DatabaseInstance
//	 *            search
//	 * @return DatabaseInstance result
//	 */
//	private DatabaseInstance getRealInstance(DatabaseInstance instance) {
//		try {
//			List<DatabaseInstance> instances = UiPersistence.getMetaProvider().loadAllDatabaseInstances();
//
//			for (DatabaseInstance dbInstance : instances) {
//				if (instanceEqual(instance, dbInstance)) {
//					return dbInstance;
//				}
//			}
//
//			return null;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	private DatabaseInstance getDatabaseInstance(String dbName) {
//		try {
//			List<DatabaseInstance> instanceList = UiPersistence.getMetaProvider().loadAllDatabaseInstances();
//
//			for (DatabaseInstance instance : instanceList) {
//				if (instance.getDbName().equals(dbName)) {
//					return instance;
//				}
//			}
//
//			return null;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	/**
//	 * Are the instances equal?
//	 * 
//	 * @param instance
//	 *            one
//	 * @param instance
//	 *            two
//	 * @return true if they are equal
//	 */
//	public boolean instanceEqual(DatabaseInstance i1, DatabaseInstance i2) {
//		double metering = Metering.start();
//
//		if (!i1.getDbName().equals(i2.getDbName())) {
//			return false;
//		}
//		if (!i1.getHost().equals(i2.getHost())) {
//			return false;
//		}
//		if (!i1.getId().equals(i2.getId())) {
//			return false;
//		}
//		if (!i1.getPort().equals(i2.getPort())) {
//			return false;
//		}
//		if (!i1.getUser().equals(i2.getUser())) {
//			return false;
//		}
//		if (i1.isProtectedByPassword() != i2.isProtectedByPassword()) {
//			return false;
//		}
//
//		Metering.stop(metering);
//		return true;
//	}
//
//	/**
//	 * 
//	 */
//	@Override
//	public boolean login(String dbName, String passwd) {
//		double metering = Metering.start();
//
//		DatabaseInstance dbInstance = getDatabaseInstance(dbName);
//
//		if (dbInstance == null) {
//			return false;
//		}
//
//		LOGGER.debug("selected database: " + dbInstance.getDbName());
//		LOGGER.debug("    host: " + dbInstance.getHost());
//		LOGGER.debug("    is pw protected: " + dbInstance.isProtectedByPassword());
//
//		IPersistenceProvider dbConnection = null;
//
//		try {
//			if (dbInstance.isProtectedByPassword()) {
//				dbConnection = FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest()
//						.getSession(), dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName(), passwd);
//			} else {
//				dbConnection = FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest()
//						.getSession(), dbInstance.getHost(), dbInstance.getPort(), dbInstance.getDbName());
//			}
//		} catch (WrongCredentialsException e) {
//			LOGGER.warn(e.getMessage());
//			return false;
//		}
//
//		if (dbConnection == null) {
//			LOGGER.warn("Can't connect to database..");
//			return false;
//		}
//
////		getUser().setCurrentAccount(dbInstance);
//		getUser().setCurrentPersistenceProvider(dbConnection);
//
////		AccountDetails details = UiPersistence.getUiProvider().loadAccountDetails(dbInstance.getId());
////		if (details == null) {
////			details = new AccountDetails();
////			details.setId(dbInstance.getId());
////			details.setAccountName(dbInstance.getDbName());
////			UiPersistence.getUiProvider().storeAccountDetails(details);
////		}
//
//		Metering.stop(metering);
//		return true;
//	}
//
//	/**
//	 * Checks if the database accepts the given password.
//	 * 
//	 * @param instance
//	 *            database which should be checked
//	 * 
//	 * @param passwd
//	 *            password for the database
//	 * 
//	 * @return true if password is correct
//	 */
//	@Override
//	public boolean checkPassword(String dbName, String passwd) {
//		DatabaseInstance instance = getDatabaseInstance(dbName);
//		if (instance == null) {
//			throw new RuntimeException("No database with name " + dbName + " found.");
//		}
//
//		if (!instance.isProtectedByPassword()) {
//			return true;
//		}
//
//		try {
//			FlexiblePersistenceProviderFactory.createPersistenceProvider(getThreadLocalRequest().getSession(),
//					instance.getHost(), instance.getPort(), instance.getDbName(), passwd);
//		} catch (WrongCredentialsException e) {
//			LOGGER.warn(e.getMessage());
//
//			return false;
//		}
//
//		return true;
//	}
//
//	@Override
//	public AccountDetails getAccountDetails() {
//		String accountId = getUser().getCurrentDatabaseId();
//		//return UiPersistence.getUiProvider().loadAccountDetails(accountId);
//		return null;
//	}
//
//	@Override
//	public void storeAccountDetails(AccountDetails details) {
//		UiPersistence.getUiProvider().storeAccountDetails(details);
//	}
//
//	@Override
//	public boolean accountExists(String accountName) {
//		try {
//			List<DatabaseInstance> accountList = UiPersistence.getMetaProvider().loadAllDatabaseInstances();
//
//			for (DatabaseInstance instance : accountList) {
//				if (instance.getDbName().equals(accountName)) {
//					return true;
//				}
//			}
//			return false;
//		} catch (DataNotFoundException e) {
//			return false;
//		}
//
//	}
//}
