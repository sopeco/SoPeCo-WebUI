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

	private String label;
	private long startTime;
	private long nextExecutionTime;
	private long lastExecutionTime;
	private String repeatDays;
	private String repeatHours;
	private String repeatMinutes;
	private boolean isRepeating;
	private long addedTime;
	private boolean active;

	private String controllerUrl;
	private ScenarioDefinition scenarioDefinition;

	public long getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(long pLastExecutionTime) {
		this.lastExecutionTime = pLastExecutionTime;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String pLabel) {
		this.label = pLabel;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long pStartTime) {
		this.startTime = pStartTime;
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	public void setNextExecutionTime(long pNextExecutionTime) {
		this.nextExecutionTime = pNextExecutionTime;
	}

	public String getRepeatDays() {
		return repeatDays;
	}

	public void setRepeatDays(String pRepeatDays) {
		this.repeatDays = pRepeatDays;
	}

	public String getRepeatHours() {
		return repeatHours;
	}

	public void setRepeatHours(String pRepeatHours) {
		this.repeatHours = pRepeatHours;
	}

	public String getRepeatMinutes() {
		return repeatMinutes;
	}

	public void setRepeatMinutes(String pRepeatMinutes) {
		this.repeatMinutes = pRepeatMinutes;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setRepeating(boolean pIsRepeating) {
		this.isRepeating = pIsRepeating;
	}

	public long getAddedTime() {
		return addedTime;
	}

	public void setAddedTime(long pAddedTime) {
		this.addedTime = pAddedTime;
	}

	public String getControllerUrl() {
		return controllerUrl;
	}

	public void setControllerUrl(String pControllerUrl) {
		this.controllerUrl = pControllerUrl;
	}

	public ScenarioDefinition getScenarioDefinition() {
		return scenarioDefinition;
	}

	public void setScenarioDefinition(ScenarioDefinition pScenarioDefinition) {
		this.scenarioDefinition = pScenarioDefinition;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
