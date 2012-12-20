package org.sopeco.frontend.shared.entities;

import java.io.Serializable;
import java.util.List;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendScheduledExperiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String account;

	private String controllerUrl;

	private long id;

	private boolean isRepeating;

	private String label;

	private String repeatDays;

	private String repeatHours;

	private String repeatMinutes;

	private ScenarioDefinition scenarioDefinition;

	private long startTime;

	private long addTime;

	private long nextExecutionTime;

	private long lastExecutionTime;

	private boolean enabled;

	private List<Long> durations;

	private boolean isRunning;

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean pIsRunning) {
		this.isRunning = pIsRunning;
	}

	public List<Long> getDurations() {
		return durations;
	}

	public void setDurations(List<Long> pDurations) {
		this.durations = pDurations;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean pEnabled) {
		this.enabled = pEnabled;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long pAddTime) {
		this.addTime = pAddTime;
	}

	public String getControllerUrl() {
		return controllerUrl;
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	public void setNextExecutionTime(long pNextExecutionTime) {
		this.nextExecutionTime = pNextExecutionTime;
	}

	public long getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(long pLastExecutionTime) {
		this.lastExecutionTime = pLastExecutionTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String pAccount) {
		this.account = pAccount;
	}

	public void setId(long pId) {
		this.id = pId;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getRepeatDays() {
		return repeatDays;
	}

	public String getRepeatHours() {
		return repeatHours;
	}

	public String getRepeatMinutes() {
		return repeatMinutes;
	}

	public ScenarioDefinition getScenarioDefinition() {
		return scenarioDefinition;
	}

	public long getStartTime() {
		return startTime;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setControllerUrl(String pControllerUrl) {
		controllerUrl = pControllerUrl;
	}

	public void setLabel(String pLabel) {
		label = pLabel;
	}

	public void setRepeatDays(String pRepeatDays) {
		repeatDays = pRepeatDays;
	}

	public void setRepeatHours(String pRepeatHours) {
		repeatHours = pRepeatHours;
	}

	public void setRepeating(boolean pIsRepeating) {
		isRepeating = pIsRepeating;
	}

	public void setRepeatMinutes(String pRepeatMinutes) {
		repeatMinutes = pRepeatMinutes;
	}

	public void setScenarioDefinition(ScenarioDefinition pScenarioDefinition) {
		scenarioDefinition = pScenarioDefinition;
	}

	public void setStartTime(long pStartTime) {
		startTime = pStartTime;
	}

}
