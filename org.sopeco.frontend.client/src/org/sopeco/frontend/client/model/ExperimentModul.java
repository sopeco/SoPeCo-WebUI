package org.sopeco.frontend.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.experiment.ExperimentController;
import org.sopeco.frontend.shared.builder.SimpleEntityFactory;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;
import org.sopeco.persistence.entities.definition.ExplorationStrategy;

/**
 * Contains all necessary methods for Experiment manipulation to quickly access
 * them.
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentModul {

	/** Termination Types. */
	public enum TerminationCondition {
		/** Termination Types. */
		Repetitions, Timeout
	}

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
	 * Returns the current selected ExperimentSeriesDefinition.
	 */
	public ExperimentSeriesDefinition getCurrentExperiment() {
		if (currentExperiment == null) {
			return null;
		}

		for (ExperimentSeriesDefinition experiment : getExperimentsOfCurrentSpecififcation()) {
			if (experiment.getName().equals(currentExperiment)) {
				return experiment;
			}
		}

		return null;
	}

	/**
	 * @param currentExperiment
	 *            the currentExperiment to set
	 */
	public void setCurrentExperiment(String newExperiment) {
		LOGGER.info("Switch experiment to '" + newExperiment + "'");
		currentExperiment = newExperiment;
	}

	/**
	 * Stores the config of the selected experiment.
	 * 
	 * @param experimentController
	 */
	public void saveExperimentConfig(ExperimentController experimentController) {
		double metering = Metering.start();
		LOGGER.info("Save experiment configuration");

		ExperimentSeriesDefinition experiment = getCurrentExperiment();
		if (experiment == null) {
			return;
		}

		String terminationName = experimentController.getTerminationExtController().getSelectedExtensionName();
		Map<String, String> terminationConfig = experimentController.getTerminationExtController().getConfigMap();

		String explorationName = experimentController.getExplorationExtController().getSelectedExtensionName();
		Map<String, String> explorationConfig = experimentController.getExplorationExtController().getConfigMap();

		experiment.setExperimentTerminationCondition(SimpleEntityFactory.createTerminationCondition(terminationName,
				terminationConfig));
		experiment.setExplorationStrategy(SimpleEntityFactory.createExplorationStrategy(explorationName,
				explorationConfig));

		manager.storeScenario();

		Metering.stop(metering);
	}

	/**
	 * Sets the termination condition to the given values.
	 * 
	 * @param condition
	 *            type of the condition
	 * @param value
	 *            value of the condition
	 */
	public void setTerminationCondition(TerminationCondition condition, String value) {

	}

	/**
	 * Reutnrs a list with all ExperimentSeriesDefinition of the selected
	 * specification.
	 * 
	 * @return list with experiments
	 */
	public List<ExperimentSeriesDefinition> getExperimentsOfCurrentSpecififcation() {
		if (manager.specification().getWorkingSpecificationName() == null) {
			return new ArrayList<ExperimentSeriesDefinition>();
		}

		return manager.getBuilder().getMeasurementSpecification(manager.specification().getWorkingSpecificationName())
				.getExperimentSeriesDefinitions();
	}

	/**
	 * Creates a new experiment series with the given name.
	 * 
	 * @param name
	 */
	public void createExperimentSeries(String name) {
		LOGGER.info("Create experiment '" + name + "'");

		ExperimentSeriesDefinition experiment = SimpleEntityFactory.createExperimentSeriesDefinition(name,
				createDefaultTerminationCondition());

		experiment.setExplorationStrategy(createDefaultExplorationStrategy());

		manager.getBuilder().getSpecificationBuilder().addExperimentSeries(experiment);

		MainLayoutPanel.get().getNavigationController().loadExperiments();
		manager.storeScenario();
	}

	/**
	 * Creates a random (first of the map) termonationCondition.
	 * 
	 * @return
	 */
	private ExperimentTerminationCondition createDefaultTerminationCondition() {
		String key = (String) Extensions.get().getExtensions(ExtensionTypes.TERMINATIONCONDITION).keySet().toArray()[0];
		Map<String, String> configMap = Extensions.get().getExtensions(ExtensionTypes.TERMINATIONCONDITION).get(key);

		return SimpleEntityFactory.createTerminationCondition(key, configMap);
	}

	/**
	 * Creates a random (first of the map) ExplorationStrategy.
	 * 
	 * @return
	 */
	private ExplorationStrategy createDefaultExplorationStrategy() {
		String key = (String) Extensions.get().getExtensions(ExtensionTypes.EXPLORATIONSTRATEGY).keySet().toArray()[0];
		Map<String, String> configMap = Extensions.get().getExtensions(ExtensionTypes.EXPLORATIONSTRATEGY).get(key);

		return SimpleEntityFactory.createExplorationStrategy(key, configMap);
	}
}
