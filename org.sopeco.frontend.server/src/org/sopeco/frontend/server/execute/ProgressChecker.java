package org.sopeco.frontend.server.execute;

import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressChecker extends Thread {

	private static ProgressChecker checker;
	private static final long CHECK_INTERVAL = 250;
	private static final long IDLE_TIMEOUT = 10000;

	public static ProgressChecker get() {
		if (checker == null) {
			checker = new ProgressChecker();
			checker.start();
		}
		return checker;
	}

	private boolean running;

	public synchronized void check() {
		checker.notify();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			for (ControllerQueue queue : ControllerQueueManager.getAllQueues()) {
				while (StatusBroker.get().getManager(queue.getCurrentToken()).hasNext()) {
					StatusMessage currentStatus = StatusBroker.get().getManager(queue.getCurrentToken()).next();
					System.out.println("Status: " + currentStatus.getEventType());
				}
				if (!queue.isExecuting()) {
					queue.finished();
				}
			}

			waiting();
		}
		checker = null;
	}

	private synchronized void waiting() {
		try {
			if (ControllerQueueManager.activeQueueCount() <= 0) {
				checker.wait(IDLE_TIMEOUT);
			} else {
				checker.wait(CHECK_INTERVAL);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}