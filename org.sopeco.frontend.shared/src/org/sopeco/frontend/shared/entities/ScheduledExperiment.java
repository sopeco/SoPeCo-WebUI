package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduledExperiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean active;
	private long addedTime;
	private String controllerUrl;
	private boolean isRepeating;
	private String label;
	private long lastExecutionTime;
	private long nextExecutionTime;
	private String repeatDays;
	private String repeatHours;
	private String repeatMinutes;

	private ScenarioDefinition scenarioDefinition;
	private long startTime;

	public long getAddedTime() {
		return addedTime;
	}

	public String getControllerUrl() {
		return controllerUrl;
	}

	public String getLabel() {
		return label;
	}

	public long getLastExecutionTime() {
		return lastExecutionTime;
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
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

	public boolean isActive() {
		return active;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setActive(boolean pActive) {
		active = pActive;
	}

	public void setAddedTime(long pAddedTime) {
		addedTime = pAddedTime;
	}

	public void setControllerUrl(String pControllerUrl) {
		controllerUrl = pControllerUrl;
	}

	public void setLabel(String pLabel) {
		label = pLabel;
	}

	public void setLastExecutionTime(long pLastExecutionTime) {
		lastExecutionTime = pLastExecutionTime;
	}

	public void setNextExecutionTime(long pNextExecutionTime) {
		nextExecutionTime = pNextExecutionTime;
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
