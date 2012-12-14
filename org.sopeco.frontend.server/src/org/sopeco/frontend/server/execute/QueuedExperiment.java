package org.sopeco.frontend.server.execute;

import org.sopeco.config.IConfiguration;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class QueuedExperiment {

	private ScenarioDefinition scenarioDefinition;
	private long timeStarted;
	private long timeQueued;
	private IConfiguration configuration;
	private String controllerUrl;

	public String getControllerUrl() {
		return controllerUrl;
	}

	public void setControllerUrl(String pControllerUrl) {
		this.controllerUrl = pControllerUrl;
	}

	public long getTimeQueued() {
		return timeQueued;
	}

	public void setTimeQueued(long pTimeQueued) {
		this.timeQueued = pTimeQueued;
	}

	public ScenarioDefinition getScenarioDefinition() {
		return scenarioDefinition;
	}

	public void setScenarioDefinition(ScenarioDefinition pScenarioDefinition) {
		this.scenarioDefinition = pScenarioDefinition;
	}

	public long getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(long pTimeStarted) {
		this.timeStarted = pTimeStarted;
	}

	public IConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(IConfiguration iConfiguration) {
		this.configuration = iConfiguration;
	}

}
