package org.sopeco.frontend.client.model;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.shared.builder.MeasurementSpecificationBuilder;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

/**
 * Contains all necessary methods for specification manipulation to quickly
 * access them.
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationModul {

	private static final Logger LOGGER = Logger.getLogger(ExperimentModul.class.getName());
	private ScenarioManager manager;

	private String workingSpecification;

	SpecificationModul(ScenarioManager scenarioManager) {
		manager = scenarioManager;

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, getSpecificationChangedEventHandler());
	}

	/**
	 * Handler which listens to the SpecificationChangedEvent.
	 */
	private SpecificationChangedEventHandler getSpecificationChangedEventHandler() {
		return new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				changeWorkingSpecification(event.getSelectedSpecification());
			}
		};
	}

	public MeasurementSpecification getWorkingSpecification() {
		return manager.getBuilder().getMeasurementSpecification(workingSpecification);
	}

	/**
	 * Changing the current working specification.
	 */
	private void changeWorkingSpecification(String newWorkingSpecification) {
		workingSpecification = newWorkingSpecification;

		MeasurementSpecification specification = manager.getBuilder().getMeasurementSpecification(workingSpecification);
		MeasurementSpecificationBuilder specificationBuilder = new MeasurementSpecificationBuilder(specification);
		manager.getBuilder().setSpecificationBuilder(specificationBuilder);
	}

	/**
	 * Return the name of the current selected specification.
	 * 
	 * @return
	 */
	public String getWorkingSpecificationName() {
		return workingSpecification;
	}

	/**
	 * @param newSpecification
	 *            the workingSpecification to set
	 */
	public void setWorkingSpecification(String newSpecification) {
		this.workingSpecification = newSpecification;
	}

	public boolean removeWorkingSpecification() {
		int msSize = manager.getCurrentScenarioDefinition().getMeasurementSpecifications().size();

		if (msSize <= 1) {
			return false;
		}

		manager.getCurrentScenarioDefinition().getMeasurementSpecifications().remove(getWorkingSpecification());

		manager.storeScenario();
		String name = manager.getCurrentScenarioDefinition().getMeasurementSpecifications().get(0).getName();

		changeWorkingSpecification(name);
		MainLayoutPanel.get().getNavigationController().updateSpecifications();
		EventControl.get().fireEvent(new SpecificationChangedEvent(name));

		return true;
	}
}
