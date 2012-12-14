package org.sopeco.frontend.server.execute;

import java.util.Date;
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

	public static ExecuteScheduler get() {
		if (executeScheduler == null) {
			executeScheduler = new ExecuteScheduler();
		}
		return executeScheduler;
	}

	/**
	 * 
	 */
	public void check() {
		try {
			List<ScheduledExperiment> experimentList = UiPersistence.getUiProvider().loadAllScheduledExperiments();
			for (ScheduledExperiment experiment : experimentList) {
				if (experiment.getNextExecutionTime() < System.currentTimeMillis()) {
					queueExperiment(experiment);
				} else {
					System.out.println("? " + experiment.getLabel() + new Date(experiment.getNextExecutionTime()));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void queueExperiment(ScheduledExperiment experiment) {
		LOGGER.info("Put experiment in CotnrollerQueue: " + experiment.getLabel() + " (account: "
				+ experiment.getAccount() + ")");

		QueuedExperiment queuedExperiment = new QueuedExperiment();
		queuedExperiment.setTimeQueued(System.currentTimeMillis());
		queuedExperiment.setScenarioDefinition(experiment.getScenarioDefinition());
		queuedExperiment.setControllerUrl(experiment.getControllerUrl());
		queuedExperiment.setConfiguration(experiment.getConfiguration());

		ControllerQueueManager.get(experiment.getControllerUrl()).addExperiment(queuedExperiment);

		if (experiment.isRepeating()) {
			long nextRepetition = ScheduleExpression.nextValidDate(experiment.getRepeatDays(),
					experiment.getRepeatHours(), experiment.getRepeatMinutes());
			experiment.setNextExecutionTime(nextRepetition);
			UiPersistence.getUiProvider().storeScheduledExperiment(experiment);
		} else {
			UiPersistence.getUiProvider().removeScheduledExperiment(experiment);
		}
	}
}
