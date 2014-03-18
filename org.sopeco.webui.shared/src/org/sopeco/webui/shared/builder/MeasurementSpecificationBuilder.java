/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.shared.builder;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.service.configuration.ServiceConfiguration;

/**
 * Builder for a {@code MeasurementSpecification} from SoPeCo Core.
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class MeasurementSpecificationBuilder implements Serializable {

	private static final long serialVersionUID = -564030537820840352L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementSpecificationBuilder.class.getName());
	
	/**
	 * The {@code MeasurementSpecification} this builder manipulates.
	 */
	private MeasurementSpecification measurementSpecification;

	/**
	 * The {@code ScenarioDefinitionBuilder} this builder is connected to.
	 */
	private ScenarioDefinitionBuilder scenarioDefinitionBuilder;
	
	/**
	 * Creates a new MeasurementSpecificationBuilder with the given {@code ScenarioDefinitionBuilder}.
	 * 
	 * @param scenarioDefinitionBuilder the connected {@code ScenarioDefinitionBuilder}
	 */
	public MeasurementSpecificationBuilder(ScenarioDefinitionBuilder scenarioDefinitionBuilder) {
		this(scenarioDefinitionBuilder, ServiceConfiguration.DEFAULT_MEASUREMENTSPECIFICATION_NAME);
	}

	/**
	 * Creates a new MeasurementSpecificationBuilder with the given {@link ScenarioDefinitionBuilder}
	 * and the name for the {@link MeasurementSpecification}.
	 * 
	 * @param scenarioDefinitionBuilder		a ScenarioDefinitionBuilder
	 * @param measurementSpecificationName	the name of the measurementSpecificationName
	 */
	public MeasurementSpecificationBuilder(ScenarioDefinitionBuilder scenarioDefinitionBuilder,
										   String measurementSpecificationName) {
		LOGGER.debug("Creating MeasurementSpecificationBuilder with name '" + measurementSpecificationName + "'");

		this.scenarioDefinitionBuilder = scenarioDefinitionBuilder;
		measurementSpecification = SimpleEntityFactory.createMeasurementSpecification(measurementSpecificationName);
		// add the MS to the ScenarioDefinition
		this.scenarioDefinitionBuilder.getScenarioDefinition().getMeasurementSpecifications().add(measurementSpecification);
	}

	/**
	 * Creates a new MeasurementSpecificationBuilder with the given {@link MeasurementSpecification}.
	 * 
	 * @param measurementSpecification the <code>MeasurementSpecification</code>
	 */
	public MeasurementSpecificationBuilder(MeasurementSpecification measurementSpecification) {
		LOGGER.info("Creating MeasurementSpecificationBuilder for the "
					+ "MeasurementSpecification with name "
					+ "'" + measurementSpecification.getName() + "'");

		this.measurementSpecification = measurementSpecification;
		scenarioDefinitionBuilder = null;
	}

	/**
	 * Adding a parameter as initial assignment.
	 * 
	 * @param parameter	definition of the parameter
	 * @param value		value of the given parameter
	 * @return 			true, if the adding was successful
	 */
	public boolean addInitAssignment(ParameterDefinition parameter, String value) {
		ConstantValueAssignment cva = SimpleEntityFactory.createConstantValueAssignment(parameter, value);

		return addInitAssignment(cva);
	}

	/**
	 * Adding a const value assignment as initial assignment.
	 * 
	 * @param cva 	const value assignment being added
	 * @return		true, if the adding was successful
	 */
	public boolean addInitAssignment(ConstantValueAssignment cva) {
		LOGGER.info("adding parameter '" + cva.getParameter().getFullName() + "' as init assignment");

		for (ConstantValueAssignment assignment : measurementSpecification.getInitializationAssignemts()) {
			if (assignment.getParameter().getFullName().equals(cva.getParameter().getFullName())) {
				LOGGER.warn("parameter '" + cva.getParameter().getFullName() + "' already in init assignments list!");
				return false;
			}
		}

		return measurementSpecification.getInitializationAssignemts().add(cva);
	}

	/**
	 * Returns true, if an assignment with the given fullname (namespace + name) exists.
	 * 
	 * @param parameter	the {@link ParameterDefinition}
	 * @return 			true, if an assignment with the given fullname (namespace + name) exists
	 */
	public boolean containsInitialAssignment(ParameterDefinition parameter) {
		for (ConstantValueAssignment assignment : measurementSpecification.getInitializationAssignemts()) {
			if (assignment.getParameter().getFullName().equals(parameter.getFullName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the given assignment.
	 * 
	 * @param cva	ConstantValueAssignment being removed
	 * @return 		true, if the removal was successful
	 */
	public boolean removeInitialAssignment(ConstantValueAssignment cva) {
		LOGGER.info("Removing ConstantValueAssignment '" + cva.getParameter().getFullName()
				+ "' from init assignment list");

		return measurementSpecification.getInitializationAssignemts().remove(cva);
	}

	/**
	 * Removes the given assignment.
	 * 
	 * @param parameter	parameter of cva being removed
	 * @return 			true, if the removal was successful
	 */
	public boolean removeInitialAssignment(ParameterDefinition parameter) {
		LOGGER.info("Removing parameter '" + parameter.getFullName() + "' from init assignment list");

		for (ConstantValueAssignment cva : measurementSpecification.getInitializationAssignemts()) {
			if (cva.getParameter().getFullName().equals(parameter.getFullName())) {
				if (measurementSpecification.getInitializationAssignemts().remove(cva)) {

					return true;
				}
				return false;
			}
		}

		return false;
	}

	/**
	 * Adds a new experiment series definition to the specification.
	 * 
	 * @param name	experiment name
	 * @return 		if adding was successful: the created experimentSeriesDefinition
	 *         		object else null
	 */
	public ExperimentSeriesDefinition addExperimentSeries(String name) {
		ExperimentSeriesDefinition experiment = SimpleEntityFactory.createExperimentSeriesDefinition(name);

		if (addExperimentSeries(experiment)) {
			return experiment;
		} else {
			return null;
		}
	}

	/**
	 * Adds the given exp. series def. object to the specification.
	 * 
	 * @param experiment	esd object which will be added
	 * @return 				true if the adding was successful; false if a experiment with the
	 *         				given name already exists
	 */
	public boolean addExperimentSeries(ExperimentSeriesDefinition experiment) {
		LOGGER.info("adding new experiementSeriesDefinition '" + experiment.getName() + "' to specification '"
				+ measurementSpecification.getName() + "'");

		for (ExperimentSeriesDefinition expDefinition : measurementSpecification.getExperimentSeriesDefinitions()) {
			if (expDefinition.getName().equals(experiment.getName())) {
				LOGGER.warn("adding failed. there is already a experiementSeriesDefinition called '"
						+ experiment.getName() + " in specification '" + measurementSpecification.getName() + "'");

				return false;
			}
		}

		return measurementSpecification.getExperimentSeriesDefinitions().add(experiment);
	}

	/**
	 * Return the ExpSeriesDef with the given name.
	 * 
	 * @param name	name of the ExpSeries
	 * @return 		the found experimentSeries. if no expSeries with the given name
	 *         		was found, it returns null
	 */
	public ExperimentSeriesDefinition getExperimentSeries(String name) {
		for (ExperimentSeriesDefinition expDefinition : measurementSpecification.getExperimentSeriesDefinitions()) {
			if (expDefinition.getName().equals(name)) {
				LOGGER.info("experiment called '" + name + " was found in specification '" + measurementSpecification.getName()
						+ "'");

				return expDefinition;
			}
		}

		LOGGER.warn("specification '" + measurementSpecification.getName() + "' has no experiment called '" + name + "'");

		return null;
	}

	/**
	 * Removes the given experiment from the specification.
	 * 
	 * @param expDefinition	experimentSeries which will be removed
	 * @return 				true if the removal was successful
	 */
	public boolean removeExperimentSeries(ExperimentSeriesDefinition expDefinition) {
		return removeExperimentSeries(expDefinition.getName());
	}

	/**
	 * Removes the experiment, which has the given name, from the specification.
	 * 
	 * @param name	name of the experimentSeries which will be removed
	 * @return 		true if the removal was successful
	 */
	public boolean removeExperimentSeries(String name) {
		LOGGER.info("removing the experiment '" + name + "' from the specification '" + measurementSpecification.getName() + "'");

		for (ExperimentSeriesDefinition expDefinition : measurementSpecification.getExperimentSeriesDefinitions()) {
			if (expDefinition.getName().equals(name)) {
				return measurementSpecification.getExperimentSeriesDefinitions().remove(expDefinition);
			}
		}

		LOGGER.warn("can't remove exp. '" + name + "' because nothing was found in spec. '"
				+ measurementSpecification.getName() + "'");

		return false;
	}

	/**
	 * Set a new specification name.
	 * 
	 * @param name	the new spec. name
	 */
	public void setName(String name) {
		LOGGER.info("Setting new specification name: '" + measurementSpecification.getName() + "' -> '" + name + "'");

		measurementSpecification.setName(name);
	}

	/**
	 * Returns the built measurement specification.
	 * 
	 * @return the built specification
	 */
	public MeasurementSpecification getBuiltSpecification() {
		return measurementSpecification;
	}
}
