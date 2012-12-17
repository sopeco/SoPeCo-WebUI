package org.sopeco.frontend.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.sopeco.frontend.server.execute.ExecuteScheduler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Scheduler {

	private static final Logger LOGGER = Logger.getLogger(Scheduler.class.getName());

	private Scheduler() {
	}

	private static final int REPEATE_INTERVAL = 5;
	private static SchedulerAction schedulerAction;
	private static ScheduledExecutorService scheduler;

	public static synchronized void startScheduler() {
		if (schedulerAction == null) {
			LOGGER.info("Starting SchedulerThread.");
			scheduler = Executors.newScheduledThreadPool(1);
			schedulerAction = new SchedulerAction();
			scheduler.scheduleAtFixedRate(schedulerAction, REPEATE_INTERVAL, REPEATE_INTERVAL, TimeUnit.SECONDS);
		}
	}

	public static synchronized void stopScheduler() {
		if (schedulerAction != null) {
			LOGGER.info("Stopping SchedulerThread.");
			scheduler.shutdown();
			schedulerAction = null;
		}
	}

	/**
	 *
	 */
	private static class SchedulerAction implements Runnable {

		public SchedulerAction() {
		}

		@Override
		public void run() {
			LOGGER.fine("SchedulerAction starts..");

			TimeoutChecker.checkTimeout();
			ExecuteScheduler.get().check();

			LOGGER.fine("SchedulerAction is finished");
		}
	}
}
