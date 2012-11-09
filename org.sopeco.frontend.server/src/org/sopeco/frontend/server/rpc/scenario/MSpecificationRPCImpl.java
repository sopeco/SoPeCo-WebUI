package org.sopeco.frontend.server.rpc.scenario;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.MSpecificationRPC;
import org.sopeco.frontend.server.rpc.SuperRemoteServlet;
import org.sopeco.frontend.shared.builder.MeasurementSpecificationBuilder;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MSpecificationRPCImpl extends SuperRemoteServlet implements MSpecificationRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(MSpecificationRPCImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> getAllSpecificationNames() {
		double metering = Metering.start();

		List<String> returnList = new ArrayList<String>();
		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario()
				.getMeasurementSpecifications()) {

			returnList.add(ms.getName());

		}

		Metering.stop(metering);
		return returnList;
	}

	@Override
	public List<MeasurementSpecification> getAllSpecifications() {
		double metering = Metering.start();

		List<MeasurementSpecification> returnList = new ArrayList<MeasurementSpecification>();
		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario()
				.getMeasurementSpecifications()) {
			returnList.add(ms);
		}

		Metering.stop(metering);
		return returnList;
	}

	@Override
	public boolean setWorkingSpecification(String specificationName) {
		double metering = Metering.start();

		LOGGER.debug("Set working specification on: " + specificationName);

		if (!existSpecification(specificationName)) {
			LOGGER.debug("Can't set working specification to '{}' because it doesn't exists. ", specificationName);
			return false;
		}

		getUser().setWorkingSpecification(specificationName);
		Metering.stop(metering);
		return true;
	}

	/**
	 * Returns whether a specification with the given name exists.
	 * 
	 * @param specification
	 *            specififcation name
	 * @return specification exists
	 */
	private boolean existSpecification(String specification) {
		double metering = Metering.start();

		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario()
				.getMeasurementSpecifications()) {
			if (specification.equals(ms.getName())) {
				return true;
			}
		}
		Metering.stop(metering);
		return false;
	}

	@Override
	public boolean createSpecification(String name) {
		double metering = Metering.start();

		if (existSpecification(name)) {
			LOGGER.warn("Specification with the name '{}' already exists.", name);
			return false;
		}

		MeasurementSpecificationBuilder newBuilder = getUser().getCurrentScenarioDefinitionBuilder()
				.addNewMeasurementSpecification();
		if (newBuilder == null) {
			LOGGER.warn("Error at adding new specification '{}'", name);
			return false;
		}

		newBuilder.setName(name);
		getUser().storeCurrentScenarioDefinition();

		Metering.stop(metering);
		return true;
	}

	@Override
	public boolean renameWorkingSpecification(String newName) {
		double metering = Metering.start();

		if (existSpecification(newName)) {
			LOGGER.warn("Can't rename, because specification with the name '{}' already exists.", newName);
			return false;
		}

		getUser().getCurrentScenarioDefinitionBuilder().getSpecificationBuilder().setName(newName);
		getUser().storeCurrentScenarioDefinition();

		Metering.stop(metering);
		return true;
	}
}
