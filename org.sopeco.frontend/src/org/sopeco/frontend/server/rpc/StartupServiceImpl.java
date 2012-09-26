package org.sopeco.frontend.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.frontend.client.rpc.StartupService;
import org.sopeco.frontend.server.db.UIPersistenceProvider;
import org.sopeco.frontend.server.db.UIPersistenceProviderFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Class which loading all important server-settings etc.
 * 
 * @author Marius Oehler
 * 
 */
public class StartupServiceImpl extends RemoteServiceServlet implements StartupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartupServiceImpl.class);

	private static final long serialVersionUID = 1L;
	private final String CONFIGURATION_FILE = "sopeco-gui.conf";

	private static boolean loaded = false;

	@Override
	public boolean start() {
		if (!loaded) {
			LOGGER.debug("loading");
			load();
		} else {
			LOGGER.debug("already loaded");
		}

		return false;
	}

	private void load() {
		loaded = true;

		try {
			Configuration.getSessionSingleton(getThreadLocalRequest().getSession().getId()).loadConfiguration(
					this.getClass().getClassLoader(), CONFIGURATION_FILE);
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}

	}
}
