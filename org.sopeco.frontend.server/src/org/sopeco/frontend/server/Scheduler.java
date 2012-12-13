package org.sopeco.frontend.server;

import java.util.logging.Logger;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Scheduler {

	private static final Logger LOGGER = Logger.getLogger(Scheduler.class.getName());

	private Scheduler() {
	}

	private static final int REPEATE_INTERVAL = 5000;
	private static ScheduleTimer scheduler;

	public static synchronized void startScheduler() {
		if (scheduler == null) {
			LOGGER.info("Starting SchedulerThread.");
			scheduler = new ScheduleTimer();
			scheduler.start();
		}
	}

	public static synchronized void stopScheduler() {
		if (scheduler != null) {
			LOGGER.info("Stopping SchedulerThread.");
			scheduler.running = false;
			scheduler.monitor.notify();
		}
	}

	/**
	 * This thread is repeated constantly and performs every minute an action.
	 */
	private static class ScheduleTimer extends Thread {
		private Object monitor = new Object();
		private boolean running;

		@Override
		public void run() {
			LOGGER.info("SchedulerThread started.");
			running = true;
			try {
				while (running) {
					synchronized (monitor) {
						new SchedulerAction().start();
						monitor.wait(REPEATE_INTERVAL);
					}
				}
			} catch (InterruptedException e) {
				new RuntimeException(e);
			}
			LOGGER.info("SchedulerThread stopped.");
		}
	}

	/**
	 * After each time interval, a thread (this class) will be created and
	 * executed.
	 */
	private static class SchedulerAction extends Thread {
		@Override
		public void run() {
			LOGGER.fine("SchedulerAction starts..");

			TimeoutChecker.checkTimeout();

			LOGGER.fine("SchedulerAction is finished");
		}
	}
}
