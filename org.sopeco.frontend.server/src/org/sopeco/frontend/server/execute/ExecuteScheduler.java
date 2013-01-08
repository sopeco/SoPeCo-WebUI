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
				+ experiment.getAccount() + ") in queue");

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
