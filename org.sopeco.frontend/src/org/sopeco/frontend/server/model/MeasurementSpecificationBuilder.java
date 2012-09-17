package org.sopeco.frontend.server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.EntityFactory;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * Builder for the measurement specification.
 * 
 * @author Marius Oehler
 * 
 */
public class MeasurementSpecificationBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementSpecificationBuilder.class);
	private MeasurementSpecification specification;

	public MeasurementSpecificationBuilder() {
		this("");
	}

	public MeasurementSpecificationBuilder(String specName) {
		LOGGER.debug("Creating MeasurementSpecificationBuilder '" + specName + "'");

		specification = EntityFactory.createMeasurementSpecification(specName);
	}

	/**
	 * Adding a parameter as initial assignment.
	 * 
	 * @param parameter
	 *            definition of the parameter
	 * @param value
	 *            value of the given parameter
	 * @return was the adding successful
	 */
	public boolean addInitAssignment(ParameterDefinition parameter, String value) {
		ConstantValueAssignment cva = EntityFactory.createConstantValueAssignment(parameter, value);

		return addInitAssignment(cva);
	}

	/**
	 * Adding a const value assignment as initial assignment.
	 * 
	 * @param cva
	 *            const value assignment being added
	 * @return was the adding successful
	 */
	public boolean addInitAssignment(ConstantValueAssignment cva) {
		LOGGER.debug("adding parameter '" + cva.getParameter().getFullName() + "' as init assignment");

		for (ConstantValueAssignment assignment : specification.getInitializationAssignemts()) {
			if (assignment.getParameter().getFullName().equals(cva.getParameter().getFullName())) {
				LOGGER.warn("parameter '" + cva.getParameter().getFullName() + "' already in init assignments list!");
				return false;
			}
		}

		return specification.getInitializationAssignemts().add(cva);
	}

	/**
	 * Removes the given assignment.
	 * 
	 * @param cva
	 *            ConstantValueAssignment being removed
	 * @return was the removal successful
	 */
	public boolean removeInitialAssignment(ConstantValueAssignment cva) {
		LOGGER.debug("Removing ConstantValueAssignment '" + cva.getParameter().getFullName()
				+ "' from init assignment list");

		return specification.getInitializationAssignemts().remove(cva);
	}

	/**
	 * Removes the given assignment.
	 * 
	 * @param parameter
	 *            parameter of cva being removed
	 * @return was the removal successful
	 */
	public boolean removeInitialAssignment(ParameterDefinition parameter) {
		LOGGER.debug("Removing parameter '" + parameter.getFullName() + "' from init assignment list");

		for (ConstantValueAssignment cva : specification.getInitializationAssignemts()) {
			if (cva.getParameter() == parameter) {
				return specification.getInitializationAssignemts().remove(cva);
			}
		}

		return false;
	}

	/**
	 * Set a new specification name.
	 * 
	 * @param name
	 *            the new spec. name
	 */
	public void setName(String name) {
		LOGGER.debug("Setting new specification name: '" + specification.getName() + "' -> '" + name + "'");

		specification.setName(name);
	}

	/**
	 * Returns the built measurement specification.
	 * 
	 * @return the built specification
	 */
	public MeasurementSpecification getBuiltSpecification() {
		return specification;
	}
}
