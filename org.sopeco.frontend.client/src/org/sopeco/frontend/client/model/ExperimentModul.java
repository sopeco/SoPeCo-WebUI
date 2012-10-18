package org.sopeco.frontend.client.model;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;

/**
 * Contains all necessary methods for Experiment manipulation to quickly access
 * them.
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentModul {

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

}
