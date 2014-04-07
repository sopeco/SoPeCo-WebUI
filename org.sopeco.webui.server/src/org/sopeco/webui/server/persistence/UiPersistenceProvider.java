package org.sopeco.webui.server.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.webui.server.UiConfiguration;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.entities.account.RememberMeToken;

/**
 * Visiblity of database modification methods is worldwide. The methods can only be
 * accessed via the class singleton, which can be requested via {@link getInstance()}.
 * 
 * @author Peter Merkert
 */
public final class UiPersistenceProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UiPersistenceProvider.class.getName());

	/**
	 * The entitymanagerfactory is like a thread pol. Entitmanagers to execute database queries
	 * can be created with this factory.
	 * This factory handles a connection pool automatically.
	 * 
	 * @Todo close the emf down at end of program life
	 */
	private EntityManagerFactory emf;

	/**
	 * Singleton instance.
	 */
	private static UiPersistenceProvider singleton;
	
	/**
	 * Database settings for JDBC
	 */
	private static final String DB_URL = "javax.persistence.jdbc.url";
	private static final String SERVER_URL_PREFIX = "jdbc:derby://";
	private static final String SERVER_URL_SUFFIX = ";create=true";

	/**
	 * Hidden constructor as a contructor for singleton. Get an instance by calling {@link getInstance()}.
	 */
	private UiPersistenceProvider() {
		
		try {
			emf = Persistence.createEntityManagerFactory("sopeco-webui", getConfigOverrides());
		} catch (ConfigurationException ce) {
			LOGGER.warn("Could not load the configuration files");
			LOGGER.warn(ce.getLocalizedMessage());
		} catch (Exception e) {
			LOGGER.warn(e.getLocalizedMessage());
			throw new IllegalArgumentException("Could not create persistence provider!", e);
		}
		
	}

	/**
	 * The contructor sets the {@link EntityManagerFactory}. It's used
	 * to load, store and remove items from the database.
	 * 
	 * @param factory the EntityManagerFactory
	 */
	UiPersistenceProvider(EntityManagerFactory factory) {
		emf = factory;
	}

	public List<Visualization> loadAllVisualizations() {
		return loadByQuery(Visualization.class, "getAllVisualizations");
	}

	public List<Visualization> loadVisualizationsByAccount(long accountId) {
		return loadByQuery(Visualization.class, "getVisualizationsByAccount", "accountId", accountId);
	}

	public void storeVisualization(Visualization visualization) {
		store(visualization);
	}

	public void removeVisualization(Visualization visualization) {
		remove(visualization);
	}

	public void storeRememberMeToken(RememberMeToken token) {
		store(token);
	}

	public RememberMeToken loadRememberMeToken(String tokenHash) {
		return loadSingleById(RememberMeToken.class, tokenHash);
	}

	public int deleteExpiredRememberMeToken() {
		return updateQuery("deleteExipredTokens", "expireDate", System.currentTimeMillis());
	}

	public void removeRememberMeToken(RememberMeToken rememberMeToken) {
		remove(rememberMeToken);
	}
	
	public AccountDetails loadAccountDetails(long accountId) {
		return loadSingleById(AccountDetails.class, accountId);
	}

	public List<AccountDetails> loadAllAccountDetails() throws DataNotFoundException {
		return loadByQuery(AccountDetails.class, "getAllAccountDetails");
	}

	public void removeAccountDetails(AccountDetails accountDetails) {
		remove(accountDetails);
	}

	public void storeAccountDetails(AccountDetails accountDetails) {
		store(accountDetails);
	}

	/********************************************************************/

	private <T> T store(T object) {
		EntityManager em = emf.createEntityManager();
		T managedObject = null;
		try {
			em.getTransaction().begin();
			managedObject = em.merge(object);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		return managedObject;
	}

	private <T> void remove(T object) {
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			T removeObject = em.merge(object);
			em.remove(removeObject);
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
	}

	private <T> T loadSingleById(Class<T> returnClazz, Object primaryKey) {
		EntityManager em = emf.createEntityManager();
		T entity = em.find(returnClazz, primaryKey);
		em.close();
		return entity;
	}

	private <T> T loadSingleByQuery(Class<T> returnClazz, String queryName, Object... parameterList) {
		T result = null;
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<T> query = em.createNamedQuery(queryName, returnClazz);
			for (int i = 0; i <= parameterList.length / 2; i += 2) {
				query.setParameter((String) parameterList[i], parameterList[i + 1]);
			}
			result = query.getSingleResult();
		} catch (NoResultException e) {
			String parameter = "[";
			for (int i = 0; i < parameterList.length; i++) {
				parameter += (i == 0 ? "" : ", ") + parameterList[i];
			}
			parameter += "]";
			LOGGER.debug("No result with query '" + queryName + "' with parameter " + parameter);
		} catch (NonUniqueResultException e) {
			String parameter = "[";
			for (int i = 0; i < parameterList.length; i++) {
				parameter += (i == 0 ? "" : ", ") + parameterList[i];
			}
			parameter += "]";
			LOGGER.debug("No unique result with query '" + queryName + "' with parameter " + parameter);
		} catch (IllegalStateException e) {
			LOGGER.error("Query '" + queryName + "' failed: " + e);
		} finally {
			em.close();
		}
		return result;
	}

	private <T> List<T> loadByQuery(Class<T> clazz, String queryName, Object... parameterList) {
		List<T> result = new ArrayList<T>();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
			for (int i = 0; i + 1 < parameterList.length; i += 2) {
				query.setParameter((String) parameterList[i], parameterList[i + 1]);
			}
			result = query.getResultList();
		} catch (IllegalStateException e) {
			LOGGER.error("Query '" + queryName + "' failed: " + e);
		} finally {
			em.close();
		}
		return result;
	}

	private int updateQuery(String queryName, Object... parameterList) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createNamedQuery(queryName);
		for (int i = 0; i <= parameterList.length / 2; i += 2) {
			query.setParameter((String) parameterList[i], parameterList[i + 1]);
		}
		int count = 0;
		try {
			em.getTransaction().begin();
			count = query.executeUpdate();
			em.getTransaction().commit();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			em.close();
		}
		return count;
	}
	

	/*******************************Database configuration*************************************/
	
	/**
	 * Creates a new ServicePersistenceProvider to access the database.
	 * 
	 * @return ServicePersistenceProvider to access database
	 */
	public static UiPersistenceProvider getInstance() {
		
		if (singleton == null) {
			singleton = new UiPersistenceProvider();
		}
		
		return singleton;
	}

	/**
	 * Creates a configuration map, which contains the connection url to the
	 * database.
	 * 
	 * @return configuration for database
	 */
	private static Map<String, Object> getConfigOverrides() throws ConfigurationException {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put(DB_URL, getServerUrl());
		LOGGER.debug("Database connection string: {}", configOverrides.get(DB_URL));
		return configOverrides;
	}

	/**
	 * Builds the connection-url of the SoPeCo service database.
	 * 
	 * @return connection-url to the database
	 */
	private static String getServerUrl() throws ConfigurationException {
		
		if (UiConfiguration.PERSISTENCE_HOST == null) {
			throw new NullPointerException("No MetaDataHost defined.");
		}
		
		String host = UiConfiguration.PERSISTENCE_HOST;
		String port = String.valueOf(UiConfiguration.PERSISTENCE_PORT);
		String name = UiConfiguration.PERSISTENCE_NAME;
		String user = UiConfiguration.PERSISTENCE_USER;
		String password = UiConfiguration.PERSISTENCE_PASSWORD;
		
		return SERVER_URL_PREFIX 	+ host + ":" + port
									+ "/" + name
									+ ";user=" + user
									+ ";password=" + password
									+ SERVER_URL_SUFFIX;
	}
}
