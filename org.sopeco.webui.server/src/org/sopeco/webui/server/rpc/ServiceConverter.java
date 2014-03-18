package org.sopeco.webui.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.service.persistence.entities.ScheduledExperiment;
import org.sopeco.webui.shared.entities.ExecutedExperimentDetails;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;
import org.sopeco.webui.shared.entities.MECLog;
import org.sopeco.webui.shared.entities.ScenarioDetails;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.helper.MECLogEntry;

public final class ServiceConverter {

	/**
	 * Converts a {@link ScheduledExperiment} into a {@link FrontendScheduledExperiment}.
	 * 
	 * @param se	the {@link ScheduledExperiment}
	 * @return		the {@link FrontendScheduledExperiment}
	 */
	public static FrontendScheduledExperiment convertScheduledExperiment(ScheduledExperiment se) {
		if (se == null) {
			return null;
		}
		
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
	public static ScheduledExperiment convertFrontendScheduledExperiment(FrontendScheduledExperiment fse) {
		if (fse == null) {
			return null;
		}
		
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
	
	public static AccountDetails convertToAccountDetails(org.sopeco.service.persistence.entities.AccountDetails accountDetails) {
		if (accountDetails == null) {
			return null;
		}
		
		AccountDetails ad = new AccountDetails();
		ad.setAccountName(accountDetails.getAccountName());
		ad.setCsvCommentSeparator(accountDetails.getCsvCommentSeparator());
		ad.setCsvDecimalDelimiter(accountDetails.getCsvDecimalDelimiter());
		ad.setCsvValueSeparator(accountDetails.getCsvValueSeparator());
		ad.setId(accountDetails.getId());
		ad.setSelectedScenario(accountDetails.getSelectedScenario());
		
		List<ScenarioDetails> list = new ArrayList<ScenarioDetails>();
		for (org.sopeco.service.persistence.entities.ScenarioDetails sd : accountDetails.getScenarioDetails()) {
			list.add(ServiceConverter.convertToScenarioDetails(sd));
		}
		ad.setScenarioDetails(list);
		
		return ad;
	}
	
	public static org.sopeco.service.persistence.entities.AccountDetails convertToServiceAccountDetails(AccountDetails accountDetails) {
		if (accountDetails == null) {
			return null;
		}
		
		org.sopeco.service.persistence.entities.AccountDetails ad = new org.sopeco.service.persistence.entities.AccountDetails();
		ad.setAccountName(accountDetails.getAccountName());
		ad.setCsvCommentSeparator(accountDetails.getCsvQuoteChar());
		ad.setCsvDecimalDelimiter(accountDetails.getCsvDecimalDelimiter());
		ad.setCsvValueSeparator(accountDetails.getCsvValueSeparator());
		ad.setId(accountDetails.getId());
		ad.setSelectedScenario(accountDetails.getSelectedScenario());
		
		List<org.sopeco.service.persistence.entities.ScenarioDetails> list = new ArrayList<org.sopeco.service.persistence.entities.ScenarioDetails>();
		for (ScenarioDetails sd : accountDetails.getScenarioDetails()) {
			list.add(convertToServiceScenarioDetails(sd));
		}
		ad.setScenarioDetails(list);
		
		return ad;
	}
	
	public static org.sopeco.service.persistence.entities.ScenarioDetails convertToServiceScenarioDetails(ScenarioDetails scenarioDetails) {
		if (scenarioDetails == null) {
			return null;
		}
		
		org.sopeco.service.persistence.entities.ScenarioDetails sd = new org.sopeco.service.persistence.entities.ScenarioDetails();
		sd.setControllerHost(scenarioDetails.getControllerHost());
		sd.setControllerName(scenarioDetails.getControllerName());
		sd.setControllerPort(scenarioDetails.getControllerPort());
		sd.setControllerProtocol(scenarioDetails.getControllerProtocol());
		sd.setScenarioName(scenarioDetails.getScenarioName());
		sd.setSelectedExperiment(scenarioDetails.getSelectedExperiment());
		sd.setSelectedSpecification(scenarioDetails.getSelectedSpecification());
		
		return sd;
	}
	
	public static ScenarioDetails convertToScenarioDetails(org.sopeco.service.persistence.entities.ScenarioDetails scenarioDetails) {
		if (scenarioDetails == null) {
			return null;
		}
		
		ScenarioDetails sd = new ScenarioDetails();
		sd.setControllerHost(scenarioDetails.getControllerHost());
		sd.setControllerName(scenarioDetails.getControllerName());
		sd.setControllerPort(scenarioDetails.getControllerPort());
		sd.setControllerProtocol(scenarioDetails.getControllerProtocol());
		sd.setScenarioName(scenarioDetails.getScenarioName());
		sd.setSelectedExperiment(scenarioDetails.getSelectedExperiment());
		sd.setSelectedSpecification(scenarioDetails.getSelectedSpecification());
		
		return sd;
	}
	
	public static ExecutedExperimentDetails convertToExecutedExperimentDetails(org.sopeco.service.persistence.entities.ExecutedExperimentDetails executedExperimentDetails) {
		if (executedExperimentDetails == null) {
			return null;
		}
		
		ExecutedExperimentDetails eed = new ExecutedExperimentDetails();
		eed.setAccountId(executedExperimentDetails.getAccountId());
		eed.setControllerURL(executedExperimentDetails.getControllerURL());
		eed.setId(executedExperimentDetails.getId());
		eed.setName(executedExperimentDetails.getName());
		eed.setScenarioName(executedExperimentDetails.getScenarioName());
		eed.setSuccessful(executedExperimentDetails.isSuccessful());
		eed.setTimeFinished(executedExperimentDetails.getTimeFinished());
		eed.setTimeStarted(executedExperimentDetails.getTimeStarted());
		
		// maybe set MECEventLog in eed?
		
		return eed;
	}
	
	public static org.sopeco.service.persistence.entities.ExecutedExperimentDetails convertToServiceExecutedExperimentDetails(ExecutedExperimentDetails executedExperimentDetails) {
		if (executedExperimentDetails == null) {
			return null;
		}
		
		org.sopeco.service.persistence.entities.ExecutedExperimentDetails eed = new org.sopeco.service.persistence.entities.ExecutedExperimentDetails();
		eed.setAccountId(executedExperimentDetails.getAccountId());
		eed.setControllerURL(executedExperimentDetails.getControllerURL());
		eed.setId(executedExperimentDetails.getId());
		eed.setName(executedExperimentDetails.getName());
		eed.setScenarioName(executedExperimentDetails.getScenarioName());
		eed.setSuccessful(executedExperimentDetails.isSuccessful());
		eed.setTimeFinished(executedExperimentDetails.getTimeFinished());
		eed.setTimeStarted(executedExperimentDetails.getTimeStarted());
		
		// maybe fetch MECEventLog in eed?
		
		return eed;
	}

	public static org.sopeco.service.persistence.entities.MECLog convertToServiceMECLog(MECLog meclog) {
		if (meclog == null) {
			return null;
		}
		
		org.sopeco.service.persistence.entities.MECLog ml = new org.sopeco.service.persistence.entities.MECLog();
		ml.setId(meclog.getId());
		
		List<org.sopeco.service.execute.MECLogEntry> list = new ArrayList<org.sopeco.service.execute.MECLogEntry>();
		for (MECLogEntry mle : meclog.getEntries()) {
			list.add(convertToServiceMECLogEntry(mle));
		}
		ml.setEntries(list);
		
		return ml;
	}

	public static MECLog convertToMECLog(org.sopeco.service.persistence.entities.MECLog meclog) {
		if (meclog == null) {
			return null;
		}
		
		MECLog ml = new MECLog();
		ml.setId(meclog.getId());
		
		List<MECLogEntry> list = new ArrayList<MECLogEntry>();
		for (org.sopeco.service.execute.MECLogEntry mle : meclog.getEntries()) {
			list.add(convertToMECLogEntry(mle));
		}
		ml.setEntries(list);
		
		return ml;
	}
	
	public static org.sopeco.service.execute.MECLogEntry convertToServiceMECLogEntry(MECLogEntry meclogentry) {
		if (meclogentry == null) {
			return null;
		}
		
		org.sopeco.service.execute.MECLogEntry mle = new org.sopeco.service.execute.MECLogEntry();
		mle.setError(meclogentry.isError());
		mle.setErrorMessage(meclogentry.getErrorMessage());
		mle.setException(meclogentry.isException());
		mle.setMessage(meclogentry.getMessage());
		mle.setTime(meclogentry.getTime());
		
		return mle;
	}
	
	public static MECLogEntry convertToMECLogEntry(org.sopeco.service.execute.MECLogEntry meclogentry) {
		if (meclogentry == null) {
			return null;
		}
		
		MECLogEntry mle = new MECLogEntry();
		mle.setError(meclogentry.isError());
		mle.setErrorMessage(meclogentry.getErrorMessage());
		mle.setException(meclogentry.isException());
		mle.setMessage(meclogentry.getMessage());
		mle.setTime(meclogentry.getTime());
		
		return mle;
	}
	
}
