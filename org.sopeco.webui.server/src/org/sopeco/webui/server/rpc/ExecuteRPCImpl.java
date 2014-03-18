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

import javax.validation.constraints.Null;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.service.persistence.entities.AccountDetails;
import org.sopeco.service.persistence.entities.ExecutedExperimentDetails;
import org.sopeco.service.persistence.entities.MECLog;
import org.sopeco.service.persistence.entities.ScheduledExperiment;
import org.sopeco.service.rest.exchange.ExperimentStatus;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;
import org.sopeco.webui.shared.entities.RunningControllerStatus;
import org.sopeco.webui.shared.rpc.ExecuteRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteRPCImpl extends SPCRemoteServlet implements ExecuteRPC {

	/** */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteRPCImpl.class.getName());

	/*
	 * service /execution/schedule (POST)
	 * In the RESTful serivce, the scheduled experiment is NOT set active!
	 */
	@Override
	public void scheduleExperiment(FrontendScheduledExperiment rawScheduledExperiment) {
		requiredLoggedIn();
				
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
															 ServiceConfiguration.SVC_EXECUTE_SCHEDULE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		ScheduledExperiment scheduledExperiment = convertFrontendScheduledExperiment(rawScheduledExperiment);
		scheduledExperiment.setActive(true);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).post(Entity.entity(scheduledExperiment, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.info("The experiment could not be set to execution state. Most likely the "
						+ "ScheduledExperiment cannot be has the status 'active'.");
		}
	}

	@Override
	public List<FrontendScheduledExperiment> getScheduledExperiments() {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
						 									 ServiceConfiguration.SVC_EXECUTE_SCHEDULE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		List<ScheduledExperiment> listSE = r.readEntity(new GenericType<List<ScheduledExperiment>>() { });
		List<FrontendScheduledExperiment> listFSE = new ArrayList<FrontendScheduledExperiment>();
		
		for (ScheduledExperiment se : listSE) {
			listFSE.add(convertScheduledExperiment(se));
		}
		
		return listFSE;
	}

	@Override
	public boolean removeScheduledExperiment(long id) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
															 String.valueOf(id));
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).delete();

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean setScheduledExperimentEnabled(long id, boolean enabled) {
		requiredLoggedIn();
		
		WebTarget wt = null;
		
		if (enabled) {
			wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
													   String.valueOf(id),
													   ServiceConfiguration.SVC_EXECUTE_ENABLE);
		} else {
			wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
					 								   String.valueOf(id),
					 								   ServiceConfiguration.SVC_EXECUTE_DISABLE);
		}
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());

		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public List<ExecutedExperimentDetails> getExecutedExperimentDetails() {
		requiredLoggedIn();
		
		// first fetch the current selected scenario name
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				 											 ServiceConfiguration.SVC_ACCOUNT_INFO);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		AccountDetails ad = r.readEntity(AccountDetails.class);
		
		if (ad == null) {
			return new ArrayList<ExecutedExperimentDetails>();
		}
		
		// now request the executedExperimentDetails corresponding to the current selected scenario
		wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
			     								   ServiceConfiguration.SVC_EXECUTE_DETAILS);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_EXECUTE_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_EXECUTE_SCENARIONAME, ad.getSelectedScenario());
		
		r = wt.request(MediaType.APPLICATION_JSON).get();
		
		return r.readEntity(new GenericType<List<ExecutedExperimentDetails>>() { });
	}

	@Override
	public MECLog getMECLog(long id) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
						 									 ServiceConfiguration.SVC_EXECUTE_MECLOG);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_EXECUTE_ID, id);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		return r.readEntity(MECLog.class);
	}

	@Override
	public RunningControllerStatus getControllerLog() {
		requiredLoggedIn();
		
		// first fetch the current experiment key
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				 											 ServiceConfiguration.SVC_ACCOUNT_INFO);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		AccountDetails ad = r.readEntity(AccountDetails.class);
		
		if (ad == null) {
			return null;
		}
		
		wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
												   ServiceConfiguration.SVC_EXECUTE_STATUS);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_EXECUTE_KEY, ad.getExperimentKeyOfSelectedScenario());
		
		r = wt.request(MediaType.APPLICATION_JSON).get();
		
		ExperimentStatus es = r.readEntity(ExperimentStatus.class);

		if (r.getStatus() == Status.OK.getStatusCode()) {
			// now convert the ExperimentStatus object into a RunningControllerStatus object
			RunningControllerStatus rcs = new RunningControllerStatus();
			rcs.setAccount(es.getAccountId());
			rcs.setEventLogList(es.getEventLogList());
			rcs.setHasFinished(es.isFinished());
			rcs.setLabel(es.getLabel());
			rcs.setProgress(es.getProgress());
			rcs.setScenario(es.getScenarioName());
			rcs.setTimeRemaining(es.getTimeRemaining());
			rcs.setTimeStart(es.getTimeStart());
			
			return rcs;
		}
		
		return null;
	}

	@Override
	public void abortCurrentExperiment() {
		requiredLoggedIn();
		
		// first fetch the current experiment key
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				 											 ServiceConfiguration.SVC_ACCOUNT_INFO);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		AccountDetails ad = r.readEntity(AccountDetails.class);
		
		if (ad == null) {
			return;
		}
		
		wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_EXECUTE,
												   ServiceConfiguration.SVC_EXECUTE_ABORT);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_EXECUTE_KEY, ad.getExperimentKeyOfSelectedScenario());
		
		r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
	}
	
	

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////// HELPER ///////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Converts a {@link ScheduledExperiment} into a {@link FrontendScheduledExperiment}.
	 * 
	 * @param se	the {@link ScheduledExperiment}
	 * @return		the {@link FrontendScheduledExperiment}
	 */
	private FrontendScheduledExperiment convertScheduledExperiment(ScheduledExperiment se) {
		FrontendScheduledExperiment fse = convertScheduledExperiment(se);
		fse.setAccount(se.getAccountId());
		fse.setControllerUrl(se.getControllerUrl());
		fse.setId(se.getAccountId());
		fse.setLabel(se.getLabel());
		fse.setLastExecutionTime(se.getLastExecutionTime());
		fse.setNextExecutionTime(se.getNextExecutionTime());
		fse.setRepeatDays(se.getRepeatDays());
		fse.setRepeatHours(se.getRepeatHours());
		fse.setRepeating(se.isRepeating());
		fse.setRepeatMinutes(se.getRepeatMinutes());
		fse.setStartTime(se.getStartTime());
		fse.setAddTime(se.getAddedTime());
		fse.setEnabled(se.isActive());
		fse.setDurations(se.getDurations());
		fse.setScenarioDefinition(se.getScenarioDefinition());
		fse.setSelectedExperiments(se.getSelectedExperiments());
		return fse;
	}
	
	/**
	 * Converts a {@link FrontendScheduledExperiment} into a {@link ScheduledExperiment}.
	 * 
	 * @param fse	the {@link FrontendScheduledExperiment}
	 * @return		the {@link ScheduledExperiment}
	 */
	private ScheduledExperiment convertFrontendScheduledExperiment(FrontendScheduledExperiment fse) {
		ScheduledExperiment se = new ScheduledExperiment();
		se.setAccountId(fse.getAccount());
		se.setControllerUrl(fse.getControllerUrl());
		se.setLabel(fse.getLabel());
		se.setRepeatDays(fse.getRepeatDays());
		se.setRepeatHours(fse.getRepeatHours());
		se.setRepeating(fse.isRepeating());
		se.setRepeatMinutes(fse.getRepeatMinutes());
		se.setScenarioDefinition(fse.getScenarioDefinition());
		se.setStartTime(fse.getStartTime());
		se.setAddedTime(System.currentTimeMillis());
		se.setDurations(new ArrayList<Long>());
		se.setSelectedExperiments(fse.getSelectedExperiments());
		return se;
	}
	
}
