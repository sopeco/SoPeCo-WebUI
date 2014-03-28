package org.sopeco.webui.server.rpc;

import java.util.ArrayList;

import org.sopeco.service.persistence.entities.ScheduledExperiment;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;

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
		System.out.println("+++++++++" + fse.getLabel());
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
