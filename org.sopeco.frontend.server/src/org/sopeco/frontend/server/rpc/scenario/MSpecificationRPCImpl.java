package org.sopeco.frontend.server.rpc.scenario;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.MSpecificationRPC;
import org.sopeco.frontend.server.model.MeasurementSpecificationBuilder;
import org.sopeco.frontend.server.rpc.SuperRemoteServlet;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MSpecificationRPCImpl extends SuperRemoteServlet implements
		MSpecificationRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(MSpecificationRPCImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> getAllSpecificationNames() {
		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();
		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return null;
		}
		List<String> returnList = new ArrayList<String>();
		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario().getMeasurementSpecifications()) {

			returnList.add(ms.getName());

		}

		return returnList;
	}

	@Override
	public boolean setWorkingSpecification(String specificationName) {
		LOGGER.debug("Set working specification on: " + specificationName);

		if (!existSpecification(specificationName)) {
			LOGGER.debug("Can't set working specification to '{}' because it doesn't exists. ", specificationName);
			return false;
		}

		getUser().setWorkingSpecification(specificationName);
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
		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario().getMeasurementSpecifications()) {
			if (specification.equals(ms.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean createSpecification(String name) {
		if (existSpecification(name)) {
			LOGGER.warn("Specification with the name '{}' already exists.", name);
			return false;
		}

		MeasurementSpecificationBuilder newBuilder = getUser().getCurrentScenarioDefinitionBuilder().addNewMeasurementSpecification();
		if (newBuilder == null) {
			LOGGER.warn("Error at adding new specification '{}'", name);
			return false;
		}

		newBuilder.setName(name);
		getUser().storeCurrentScenarioDefinition();
		return true;
	}

	@Override
	public boolean renameWorkingSpecification(String newName) {
		if (existSpecification(newName)) {
			LOGGER.warn("Can't rename, because specification with the name '{}' already exists.", newName);
			return false;
		}

		getUser().getCurrentScenarioDefinitionBuilder().getSpecificationBuilder().setName(newName);
		getUser().storeCurrentScenarioDefinition();
		return true;
	}
}
