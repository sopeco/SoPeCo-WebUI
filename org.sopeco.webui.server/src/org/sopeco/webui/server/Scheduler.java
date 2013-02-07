/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;
import org.sopeco.webui.server.execute.ExecuteScheduler;

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
			LOGGER.finer("SchedulerAction starts..");

			ContiniousChecker.checkUserTimeout();
			ExecuteScheduler.get().checkExperiments();
			ContiniousChecker.checkSocketMEController();

			LOGGER.finer("SchedulerAction is finished");
		}
	}
}
