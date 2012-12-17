package org.sopeco.frontend.server.execute;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.server.helper.ScheduleExpression;
import org.sopeco.frontend.server.persistence.UiPersistence;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.server.rpc.PushRPCImpl;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.push.PushPackage;

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
				if (experiment.getNextExecutionTime() < System.currentTimeMillis() && experiment.isActive()) {
					queueExperiment(experiment);
				} else if (experiment.getNextExecutionTime() < System.currentTimeMillis() && experiment.isRepeating()) {
					updateNextExecutionTime(experiment);
					sendUserNewList(experiment.getAccount());
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
			experiment.setLastExecutionTime(System.currentTimeMillis());
			updateNextExecutionTime(experiment);
		} else {
			UiPersistence.getUiProvider().removeScheduledExperiment(experiment);
		}

		sendUserNewList(experiment.getAccount());
	}

	private void updateNextExecutionTime(ScheduledExperiment experiment) {
		long nextRepetition = ScheduleExpression.nextValidDate(experiment.getRepeatDays(), experiment.getRepeatHours(),
				experiment.getRepeatMinutes());
		experiment.setNextExecutionTime(nextRepetition);
		UiPersistence.getUiProvider().storeScheduledExperiment(experiment);
	}

	public void sendUserNewList(String accountName) {
		PushPackage push = new PushPackage(Type.SCHEDULED_EXPERIMENTS);
		for (String userSessionID : UserManager.getAllUsers().keySet()) {
			if (UserManager.getUser(userSessionID).getCurrentAccount().getDbName().equals(accountName)) {
				PushRPCImpl.push(userSessionID, push);
			}
		}
	}
}
