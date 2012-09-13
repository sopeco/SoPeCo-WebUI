package org.sopeco.frontend.server.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.config.PersistenceConfiguration;

public class FlexiblePersistenceProviderFactory extends PersistenceProviderFactory {
	private static Logger logger = LoggerFactory.getLogger(FlexiblePersistenceProviderFactory.class);

	public static IPersistenceProvider createPersistenceProvider(String host, String port, String dbName) {
		return createPersistenceProvider(host, port, dbName, null);
	}

	public static IPersistenceProvider createPersistenceProvider(String host, String port, String dbName,
			String password) {
		if (password != null) {
			PersistenceConfiguration.getSingleton().setUsePassword(true);
			PersistenceConfiguration.getSingleton().updateDBPassword(password);
		} else {
			PersistenceConfiguration.getSingleton().setUsePassword(false);
		}
		PersistenceConfiguration.getSingleton().updateDBHost(host);
		PersistenceConfiguration.getSingleton().updateDBPort(port);
		PersistenceConfiguration.getSingleton().updateDBName(dbName);
		Object [] hpn = {host,port,dbName};
		logger.debug("Creating a new persistence provider for {}:{}/{}", hpn);
		persistenceProviderInstance = createJPAPersistenceProvider();

		return persistenceProviderInstance;
	}
}
