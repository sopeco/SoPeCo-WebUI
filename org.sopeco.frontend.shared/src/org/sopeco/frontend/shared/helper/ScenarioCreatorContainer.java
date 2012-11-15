package org.sopeco.frontend.shared.helper;

import java.io.Serializable;

import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

/**
 * Object with all necessary and optional attributes to create a scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioCreatorContainer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String scenarioName;
	private String specificationName;
	private ExperimentSeriesDefinition experimentDefinition;
	private String controllerProtocol;
	private String controllerHost;
	private int controllerPort;
	private String controllerName;

	/**
	 * @return the scenarioName
	 */
	public String getScenarioName() {
		return scenarioName;
	}

	/**
	 * @param pScenarioName
	 *            the scenarioName to set
	 */
	public void setScenarioName(String pScenarioName) {
		this.scenarioName = pScenarioName;
	}

	/**
	 * @return the specificationName
	 */
	public String getSpecificationName() {
		return specificationName;
	}

	/**
	 * @param pSpecificationName
	 *            the specificationName to set
	 */
	public void setSpecificationName(String pSpecificationName) {
		this.specificationName = pSpecificationName;
	}

	/**
	 * @return the experimentDefinition
	 */
	public ExperimentSeriesDefinition getExperimentDefinition() {
		return experimentDefinition;
	}

	/**
	 * @param pExperimentDefinition
	 *            the experimentDefinition to set
	 */
	public void setExperimentDefinition(ExperimentSeriesDefinition pExperimentDefinition) {
		this.experimentDefinition = pExperimentDefinition;
	}

	/**
	 * @return the controllerProtocol
	 */
	public String getControllerProtocol() {
		return controllerProtocol;
	}

	/**
	 * @param pControllerProtocol
	 *            the controllerProtocol to set
	 */
	public void setControllerProtocol(String pControllerProtocol) {
		this.controllerProtocol = pControllerProtocol;
	}

	/**
	 * @return the controllerHost
	 */
	public String getControllerHost() {
		return controllerHost;
	}

	/**
	 * @param pControllerHost
	 *            the controllerHost to set
	 */
	public void setControllerHost(String pControllerHost) {
		this.controllerHost = pControllerHost;
	}

	/**
	 * @return the controllerPort
	 */
	public int getControllerPort() {
		return controllerPort;
	}

	/**
	 * @param pControllerPort
	 *            the controllerPort to set
	 */
	public void setControllerPort(int pControllerPort) {
		this.controllerPort = pControllerPort;
	}

	/**
	 * @return the controllerName
	 */
	public String getControllerName() {
		return controllerName;
	}

	/**
	 * @param pControllerName
	 *            the controllerName to set
	 */
	public void setControllerName(String pControllerName) {
		this.controllerName = pControllerName;
	}

}
