package org.sopeco.frontend.server.model;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Builds a scenario.
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioDefinitionBuilder {

	private ScenarioDefinition currentBuild;
	private MeasurementEnvironmentBuilder meBuilder;
	private MeasurementSpecificationBuilder msBuilder;

	public ScenarioDefinitionBuilder() {
		currentBuild = new ScenarioDefinition();
		meBuilder = new MeasurementEnvironmentBuilder(this);
		msBuilder = new MeasurementSpecificationBuilder();
	}

	/**
	 * Set the scenario name.
	 * 
	 * @param name
	 *            New scenario name
	 */
	public void setScenarioName(String name) {
		currentBuild.setScenarioName(name);
	}

	/**
	 * Returns the builder for the measurement environemnt.
	 * 
	 * @return the measurement environment builder
	 */
	public MeasurementEnvironmentBuilder getEnvironmentBuilder() {
		return meBuilder;
	}

	public void setMEDefinition(MeasurementEnvironmentDefinition meDefinition) {
		currentBuild.setMeasurementEnvironmentDefinition(meDefinition);
	}

	public MeasurementEnvironmentDefinition getMEDefinition() {
		return currentBuild.getMeasurementEnvironmentDefinition();
	}


	/**
	 * Returns the builder for the measurement specification.
	 * 
	 * @return the measurement specification builder
	 */
	public MeasurementSpecificationBuilder getSpecificationBuilder() {
		return msBuilder;
	}

	/**
	 * Returns the created scenario.
	 * 
	 * @return Created scenario
	 */
	public ScenarioDefinition getBuiltScenario() {

		return currentBuild;
	}

	/**
	 * Creates an empty scenario.
	 * 
	 * @param name
	 *            Name of the scenario
	 * @return Build scenario
	 */
	public static ScenarioDefinition buildEmptyScenario(String name) {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();
		
		builder.setScenarioName(name);
		
		return builder.getBuiltScenario();
	}

	public static ScenarioDefinitionBuilder load(ScenarioDefinition definition) {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();

		builder.currentBuild = definition;

		return builder;
	}
}
