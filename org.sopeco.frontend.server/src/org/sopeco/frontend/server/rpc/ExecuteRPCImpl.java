package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.config.Configuration;
import org.sopeco.frontend.client.rpc.ExecuteRPC;
import org.sopeco.frontend.server.helper.ScheduleExpression;
import org.sopeco.frontend.server.persistence.UiPersistence;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteRPCImpl extends SuperRemoteServlet implements ExecuteRPC {

	/** */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(ExecuteRPCImpl.class.getName());

	@Override
	public void scheduleExperiment(FrontendScheduledExperiment rawScheduledExperiment) {
		ScheduledExperiment scheduledExperiment = new ScheduledExperiment(rawScheduledExperiment);
		scheduledExperiment.setActive(true);
		scheduledExperiment.setLastExecutionTime(-1);
		scheduledExperiment.setAddedTime(System.currentTimeMillis());
		scheduledExperiment.setConfiguration(Configuration.getSessionSingleton(getSessionId()));

		long nextExecution = scheduledExperiment.getStartTime();
		if (scheduledExperiment.isRepeating()) {
			nextExecution = ScheduleExpression.nextValidDate(scheduledExperiment.getStartTime(),
					scheduledExperiment.getRepeatDays(), scheduledExperiment.getRepeatHours(),
					scheduledExperiment.getRepeatMinutes());
		}
		scheduledExperiment.setNextExecutionTime(nextExecution);

		UiPersistence.getUiProvider().storeScheduledExperiment(scheduledExperiment);
	}

	@Override
	public List<FrontendScheduledExperiment> getScheduledExperiments() {
		String accountName = getUser().getCurrentAccount().getDbName();
		List<ScheduledExperiment> resultList = UiPersistence.getUiProvider().loadScheduledExperimentsByAccount(
				accountName);

		List<FrontendScheduledExperiment> fseList = new ArrayList<FrontendScheduledExperiment>();
		for (ScheduledExperiment experiment : resultList) {
			fseList.add(experiment.getFrontendScheduledExperiment());
		}
		return fseList;
	}

	@Override
	public boolean removeScheduledExperiment(long id) {
		ScheduledExperiment exp = UiPersistence.getUiProvider().loadScheduledExperiment(id);
		if (exp != null && exp.getAccount().equals(getUser().getCurrentAccount().getDbName())) {
			UiPersistence.getUiProvider().removeScheduledExperiment(exp);
			return true;
		}
		return false;
	}

	@Override
	public boolean setScheduledExperimentEnabled(long id, boolean enabled) {
		ScheduledExperiment exp = UiPersistence.getUiProvider().loadScheduledExperiment(id);
		if (exp != null && exp.getAccount().equals(getUser().getCurrentAccount().getDbName())) {
			exp.setActive(enabled);
			UiPersistence.getUiProvider().storeScheduledExperiment(exp);
			return true;
		}
		return false;
	}
}
