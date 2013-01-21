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
package org.sopeco.frontend.server.execute;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.server.helper.ScheduleExpression;
import org.sopeco.frontend.server.persistence.UiPersistence;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExecuteScheduler {

	private static final Logger LOGGER = Logger.getLogger(ExecuteScheduler.class.getName());
	private static ExecuteScheduler executeScheduler;

	private ExecuteScheduler() {
	}

	/**
	 * Returns the ExecuteScheduler Singleton object. If the object doesn't
	 * exists, it will be created.
	 * 
	 * @return ExecuteScheduler
	 */
	public static ExecuteScheduler get() {
		if (executeScheduler == null) {
			executeScheduler = new ExecuteScheduler();
		}
		return executeScheduler;
	}

	/**
	 * Checks all stored experiments if one of them should be executed now.
	 */
	public void checkExperiments() {
		LOGGER.fine("Checking for scheduled experiments");
		try {
			List<ScheduledExperiment> experimentList = UiPersistence.getUiProvider().loadAllScheduledExperiments();

			for (ControllerQueue queue : ControllerQueueManager.getAllQueues()) {
				queue.check();
			}
			
			for (ScheduledExperiment experiment : experimentList) {
				if (experiment.getNextExecutionTime() < System.currentTimeMillis() && experiment.isActive()) {
					// Experiment will be executed
					queueExperiment(experiment);
				} else if (experiment.getNextExecutionTime() < System.currentTimeMillis() && experiment.isRepeating()) {
					// Calculates the next execution time.
					updateNextExecutionTime(experiment);
				}
			}
		} catch (Exception e) {
			LOGGER.severe(e.getLocalizedMessage());
		}
	}

	/**
	 * Inserts an experiment in the proper controller queue. Previously a
	 * QueuedExperiment is created, which contains all relevant information for
	 * the controllerQueue to execute it.
	 * 
	 * @param experiment
	 *            to insert into a queue
	 */
	private void queueExperiment(ScheduledExperiment experiment) {
		LOGGER.info("Insert experiment '" + experiment.getLabel() + "' (id: " + experiment.getId() + " - account: "
				+ experiment.getAccountId() + ") in queue");

		ControllerQueueManager.get(experiment.getControllerUrl()).addExperiment(experiment.createQueuedExperiment());

		if (experiment.isRepeating()) {
			LOGGER.info("Update execution times");
			experiment.setLastExecutionTime(System.currentTimeMillis());
			updateNextExecutionTime(experiment);
		} else {
			LOGGER.info("Remove ScheduleExperiment");
			UiPersistence.getUiProvider().removeScheduledExperiment(experiment);
		}

		// notifyFrontend(experiment.getAccount());
	}

	/**
	 * Calculates the next date when the experiment is to be executed.
	 * 
	 * @param experiment
	 */
	private void updateNextExecutionTime(ScheduledExperiment experiment) {
		long nextRepetition = ScheduleExpression.nextValidDate(experiment.getRepeatDays(), experiment.getRepeatHours(),
				experiment.getRepeatMinutes());
		experiment.setNextExecutionTime(nextRepetition);
		UiPersistence.getUiProvider().storeScheduledExperiment(experiment);
	}
}
