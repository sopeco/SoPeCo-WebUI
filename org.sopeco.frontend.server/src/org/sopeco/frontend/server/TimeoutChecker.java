package org.sopeco.frontend.server;

import java.util.logging.Logger;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class TimeoutChecker {

	private static final Logger LOGGER = Logger.getLogger(TimeoutChecker.class.getName());
	private static final int DEFAULT_USER_TIMEOUT = 1000 * 3600;
	private static int userTimeout = -1;

	private TimeoutChecker() {
	}

	public static void checkTimeout() {
		if (userTimeout == -1) {
			IConfiguration config = Configuration.getSessionSingleton(Configuration.getGlobalSessionId());
			userTimeout = config.getPropertyAsInteger(UiConfiguration.USER_TIMEOUT, DEFAULT_USER_TIMEOUT);
		}

		for (User u : UserManager.getAllUsers().values()) {
			if (System.currentTimeMillis() - u.getLastRequestTime() > userTimeout) {
				LOGGER.fine("Removing user: " + u.getSessionId());
				UserManager.removeUser(u);
			}
		}
	}
}
