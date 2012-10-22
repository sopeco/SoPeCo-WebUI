package org.sopeco.frontend.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.engine.experimentseries.ITerminationConditionExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

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
	public String getCurrentExperiment() {
		return currentExperiment;
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
//		ExtensionRegistry.getSingleton().getExtensions(ITerminationConditionExtension.class).getList();
//		ExperimentTerminationCondition condition = EntityFactory.createTerminationCondition("Number Of Repetitions", )
	
		
	}
	
	
}
