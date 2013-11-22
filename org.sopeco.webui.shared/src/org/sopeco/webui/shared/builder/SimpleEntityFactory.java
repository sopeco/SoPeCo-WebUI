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

import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

/**
 * See {@link org.sopeco.persistence.EntityFactory}
 * 
 * @author Marius Oehler
 * 
 */
public class SimpleEntityFactory {

	private SimpleEntityFactory() {
	}

	public static MeasurementSpecification createMeasurementSpecification(String name) {
		MeasurementSpecification ms = new MeasurementSpecification();
		ms.setName(name);
		return ms;
	}

	public static ConstantValueAssignment createConstantValueAssignment(final ParameterDefinition parameter,
			String value) {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(parameter);
		cva.setValue(value);
		return cva;
	}

	public static ExperimentSeriesDefinition createExperimentSeriesDefinition(String name
	/* ,ExperimentTerminationCondition terminationCondition */) {
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		esd.setName(name);
		// esd.setExperimentTerminationCondition(terminationCondition);
		return esd;
	}

	public static ParameterNamespace createNamespace(String name) {
		ParameterNamespace child = new ParameterNamespace();
		child.setName(name);
		return child;
	}

	public static ParameterDefinition createParameterDefinition(String name, String type, ParameterRole role) {
		ParameterDefinition pd = new ParameterDefinition();
		pd.setName(name);
		pd.setRole(role);
		pd.setType(type);
		return pd;
	}

	// public static ExperimentTerminationCondition
	// createTerminationCondition(String name, Map<String, String> config) {
	// ExperimentTerminationCondition etc = new
	// ExperimentTerminationCondition();
	// etc.setName(name);
	// if (config != null) {
	// etc.getConfiguration().putAll(config);
	// }
	// return etc;
	// }

	public static ExplorationStrategy createExplorationStrategy(String name, Map<String, String> config) {
		ExplorationStrategy es = new ExplorationStrategy();
		es.setName(name);
		if (config != null) {
			es.getConfiguration().putAll(config);
		}
		return es;
	}

	public static ExplorationStrategy createExplorationStrategy(String explorationName,
			Map<String, String> explorationConfig, String analysisName, Map<String, String> analysisConfig,
			ParameterDefinition dependentParameter, List<ParameterDefinition> independentParameters) {
		ExplorationStrategy es = createExplorationStrategy(explorationName, explorationConfig);
		AnalysisConfiguration ac = new AnalysisConfiguration();
		ac.setName(analysisName);
		if (analysisConfig != null) {
			ac.getConfiguration().putAll(analysisConfig);
		}
		ac.getDependentParameters().add(dependentParameter);
		ac.getIndependentParameters().addAll(independentParameters);
		es.getAnalysisConfigurations().add(ac);
		return es;
	}
}
