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
package org.sopeco.webui.server.rpc.scenario;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.engine.model.ScenarioDefinitionWriter;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.ArchiveEntry;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.webui.client.rpc.ScenarioManagerRPC;
import org.sopeco.webui.server.rpc.SuperRemoteServlet;
import org.sopeco.webui.shared.builder.ScenarioDefinitionBuilder;

/**
 * Implementation of the ScenarioManagerRPC. The Frontend is getting all
 * scenario inforamtion from these calls.
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioManagerRPCImpl extends SuperRemoteServlet implements ScenarioManagerRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioManagerRPCImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getScenarioNames() {

		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();

		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return null;
		}

		try {
			List<ScenarioDefinition> scenarioList = dbCon.loadAllScenarioDefinitions();

			String[] retValues = new String[scenarioList.size()];

			for (int i = 0; i < scenarioList.size(); i++) {
				ScenarioDefinition sd = scenarioList.get(i);
				retValues[i] = sd.getScenarioName();
			}

			return retValues;
		} catch (DataNotFoundException e) {
			return null;
		}
	}

	@Override
	public boolean addScenario(String scenarioName, String specificationName, ExperimentSeriesDefinition experiment) {
		scenarioName = scenarioName.replaceAll("[^a-zA-Z0-9_]", "_");

		ScenarioDefinition emptyScenario = ScenarioDefinitionBuilder.buildEmptyScenario(scenarioName);

		if (specificationName != null) {
			emptyScenario.getMeasurementSpecifications().get(0).setName(specificationName);
			if (experiment != null) {
				emptyScenario.getMeasurementSpecifications().get(0).getExperimentSeriesDefinitions().add(experiment);
			}
		}

		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();

		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return false;
		}

		dbCon.store(emptyScenario);

		switchScenario(scenarioName);
		return true;
	}

	@Override
	public boolean addScenario(ScenarioDefinition scenario) {
		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();

		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return false;
		}

		dbCon.store(scenario);

		switchScenario(scenario.getScenarioName());
		return true;
	}

	@Override
	public boolean removeScenario(String name) {
		if (!name.matches("[a-zA-Z0-9_]+")) {
			return false;
		}

		IPersistenceProvider dbCon = getUser().getCurrentPersistenceProvider();

		try {
			ScenarioDefinition definition = dbCon.loadScenarioDefinition(name);

			dbCon.remove(definition);

			return true;
		} catch (DataNotFoundException e) {
			LOGGER.warn("Scenario '{}' not found.", name);
			return false;
		}
	}

	@Override
	public boolean switchScenario(String name) {
		ScenarioDefinition definition = loadScenarioDefinition(name);
		if (definition == null) {
			return false;
		}

		ScenarioDefinitionBuilder builder = ScenarioDefinitionBuilder.load(definition);
		getUser().setCurrentScenarioDefinitionBuilder(builder);

		return true;

	}

	private ScenarioDefinition loadScenarioDefinition(String sceName) {
		try {
			ScenarioDefinition definition = getUser().getCurrentPersistenceProvider().loadScenarioDefinition(sceName);

			return definition;
		} catch (DataNotFoundException e) {
			LOGGER.warn("Scenario '{}' not found.", sceName);
			return null;
		}
	}

	@Override
	public ScenarioDefinition getCurrentScenarioDefinition() {
		return getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario();
	}

	@Override
	public boolean storeScenarioDefinition(ScenarioDefinition definition) {
		ScenarioDefinition current = getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario();
		try {
			for (ScenarioInstance instance : getUser().getCurrentPersistenceProvider().loadScenarioInstances(
					current.getScenarioName())) {
				if (!definition.equals(current)) {
					String changeHandlingMode = Configuration.getSessionSingleton(getSessionId()).getPropertyAsStr(
							IConfiguration.CONF_DEFINITION_CHANGE_HANDLING_MODE);
					if (changeHandlingMode.equals(IConfiguration.DCHM_ARCHIVE)) {

						archiveOldResults(instance);
					}

					getUser().getCurrentPersistenceProvider().removeScenarioInstanceKeepResults(instance);
				}
			}
		} catch (DataNotFoundException e) {
			LOGGER.debug("No scenario instance with name {} available. Skip archiving old results!",
					current.getScenarioName());
		}

		getUser().setCurrentScenarioDefinitionBuilder(ScenarioDefinitionBuilder.load(definition));

		getUser().storeCurrentScenarioDefinition();
		return true;
	}

	@Override
	public String getScenarioAsXML() {
		ScenarioDefinition definition = getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario();

		if (definition != null) {
			ScenarioDefinitionWriter writer = new ScenarioDefinitionWriter(getSessionId());
			return writer.convertToXMLString(definition);
		}
		return "";
	}

	private void archiveOldResults(ScenarioInstance scenarioInstance) {
		ScenarioDefinitionWriter writer = new ScenarioDefinitionWriter(getSessionId());
		String scenarioDefinitionXML = writer.convertToXMLString(scenarioInstance.getScenarioDefinition());
		for (ExperimentSeries es : scenarioInstance.getExperimentSeriesList()) {
			for (ExperimentSeriesRun run : es.getExperimentSeriesRuns()) {
				ArchiveEntry entry = new ArchiveEntry(getUser().getCurrentPersistenceProvider(), run.getTimestamp(),
						scenarioInstance.getName(), scenarioInstance.getMeasurementEnvironmentUrl(), es.getName(),
						run.getLabel(), scenarioDefinitionXML, run.getDatasetId());
				getUser().getCurrentPersistenceProvider().store(entry);
			}
		}
	}
}
