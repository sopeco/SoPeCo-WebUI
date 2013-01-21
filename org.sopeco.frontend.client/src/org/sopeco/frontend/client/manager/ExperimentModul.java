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
package org.sopeco.frontend.client.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.PreperationAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.experiment.ExperimentController;
import org.sopeco.frontend.client.manager.helper.Duplicator;
import org.sopeco.frontend.shared.builder.SimpleEntityFactory;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * Contains all necessary methods for Experiment manipulation to quickly access
 * them.
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentModul {

	private static final String DEFAULT_EXPLORATION = "Full Exploration Strategy";

	private static final Logger LOGGER = Logger.getLogger(ExperimentModul.class.getName());
	private ScenarioManager manager;

	private String currentExperiment;

	ExperimentModul(ScenarioManager scenarioManager) {
		manager = scenarioManager;

		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, getExperimentChangedEventHandler());
	}

	/**
	 * Handler which listens to the ExperimentChangedEvent.
	 */
	private ExperimentChangedEventHandler getExperimentChangedEventHandler() {
		return new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				setCurrentExperiment(event.getExperimentName());
			}
		};
	}

	/**
	 * @return the currentExperiment
	 */
	public String getCurrentExperimentName() {
		return currentExperiment;
	}

	/**
	 * Removes the current selected experiment series from the specification
	 * definition.
	 */
	public void removeCurrentExperimentSeries() {
		MeasurementSpecification ms = manager.getBuilder().getMeasurementSpecification(
				Manager.get().getCurrentScenarioDetails().getSelectedSpecification());

		ms.getExperimentSeriesDefinitions().remove(getCurrentExperiment());

		manager.storeScenario();

		MainLayoutPanel.get().getNavigationController().loadExperiments();
	}

	/**
	 * Returns the current selected ExperimentSeriesDefinition.
	 */
	public ExperimentSeriesDefinition getCurrentExperiment() {
		if (currentExperiment == null) {
			return null;
		}

		ExperimentSeriesDefinition foundExperiment = null;
		for (ExperimentSeriesDefinition experiment : getExperimentsOfCurrentSpecififcation()) {
			if (experiment.getName().equals(currentExperiment)) {
				if (foundExperiment == null || foundExperiment.getVersion() < experiment.getVersion()) {
					foundExperiment = experiment;
				}
			}
		}

		return foundExperiment;
	}

	/**
	 * @param currentExperiment
	 *            the currentExperiment to set
	 */
	public void setCurrentExperiment(String newExperiment) {
		LOGGER.fine("Switch experiment to '" + newExperiment + "'");
		currentExperiment = newExperiment;
		Manager.get().setSelectedExperiment(newExperiment);
	}

	/**
	 * Stores the config of the selected experiment.
	 * 
	 * @param experimentController
	 */
	public void saveExperimentConfig(ExperimentController experimentController) {
		double metering = Metering.start();
		LOGGER.fine("Save experiment configuration");

		ExperimentSeriesDefinition experiment = getCurrentExperiment();
		if (experiment == null) {
			return;
		}

		// String terminationName =
		// experimentController.getTerminationExtController().getSelectedExtensionName();
		// Map<String, String> terminationConfig =
		// experimentController.getTerminationExtController().getConfigMap();

		String explorationName = experimentController.getExplorationExtController().getCurrentExtensionName();
		Map<String, String> explorationConfig = experimentController.getExplorationExtController().getConfigMap();

		// experiment.setExperimentTerminationCondition(SimpleEntityFactory.createTerminationCondition(terminationName,
		// terminationConfig));
		experiment.setExplorationStrategy(SimpleEntityFactory.createExplorationStrategy(explorationName,
				explorationConfig));

		manager.storeScenario();

		Metering.stop(metering);
	}

	/**
	 * Reutnrs a list with all ExperimentSeriesDefinition of the selected
	 * specification.
	 * 
	 * @return list with experiments
	 */
	public List<ExperimentSeriesDefinition> getExperimentsOfCurrentSpecififcation() {
		if (!manager.isScenarioAvailable()
				|| Manager.get().getCurrentScenarioDetails().getSelectedSpecification() == null) {
			return new ArrayList<ExperimentSeriesDefinition>();
		}

		return manager.getBuilder()
				.getMeasurementSpecification(Manager.get().getCurrentScenarioDetails().getSelectedSpecification())
				.getExperimentSeriesDefinitions();
	}

	/**
	 * Creates a new experiment series with the given name.
	 * 
	 * @param name
	 */
	public void createExperimentSeries(String name) {
		LOGGER.fine("Create experiment '" + name + "'");

		ExperimentSeriesDefinition experiment = getNewExperimentSeries(name);

		manager.getBuilder().getSpecificationBuilder().addExperimentSeries(experiment);

		MainLayoutPanel.get().getNavigationController().loadExperiments();
		MainLayoutPanel.get().getViewSwitch().switchToExperiment(name);

		manager.storeScenario();
	}

	/**
	 * Creates a new experiment series with the given name.
	 * 
	 * @param name
	 */
	public ExperimentSeriesDefinition getNewExperimentSeries(String name) {
		LOGGER.fine("Create (and return) experiment '" + name + "'");

		ExperimentSeriesDefinition experiment = SimpleEntityFactory.createExperimentSeriesDefinition(name);

		experiment.setExplorationStrategy(createDefaultExplorationStrategy());

		return experiment;
	}

	/**
	 * Creates a random (first of the map) ExplorationStrategy.
	 * 
	 * @return
	 */
	private ExplorationStrategy createDefaultExplorationStrategy() {
		String key;
		if (Extensions.get().getExtensions(ExtensionTypes.EXPLORATIONSTRATEGY).containsKey(DEFAULT_EXPLORATION)) {
			key = DEFAULT_EXPLORATION;
		} else {
			key = (String) Extensions.get().getExtensions(ExtensionTypes.EXPLORATIONSTRATEGY).keySet().toArray()[0];
		}

		Map<String, String> configMap = Extensions.get().getExtensions(ExtensionTypes.EXPLORATIONSTRATEGY).get(key);

		return SimpleEntityFactory.createExplorationStrategy(key, configMap);
	}

	/**
	 * Adds a parameterdefinition as a preperation assignment.
	 */
	public void addPreperationAssignment(ParameterDefinition definition) {
		if (isPreperationAssignment(definition)) {
			return;
		}
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(definition);
		cva.setValue("");

		getCurrentExperiment().getPreperationAssignments().add(cva);

		manager.storeScenario();

		EventControl.get().fireEvent(new PreperationAssignmentsChangedEvent());
	}

	/**
	 * Sets the value of the assignment with the given parameter.
	 * 
	 * @param definition
	 * @param value
	 */
	public void setPreperationAssignmentValue(ParameterDefinition definition, String value) {
		if (getCurrentExperiment() == null) {
			return;
		}

		for (ConstantValueAssignment cva : getCurrentExperiment().getPreperationAssignments()) {
			if (cva.getParameter().getFullName().equals(definition.getFullName())) {
				cva.setValue(value);

				manager.storeScenario();
				return;
			}
		}
	}

	/**
	 * Removes the given Definiton of the current experiment.
	 * 
	 * @param definition
	 */
	public void removePreperationAssignment(ParameterDefinition definition) {
		if (getCurrentExperiment() == null) {
			return;
		}

		for (ConstantValueAssignment cva : getCurrentExperiment().getPreperationAssignments()) {
			if (cva.getParameter().getFullName().equals(definition.getFullName())) {
				getCurrentExperiment().getPreperationAssignments().remove(cva);
				break;
			}
		}

		manager.storeScenario();

		EventControl.get().fireEvent(new PreperationAssignmentsChangedEvent());
	}

	/**
	 * Checks if the given paramaeter is already a preperation assignment.
	 * 
	 * @param definition
	 * @return
	 */
	public boolean isPreperationAssignment(ParameterDefinition definition) {
		if (getCurrentExperiment() == null) {
			return false;
		}

		for (ConstantValueAssignment cva : getCurrentExperiment().getPreperationAssignments()) {
			if (cva.getParameter().getFullName().equals(definition.getFullName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a parameterdefinition as a preperation assignment.
	 */
	public void addExperimentAssignment(ParameterDefinition definition) {
		// if (isPreperationAssignment(definition)) {
		// return;
		// }
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(definition);
		cva.setValue("");

		getCurrentExperiment().getExperimentAssignments().add(cva);

		manager.storeScenario();

		EventControl.get().fireEvent(new ExperimentAssignmentsChangedEvent());
	}

	/**
	 * Removes the given Definiton of the current experiment.
	 * 
	 * @param definition
	 */
	public void removeExperimentAssignment(ParameterDefinition definition) {
		if (getCurrentExperiment() == null) {
			return;
		}

		for (ParameterValueAssignment pva : getCurrentExperiment().getExperimentAssignments()) {
			if (pva.getParameter().getFullName().equals(definition.getFullName())) {
				getCurrentExperiment().getExperimentAssignments().remove(pva);
				break;
			}
		}

		manager.storeScenario();

		EventControl.get().fireEvent(new ExperimentAssignmentsChangedEvent());
	}

	/**
	 * Checks if the given paramaeter is already a preperation assignment.
	 * 
	 * @param definition
	 * @return
	 */
	public boolean isExperimentAssignment(ParameterDefinition definition) {
		if (getCurrentExperiment() == null) {
			return false;
		}

		for (ParameterValueAssignment pva : getCurrentExperiment().getExperimentAssignments()) {
			if (pva.getParameter().getFullName().equals(definition.getFullName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	public void setExperimentAssignment(ParameterValueAssignment pva) {
		if (isExperimentAssignment(pva.getParameter())) {
			for (ParameterValueAssignment va : getCurrentExperiment().getExperimentAssignments()) {
				if (va.getParameter().getFullName().equals(pva.getParameter().getFullName())) {
					getCurrentExperiment().getExperimentAssignments().remove(va);
					break;
				}
			}

		}

		getCurrentExperiment().getExperimentAssignments().add(pva);

		manager.storeScenario();
	}

	/**
	 * 
	 * @param termination
	 */
	public void addTermination(ExperimentTerminationCondition termination) {
		if (isSetTermination(termination)) {
			LOGGER.fine("is already as t-condition set => updating config.");
			getTerminationCondition(termination).getParametersValues().clear();
			getTerminationCondition(termination).getParametersValues().putAll(termination.getParametersValues());
		} else {
			getCurrentExperiment().addTerminationCondition(termination);
		}
		manager.storeScenario();
	}

	/**
	 * 
	 * @param termination
	 */
	public boolean isSetTermination(ExperimentTerminationCondition termination) {
		for (ExperimentTerminationCondition condition : getCurrentExperiment().getTerminationConditions()) {
			if (condition.getName().toLowerCase().equals(termination.getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param termination
	 * @return
	 */
	public ExperimentTerminationCondition getTerminationCondition(ExperimentTerminationCondition termination) {
		for (ExperimentTerminationCondition condition : getCurrentExperiment().getTerminationConditions()) {
			if (condition.getName().toLowerCase().equals(termination.getName().toLowerCase())) {
				return condition;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param termination
	 */
	public void removeTermination(ExperimentTerminationCondition termination) {
		ExperimentTerminationCondition search = null;
		for (ExperimentTerminationCondition condition : getCurrentExperiment().getTerminationConditions()) {
			if (condition.getName().toLowerCase().equals(termination.getName().toLowerCase())) {
				search = condition;
				break;
			}
		}

		if (search != null) {
			getCurrentExperiment().getTerminationConditions().remove(search);
		} else {
			LOGGER.fine("is not set as t-condition set.");
		}

		manager.storeScenario();
	}

	/**
	 * 
	 * @param newName
	 */
	public void renameCurrentExpSeries(String newName) {
		getCurrentExperiment().setName(newName);
		setCurrentExperiment(newName);

		MainLayoutPanel.get().getNavigationController().loadExperiments();

		EventControl.get().fireEvent(new ExperimentChangedEvent(newName));

		manager.storeScenario();
	}

	public void cloneCurrentExperiment(String targetName) {
		ExperimentSeriesDefinition source = ScenarioManager.get().experiment().getCurrentExperiment();

		ExperimentSeriesDefinition clone = Duplicator.cloneExperiment(source);
		clone.setName(targetName);

		ScenarioManager.get().specification().getSpecification().getExperimentSeriesDefinitions().add(clone);

		ScenarioManager.get().storeScenario();

		MainLayoutPanel.get().getNavigationController().loadExperiments();
		MainLayoutPanel.get().getViewSwitch().switchToExperiment(targetName);
	}
}
