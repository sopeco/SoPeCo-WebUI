package org.sopeco.frontend.server;

import java.util.logging.Logger;

import org.sopeco.config.Configuration;
import org.sopeco.config.exception.ConfigurationException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class StartUp {

	private StartUp() {
	}

	private static final Logger LOGGER = Logger.getLogger(StartUp.class.getName());
	private static final String CONFIGURATION_FILE = "sopeco-gui.conf";

	private static Boolean hasStarted = false;

	public static synchronized void start(String sessionId) {
		if (!hasStarted) {
			System.out.println(">> Starting backend..");
			try {
				loadConfiguration(sessionId);
				Scheduler.startScheduler();
				hasStarted = true;
			} catch (ConfigurationException e) {
				LOGGER.warning(e.getMessage());
			}
		}
	}

	private static void loadConfiguration(String sessionId) throws ConfigurationException {
		Configuration.getSessionSingleton(Configuration.getGlobalSessionId()).loadConfiguration(
				StartUp.class.getClassLoader(), CONFIGURATION_FILE);
	}
}
