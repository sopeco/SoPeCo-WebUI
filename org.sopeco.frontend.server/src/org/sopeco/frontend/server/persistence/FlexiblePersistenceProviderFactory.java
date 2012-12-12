package org.sopeco.frontend.server.persistence;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.PersistenceProviderFactory;
import org.sopeco.persistence.config.PersistenceConfiguration;

/**
 * 
 *
 */
public class FlexiblePersistenceProviderFactory extends PersistenceProviderFactory {
	private static Logger logger = LoggerFactory.getLogger(FlexiblePersistenceProviderFactory.class);

	public static IPersistenceProvider createPersistenceProvider(HttpSession session, String host, String port,
			String dbName) {
		return createPersistenceProvider(session, host, port, dbName, null);
	}

	public static IPersistenceProvider createPersistenceProvider(HttpSession session, String host, String port,
			String dbName, String password) {
		if (password != null) {
			PersistenceConfiguration.getSessionSingleton(session.getId()).setUsePassword(true);
			PersistenceConfiguration.getSessionSingleton(session.getId()).updateDBPassword(password);
		} else {
			PersistenceConfiguration.getSessionSingleton(session.getId()).setUsePassword(false);
		}
		PersistenceConfiguration.getSessionSingleton(session.getId()).updateDBHost(host);
		PersistenceConfiguration.getSessionSingleton(session.getId()).updateDBPort(port);
		PersistenceConfiguration.getSessionSingleton(session.getId()).updateDBName(dbName);
		Object[] hpn = { host, port, dbName };
		logger.debug("Creating a new persistence provider for {}:{}/{}", hpn);

		FlexiblePersistenceProviderFactory factory = new FlexiblePersistenceProviderFactory();
		return factory.createJPAPersistenceProvider(session.getId());
	}
}
