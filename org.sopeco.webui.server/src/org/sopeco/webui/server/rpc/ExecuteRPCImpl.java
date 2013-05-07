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
package org.sopeco.webui.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.config.Configuration;
import org.sopeco.webui.client.rpc.ExecuteRPC;
import org.sopeco.webui.server.execute.ControllerQueueManager;
import org.sopeco.webui.server.helper.ScheduleExpression;
import org.sopeco.webui.server.persistence.UiPersistence;
import org.sopeco.webui.server.persistence.entities.ScheduledExperiment;
import org.sopeco.webui.shared.entities.ExecutedExperimentDetails;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;
import org.sopeco.webui.shared.entities.MECLog;
import org.sopeco.webui.shared.entities.RunningControllerStatus;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteRPCImpl extends SuperRemoteServlet implements ExecuteRPC {

	/** */
	private static final long serialVersionUID = 1L;

	// private static final Logger LOGGER =
	// Logger.getLogger(ExecuteRPCImpl.class.getName());

	@Override
	public void scheduleExperiment(FrontendScheduledExperiment rawScheduledExperiment) {
		ScheduledExperiment scheduledExperiment = new ScheduledExperiment(rawScheduledExperiment);
		scheduledExperiment.setActive(true);
		scheduledExperiment.setLastExecutionTime(-1);
		scheduledExperiment.setAddedTime(System.currentTimeMillis());
		scheduledExperiment.setProperties(Configuration.getSessionSingleton(getSessionId()));

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
		String accountName = getUser().getCurrentDatabase().getId();
		List<ScheduledExperiment> resultList = UiPersistence.getUiProvider().loadScheduledExperimentsByAccount(
				accountName);

		// List<Long> runningExperiments =
		// ControllerQueueManager.getRunningExperimentIds();

		List<FrontendScheduledExperiment> fseList = new ArrayList<FrontendScheduledExperiment>();
		for (ScheduledExperiment experiment : resultList) {
			// System.out.println(runningExperiments.contains(experiment.getId()));
			fseList.add(experiment.createFrontendScheduledExperiment());
		}
		return fseList;
	}

	@Override
	public boolean removeScheduledExperiment(long id) {
		ScheduledExperiment exp = UiPersistence.getUiProvider().loadScheduledExperiment(id);
		if (exp != null && exp.getAccountId().equals(getUser().getCurrentAccount().getId())) {
			UiPersistence.getUiProvider().removeScheduledExperiment(exp);
			return true;
		}
		return false;
	}

	@Override
	public boolean setScheduledExperimentEnabled(long id, boolean enabled) {
		ScheduledExperiment exp = UiPersistence.getUiProvider().loadScheduledExperiment(id);
		if (exp != null && exp.getAccountId().equals(getUser().getCurrentAccount().getId())) {
			exp.setActive(enabled);
			UiPersistence.getUiProvider().storeScheduledExperiment(exp);
			return true;
		}
		return false;
	}

	@Override
	public List<ExecutedExperimentDetails> getExecutedExperimentDetails() {
		String accountId = getUser().getCurrentDatabase().getId();
		String scenarioName = getUser().getAccountDetails().getSelectedScenario();

		return UiPersistence.getUiProvider().loadExecutedExperimentDetails(accountId, scenarioName);
	}

	@Override
	public MECLog getMECLog(long id) {
		return UiPersistence.getUiProvider().loadMECLog(id);
	}

	@Override
	public RunningControllerStatus getControllerLog() {
		return ControllerQueueManager.get(getUser().getAccountDetails().getControllerUrl())
				.createControllerStatusPackage();
	}

	@Override
	public void abortCurrentExperiment() {
		ControllerQueueManager.get(getUser().getAccountDetails().getControllerUrl()).abortExperiment();
	}
}
