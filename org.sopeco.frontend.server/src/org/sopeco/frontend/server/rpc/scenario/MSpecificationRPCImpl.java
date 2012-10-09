package org.sopeco.frontend.server.rpc.scenario;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.MSpecificationRPC;
import org.sopeco.frontend.server.rpc.SuperRemoteServlet;
import org.sopeco.persistence.IPersistenceProvider;
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
		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();
		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return null;
		}
		List<String> returnList = new ArrayList<String>();
		for (MeasurementSpecification ms : getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario()
				.getMeasurementSpecifications()) {

			returnList.add(ms.getName());

		}
		returnList.add("test 1");
		returnList.add("test 2");
		return returnList;
	}

}
