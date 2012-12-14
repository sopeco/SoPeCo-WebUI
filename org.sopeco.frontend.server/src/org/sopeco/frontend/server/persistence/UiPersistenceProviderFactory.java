package org.sopeco.frontend.server.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.frontend.server.UiConfiguration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UiPersistenceProviderFactory {

	private static final String DB_URL = "javax.persistence.jdbc.url";
	private static final String SERVER_URL_PREFIX = "jdbc:derby://";
	private static final String SERVER_URL_SUFFIX = ";create=true";

	/**
	 * Hidden constructor.
	 */
	private UiPersistenceProviderFactory() {

	}

	/**
	 * Creates a new UiPersistenceProvider.
	 * 
	 * @return UiPersistenceProvider
	 */
	public static UiPersistenceProvider createUiPersistenceProvider() {
		try {
			Map<String, Object> x = getConfigOverrides();
			EntityManagerFactory factory = Persistence.createEntityManagerFactory("sopeco-frontend",
					getConfigOverrides());
			return new UiPersistenceProvider(factory);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not create peristence provider!", e);
		}
	}

	/**
	 * Creates a configuration map, which contains the connection url to the
	 * database.
	 * 
	 * @return
	 */
	private static Map<String, Object> getConfigOverrides() {
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put(DB_URL, getServerUrl());
		return configOverrides;
	}

	/**
	 * Builds the connection-url of the ui-database.
	 * 
	 * @return connection-url
	 */
	private static String getServerUrl() {
		IConfiguration config = Configuration.getSessionSingleton(Configuration.getGlobalSessionId());
		if (config.getPropertyAsStr(UiConfiguration.META_DATA_HOST) == null) {
			throw new NullPointerException("No MetaDataHost defined.");
		}
		String host = config.getPropertyAsStr(UiConfiguration.META_DATA_HOST);
		String port = config.getPropertyAsStr(UiConfiguration.META_DATA_PORT);
		String name = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_NAME);
		String user = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_USER);
		String password = config.getPropertyAsStr(UiConfiguration.SOPECO_UI_DATABASE_PASSWORD);
		return SERVER_URL_PREFIX + host + ":" + port + "/" + name + ";user=" + user + ";password=" + password
				+ SERVER_URL_SUFFIX;
	}
}
