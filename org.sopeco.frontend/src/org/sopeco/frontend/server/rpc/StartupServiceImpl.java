package org.sopeco.frontend.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.frontend.client.rpc.StartupService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Class which loading all important server-settings etc.
 * 
 * @author Marius Oehler
 *
 */
public class StartupServiceImpl extends RemoteServiceServlet implements
		StartupService {

	private static final Logger logger = LoggerFactory
			.getLogger(StartupServiceImpl.class);

	private static final long serialVersionUID = 1L;
	private final String CONFIGURATION_FILE = "sopeco-gui.conf";

	private static boolean loaded = false;

	@Override
	public boolean start() {
		if (!loaded) {
			logger.debug("loading");
			load();
		} else {
			logger.debug("already loaded");
		}

		return false;
	}

	private void load() {
		loaded = true;

		try {
			Configuration.getSingleton().loadConfiguration(this.getClass().getClassLoader(), CONFIGURATION_FILE);
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}

	}
}
