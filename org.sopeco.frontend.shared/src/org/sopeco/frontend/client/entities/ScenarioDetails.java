package org.sopeco.frontend.client.entities;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
public class ScenarioDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String scenarioName = "";

	private String controllerProtocol = "rmi://";

	private String controllerHost = "localhost";

	private int controllerPort = 1099;

	private String controllerName = "";

	private String selectedSpecification = "";

	private String selectedExperiment = "";

	/**
	 * @return the selectedExperiment
	 */
	public String getSelectedExperiment() {
		return selectedExperiment;
	}

	/**
	 * @param pSelectedExperiment
	 *            the selectedExperiment to set
	 */
	public void setSelectedExperiment(String pSelectedExperiment) {
		this.selectedExperiment = pSelectedExperiment;
	}

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

	/**
	 * @return the selectedSpecification
	 */
	public String getSelectedSpecification() {
		return selectedSpecification;
	}

	/**
	 * @param pSelectedSpecification
	 *            the selectedSpecification to set
	 */
	public void setSelectedSpecification(String pSelectedSpecification) {
		this.selectedSpecification = pSelectedSpecification;
	}

}
