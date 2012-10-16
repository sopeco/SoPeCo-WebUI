package org.sopeco.frontend.shared.builder;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
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
		msBuilder = new MeasurementSpecificationBuilder(this);
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

	/**
	 * Sets the current MeasurementEnvironmentDefiniton of the builder.
	 * 
	 * @param meDefinition
	 */
	public void setMEDefinition(MeasurementEnvironmentDefinition meDefinition) {
		currentBuild.setMeasurementEnvironmentDefinition(meDefinition);
	}

	/**
	 * Returns the current MeasurementEnvironmentDefinition of the builder.
	 * 
	 * @return MeasurementEnvironmentDefinition
	 */
	public MeasurementEnvironmentDefinition getMEDefinition() {
		return currentBuild.getMeasurementEnvironmentDefinition();
	}

	/**
	 * Adds a new MeasurementSpecification to the current scenario.
	 * 
	 * @return
	 */
	public MeasurementSpecificationBuilder addNewMeasurementSpecification() {
		return new MeasurementSpecificationBuilder(this);
	}

	/**
	 * Returns the specification with the given name.
	 * 
	 * @param name
	 *            name of the specification
	 * @return MeasurementSpecification
	 */
	public MeasurementSpecification getMeasurementSpecification(String name) {
		for (MeasurementSpecification ms : getBuiltScenario().getMeasurementSpecifications()) {
			if (ms.getName().equals(name)) {
				return ms;
			}
		}
		return null;
	}

	/**
	 * Replacing the current builder with the given builder.
	 * 
	 * @param builder
	 */
	public void setSpecificationBuilder(MeasurementSpecificationBuilder builder) {
		msBuilder = builder;
	}

	/**
	 * Returns the builder for the default measurement specification.
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
