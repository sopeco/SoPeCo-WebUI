package org.sopeco.frontend.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class StartUp implements ServletContextListener {

	private final String configurationFile = "sopeco-gui.conf";

	public StartUp() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Destroy");
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		System.out.println(">> Starting webapp..");
		try {
			loadConfiguration();
			Scheduler.startScheduler();
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	private void loadConfiguration() throws ConfigurationException {
		Configuration.getSessionSingleton(Configuration.getGlobalSessionId()).loadConfiguration(
				StartUp.class.getClassLoader(), configurationFile);
		
		IConfiguration cc =  Configuration.getSessionSingleton(Configuration.getGlobalSessionId());
	}
}
