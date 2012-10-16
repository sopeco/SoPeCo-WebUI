package org.sopeco.frontend.shared.builder;

import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

public class SimpleEntityFactory {

	private SimpleEntityFactory(){
	}
	
	public static MeasurementSpecification createMeasurementSpecification(String name){
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
	
	public static ExperimentSeriesDefinition createExperimentSeriesDefinition(String name,
			ExperimentTerminationCondition terminationCondition) {
		ExperimentSeriesDefinition esd = new ExperimentSeriesDefinition();
		esd.setName(name);
		esd.setExperimentTerminationCondition(terminationCondition);
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
}
