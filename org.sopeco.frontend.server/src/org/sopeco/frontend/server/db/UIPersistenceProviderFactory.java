package org.sopeco.frontend.server.db;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UIPersistenceProviderFactory {

	private UIPersistenceProviderFactory() {
	}

	private static final String DB_URL = "javax.persistence.jdbc.url";
	private static final String DB_USER = "javax.persistence.jdbc.user";
	private static final String DB_PASSWD = "javax.persistence.jdbc.password";

	private static String databaseHost = "deqkal276.qkal.sap.corp";
	private static String databasePort = "1527";
	private static String databaseName = "testdb";
	private static final String DATABASE_USER = "sopeco_frontend";
	private static String databasePasswd = "";
	private static final String SERVER_URL_PREFIX = "jdbc:derby://";
	private static final String SERVER_URL_SUFFIX = ";create=true";
	private static final String DATABASE_NAME_SUFFIX = "-frontend";

	/**
	 * 
	 * @return
	 */
	public static UIPersistenceProvider createUIPersistenceProvider( String host, String port,
			String dbName, String password) {

		databaseHost = host;
		databasePort = port;
		databaseName = dbName + DATABASE_NAME_SUFFIX;
		//
		if (!password.isEmpty()) {
			databasePasswd = password.replaceAll("[^a-zA-Z0-9_]", "");
		}

		try {
			Map<String, Object> configOverrides = getConfigOverrides();

			EntityManagerFactory factory = Persistence.createEntityManagerFactory("sopeco-frontend", configOverrides);

//			PersistenceProvider.setUIPersistenceProvider(new UIPersistenceProvider(factory), session);

			return new UIPersistenceProvider(factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private static Map<String, Object> getConfigOverrides() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();

		configOverrides.put(DB_URL, getServerUrl());
		// configOverrides.put(DB_USER, DATABASE_USER);
		// configOverrides.put(DB_PASSWD, databasePasswd);

		return configOverrides;
	}

	/**
	 * 
	 * @return
	 */
	private static String getServerUrl() {
		String serverUrl = SERVER_URL_PREFIX + databaseHost + ":" + databasePort + "/" + databaseName;

		if (!databasePasswd.isEmpty()) {
			serverUrl += ";user=" + DATABASE_USER + ";password=" + databasePasswd;
		}

		serverUrl += SERVER_URL_SUFFIX;

		return serverUrl;
	}
}
