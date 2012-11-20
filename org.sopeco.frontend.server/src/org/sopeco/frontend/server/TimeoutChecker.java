package org.sopeco.frontend.server;

import java.util.logging.Logger;

import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class TimeoutChecker {

	private static final int REPEATE_INTERVAL = 60000;
	private static final int USER_TIMEOUT = 3600000;
	private static final Logger LOGGER = Logger.getLogger(TimeoutChecker.class.getName());

	private static TimeoutThread timeoutThread;
	private static boolean running;

	private TimeoutChecker() {
	}

	public static synchronized void start() {
		if (timeoutThread == null) {
			timeoutThread = new TimeoutThread();
			timeoutThread.start();
		}
	}

	private static class TimeoutThread extends Thread {
		@Override
		public void run() {
			LOGGER.info("Starting TimeoutThread..");
			running = true;
			try {
				while (running) {
					Thread.sleep(REPEATE_INTERVAL);
					LOGGER.fine("Checking user-timeouts..");
					for (User u : UserManager.getAllUsers().values()) {
						if (System.currentTimeMillis() - u.getLastRequestTime() > USER_TIMEOUT) {
							UserManager.removeUser(u);
						}
					}
				}
			} catch (InterruptedException e) {
				new RuntimeException(e);
			}
		}
	}
}
