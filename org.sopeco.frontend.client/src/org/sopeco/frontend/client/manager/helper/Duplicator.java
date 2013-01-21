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
package org.sopeco.frontend.client.manager.helper;

import java.util.Map;

import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Duplicator {

	private Duplicator() {
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ExperimentSeriesDefinition cloneExperiment(ExperimentSeriesDefinition source) {
		if (source == null) {
			return null;
		}
		ExperimentSeriesDefinition clone = new ExperimentSeriesDefinition();
		clone.setVersion(source.getVersion());
		clone.setName(new String(source.getName()));

		ExplorationStrategy cloneExplStgy = cloneExplorationStrategy(source.getExplorationStrategy());
		clone.setExplorationStrategy(cloneExplStgy);

		for (ExperimentTerminationCondition tc : source.getTerminationConditions()) {
			clone.addTerminationCondition(cloneTerminationCondition(tc));
		}
		for (ParameterValueAssignment pva : source.getExperimentAssignments()) {
			clone.getExperimentAssignments().add(pva.clone());
		}
		for (ConstantValueAssignment cva : source.getPreperationAssignments()) {
			clone.getPreperationAssignments().add(cva.clone());
		}
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ExplorationStrategy cloneExplorationStrategy(ExplorationStrategy source) {
		ExplorationStrategy clone = new ExplorationStrategy();
		deepCopyHashMap(source.getConfiguration(), clone.getConfiguration());
		// TODO - Copy of the list
		clone.getAnalysisConfigurations().addAll(source.getAnalysisConfigurations());
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ExperimentTerminationCondition cloneTerminationCondition(ExperimentTerminationCondition source) {
		String name = new String(source.getName());
		String description = new String(source.getDescription());
		ExperimentTerminationCondition clone = new ExperimentTerminationCondition(name, description);
		deepCopyHashMap(source.getParametersDefaultValues(), clone.getParametersDefaultValues());
		deepCopyHashMap(source.getParametersValues(), clone.getParametersValues());
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ScenarioDefinition cloneScenario(ScenarioDefinition source) {
		ScenarioDefinition clone = new ScenarioDefinition();
		clone.setScenarioName(new String(source.getScenarioName()));
		for (MeasurementSpecification ms : source.getMeasurementSpecifications()) {
			clone.getMeasurementSpecifications().add(cloneSpecification(ms));
		}
		clone.setMeasurementEnvironmentDefinition(cloneMeasurementEnvironment(source
				.getMeasurementEnvironmentDefinition()));
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static MeasurementSpecification cloneSpecification(MeasurementSpecification source) {
		MeasurementSpecification clone = new MeasurementSpecification();
		clone.setName(new String(source.getName()));
		for (ExperimentSeriesDefinition esd : source.getExperimentSeriesDefinitions()) {
			clone.getExperimentSeriesDefinitions().add(cloneExperiment(esd));
		}
		for (ConstantValueAssignment cva : source.getInitializationAssignemts()) {
			clone.getInitializationAssignemts().add(cva.clone());
		}
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static MeasurementEnvironmentDefinition cloneMeasurementEnvironment(MeasurementEnvironmentDefinition source) {
		MeasurementEnvironmentDefinition clone = new MeasurementEnvironmentDefinition();
		for (ExperimentTerminationCondition etc : source.getSupportedTerminationConditions()) {
			clone.getSupportedTerminationConditions().add(cloneTerminationCondition(etc));
		}
		clone.setRoot(cloneNamespace(source.getRoot()));
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ParameterNamespace cloneNamespace(ParameterNamespace source) {
		ParameterNamespace clone = new ParameterNamespace();
		clone.setParent(source.getParent());
		clone.setName(new String(source.getName()));
		for (ParameterDefinition pd : source.getParameters()) {
			ParameterDefinition clonePD = cloneParameterDefinition(pd);
			pd.setNamespace(clone);
			clone.getParameters().add(clonePD);
		}
		for (ParameterNamespace pns : source.getChildren()) {
			ParameterNamespace cloneNS = cloneNamespace(pns);
			cloneNS.setParent(clone);
			clone.getChildren().add(cloneNS);
		}
		return clone;
	}

	/**
	 * Returns a clone of the given object.
	 * 
	 * @param source
	 *            object that will be cloned.
	 * @return clone of the object
	 */
	public static ParameterDefinition cloneParameterDefinition(ParameterDefinition source) {
		ParameterDefinition clone = new ParameterDefinition();
		clone.setName(new String(source.getName()));
		clone.setRole(source.getRole());
		clone.setType(new String(source.getType()));
		clone.setNamespace(source.getNamespace());
		return clone;
	}

	/**
	 * Clones all values from the Map<String,String> source to the
	 * Map<String,String> target.
	 * 
	 * @param source
	 * @param target
	 */
	private static void deepCopyHashMap(Map<String, String> source, Map<String, String> target) {
		for (String key : source.keySet()) {
			String cKey = new String(key);
			String cValue = new String(source.get(key));
			target.put(cKey, cValue);
		}
	}
}
