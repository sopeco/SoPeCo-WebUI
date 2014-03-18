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

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * Builds a {@code ScenarioDefinition}. This class handles the two {@code ScenarioDefinition}
 * properties: {@code MeasurementEnvironmentDefinition} and {@code List<MeasurementSpecification>}.
 * <br />
 * Both properties are updates via so called builder classes ({@code MeasurementEnvironmentDefinitionBuilder}
 * and {@code MeasurementSpecificationBuilder}).
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class ScenarioDefinitionBuilder implements Serializable {

	private static final long serialVersionUID = -5845272212303839119L;
	
	/**
	 * The {@code ScenarioDefinition}
	 */
	private ScenarioDefinition scenarioDefinition;
	
	/**
	 * The {@code MeasurementEnvironmentDefinitionBuilder} to handle the {@code MeasurementEnvironmentDefinition}
	 * of the connected {@code ScenarioDefinition}.
	 */
	private MeasurementEnvironmentDefinitionBuilder meBuilder;
	
	/**
	 * The {@code MeasurementSpecificationBuilder} to handle an {@code MeasurementSpecification}
	 * in the connected {@code ScenarioDefinition}.
	 */
	private MeasurementSpecificationBuilder msBuilder;

	/**
	 * Creates an empty {@code ScenarioDefinition}.
	 */
	public ScenarioDefinitionBuilder() {
		scenarioDefinition = new ScenarioDefinition();
		meBuilder = new MeasurementEnvironmentDefinitionBuilder(this);
		msBuilder = new MeasurementSpecificationBuilder(this);
	}
	
	/**
	 * Creates an empty {@code ScenarioDefinition} with the given name.
	 * 
	 * @param name name of the new {@code ScenarioDefinition}
	 */
	public ScenarioDefinitionBuilder(String name) {
		scenarioDefinition = new ScenarioDefinition();
		meBuilder = new MeasurementEnvironmentDefinitionBuilder(this);
		msBuilder = new MeasurementSpecificationBuilder(this);
		
		scenarioDefinition.setScenarioName(name);
	}
	
	/**
	 * Creates a new {@code ScenarioDefinitionBuilder} with the given
	 * {@code ScenarioDefinition}. 
	 * 
	 * @param definition the {@code ScenarioDefinition}
	 */
	public ScenarioDefinitionBuilder(ScenarioDefinition definition) {
		scenarioDefinition = definition;
		meBuilder = new MeasurementEnvironmentDefinitionBuilder(this);
		msBuilder = new MeasurementSpecificationBuilder(this);
	}

	/**
	 * Sets the name of the {@code ScenarioDefinition}.
	 * 
	 * @param name new scenario name
	 */
	public void setScenarioName(String name) {
		scenarioDefinition.setScenarioName(name);
	}

	/**
	 * @return a MeasurementEnvironmentDefinitionBuilder
	 * 
	 * @Deprecated Use the method getMeasurementEnvironmentBuilder().
	 */
	@Deprecated
	public MeasurementEnvironmentDefinitionBuilder getEnvironmentBuilder() {
		return getMeasurementEnvironmentBuilder();
	}
	
	/**
	 * Returns the builder for the measurement environemnt.
	 * 
	 * @return the measurement environment builder
	 */
	public MeasurementEnvironmentDefinitionBuilder getMeasurementEnvironmentBuilder() {
		return meBuilder;
	}

	/**
	 * @param meDefinition the MeasurementEnvironmentDefinition
	 * 
	 * @deprecated Use setMeasurementEnvironmentDefinition().
	 */
	@Deprecated
	public void setMEDefinition(MeasurementEnvironmentDefinition meDefinition) {
		setMeasurementEnvironmentDefinition(meDefinition);
	}
	
	/**
	 * Sets the MeasurementEnvironmentDefiniton for the Scenario.
	 * 
	 * @param meDefinition the new MeasurementEnvironmentDefinition
	 */
	public void setMeasurementEnvironmentDefinition(MeasurementEnvironmentDefinition meDefinition) {
		scenarioDefinition.setMeasurementEnvironmentDefinition(meDefinition);
	}

	/**
	 * @return a MeasurementEnvironmentDefinition 
	 * 
	 * @Deprecated Use getMeasurementEnvironmentDefinition()
	 */
	@Deprecated
	public MeasurementEnvironmentDefinition getMEDefinition() {
		return getMeasurementEnvironmentDefinition();
	}

	/**
	 * Returns the current MeasurementEnvironmentDefinition.
	 * 
	 * @return the current MeasurementEnvironmentDefinition
	 */
	public MeasurementEnvironmentDefinition getMeasurementEnvironmentDefinition() {
		return scenarioDefinition.getMeasurementEnvironmentDefinition();
	}
	
	/**
	 * @return a MeasurementSpecificationBuilder
	 * 
	 * @Deprecated Use getNewMeasurementSpecification()
	 */
	@Deprecated
	public MeasurementSpecificationBuilder addNewMeasurementSpecification() {
		return getNewMeasurementSpecification();
	}

	/**
	 * Returns an empty fresh created MeasurementSpecification.
	 * New feature: This ScenarioDefinitionBuilder can only have
	 * one MeasurementSpecificationBuilder.
	 * 
	 * @return empty created MeasurementSpecification
	 */
	public MeasurementSpecificationBuilder getNewMeasurementSpecification() {
		MeasurementSpecificationBuilder msb = new MeasurementSpecificationBuilder(this);
		// the MSB is the new one for this ScenarioDefinition
		setMeasurementSpecificationBuilder(msb);
		return msb;
	}
	
	/**
	 * Returns the {@code MeasurementSpecification} with the given name.
	 * 
	 * @param name name of the specification
	 * @return MeasurementSpecification for the given name
	 */
	public MeasurementSpecification getMeasurementSpecification(String name) {
		for (MeasurementSpecification ms : getScenarioDefinition().getMeasurementSpecifications()) {
			if (ms.getName().equals(name)) {
				return ms;
			}
		}
		
		return null;
	}

	/**
	 * @param builder the MeasurementSpecificationBuilder
	 * 
	 * @Deprecated Use setMeasurementSpecificationBuilder().
	 */
	@Deprecated
	public void setSpecificationBuilder(MeasurementSpecificationBuilder builder) {
		setMeasurementSpecificationBuilder(builder);
	}
	
	/**
	 * Replaces the current {@Code MeasurementSpecificationBuilder} with the given
	 * new one.
	 * 
	 * @param builder the new {@Code MeasurementSpecificationBuilder}
	 */
	public void setMeasurementSpecificationBuilder(MeasurementSpecificationBuilder builder) {
		msBuilder = builder;
	}

	/**
	 * @return a MeasurementSpecificationBuilder 
	 * 
	 * @Deprecated Use getMeasurementSpecificationBuilder().
	 */
	@Deprecated
	public MeasurementSpecificationBuilder getSpecificationBuilder() {
		return getMeasurementSpecificationBuilder();
	}

	/**
	 * Returns the builder for the default {@Code MeasurementSpecification}.
	 * 
	 * @return the measurement specification builder
	 */
	public MeasurementSpecificationBuilder getMeasurementSpecificationBuilder() {
		return msBuilder;
	}
	
	/**
	 * @return a ScenarioDefinition
	 * 
	 * @Deprecated Use getScenarioDefinition().
	 */
	@Deprecated
	public ScenarioDefinition getBuiltScenario() {
		return getScenarioDefinition();
	}
	
	/**
	 * Returns the {@code ScenarioDefinition} which this ScenarioDefinitionBuilder
	 * is handling.
	 * 
	 * @return the {@code ScenarioDefinition}
	 */
	public ScenarioDefinition getScenarioDefinition() {
		return scenarioDefinition;
	}

	/**
	 * @param name the name
	 * @return a ScenarioDefinition
	 * 
	 * @Deprecated Use constructor and getScenarioDefinition().
	 */
	@Deprecated
	public static ScenarioDefinition buildEmptyScenario(String name) {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();

		builder.setScenarioName(name);

		return builder.getBuiltScenario();
	}

	/**
	 * @param definition a ScenarioDefinition
	 * @return a ScenarioDefinitionBuilder
	 * 
	 * @Deprecated Use constructor.
	 */
	@Deprecated
	public static ScenarioDefinitionBuilder load(ScenarioDefinition definition) {
		ScenarioDefinitionBuilder builder = new ScenarioDefinitionBuilder();
		builder.scenarioDefinition = definition;
		return builder;
	}
}
