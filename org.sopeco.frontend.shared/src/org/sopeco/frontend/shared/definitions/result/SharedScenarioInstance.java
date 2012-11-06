package org.sopeco.frontend.shared.definitions.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SharedScenarioInstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String scenarioName, controllerUrl;
	private List<SharedExperimentSeries> experimentSeries;

	public SharedScenarioInstance() {
		experimentSeries = new ArrayList<SharedExperimentSeries>();
	}

	public void addExperimentSeries(SharedExperimentSeries series) {
		experimentSeries.add(series);
		series.setParentInstance(this);
	}

	/**
	 * @return the controllerUrl
	 */
	public String getControllerUrl() {
		return controllerUrl;
	}

	/**
	 * @return the experimentSeries
	 */
	public List<SharedExperimentSeries> getExperimentSeries() {
		return experimentSeries;
	}

	/**
	 * @return the scenarioName
	 */
	public String getScenarioName() {
		return scenarioName;
	}

	/**
	 * @param controllerUrl
	 *            the controllerUrl to set
	 */
	public void setControllerUrl(String controllerUrl) {
		this.controllerUrl = controllerUrl;
	}

	/**
	 * @param scenarioName
	 *            the scenarioName to set
	 */
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
}
