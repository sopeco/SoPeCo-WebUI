package org.sopeco.frontend.server.execute;

import java.util.logging.Logger;

import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressWatcher extends Thread {

	private static final Logger LOGGER = Logger.getLogger(ProgressWatcher.class.getName());

	/** Singleton object of the ProgressWatcher */
	private static ProgressWatcher checker;

	private static final long CHECK_INTERVAL = 250;
	private static final long IDLE_TIMEOUT = 10000;

	/**
	 * Returns the singleton object of this class. If it doesn't exist, it will
	 * be created and started (because it's a thread).
	 * 
	 * @return
	 */
	public static ProgressWatcher get() {
		if (checker == null) {
			checker = new ProgressWatcher();
			checker.start();
		}
		return checker;
	}

	/** while this variable is true, the thread is checking the queues. */
	private boolean running;

	public synchronized void continueLoop() {
		checker.notify();
	}

	/**
	 * Constructor which sets the thread as a daemon.
	 */
	public ProgressWatcher() {
		setDaemon(true);
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			LOGGER.fine("Running Queues: " + ControllerQueueManager.getAllRunningQueues().size() + "/"
					+ ControllerQueueManager.getAllQueues().size());

			for (ControllerQueue queue : ControllerQueueManager.getAllRunningQueues()) {
				String token = queue.getCurrentToken();
				while (token != null && StatusBroker.get().getManager(token).hasNext()) {
					StatusMessage currentStatus = StatusBroker.get().getManager(queue.getCurrentToken()).next();
					System.out.println("Status: " + currentStatus.getEventType());

					queue.notifyStatusChange(currentStatus.getEventType());
				}
				if (!queue.isExecuting()) {
					queue.finished();
				}
			}

			waiting();
		}
	}

	/**
	 * Waits a certain time until the next run, to decrease the number of
	 * cycles.
	 */
	private synchronized void waiting() {
		try {
			if (ControllerQueueManager.hasRunningQueues()) {
				checker.wait(CHECK_INTERVAL);
			} else {
				checker.wait(IDLE_TIMEOUT);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}