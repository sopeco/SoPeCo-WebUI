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
