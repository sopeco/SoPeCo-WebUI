package org.sopeco.frontend.client.model;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.helper.INotifyHandler.Result;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
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
	 private String currentSpecification;

	private ScenarioManager manager;

	/**
	 * Constructor.
	 * 
	 * @param scenarioManager
	 */
	SpecificationModul(ScenarioManager scenarioManager) {
		manager = scenarioManager;

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, getSpecificationChangedEventHandler());
	}

	/**
	 * Changing the current working specification.
	 */
	public void changeSpecification(String newWorkingSpecification) {
		Manager.get().getAccountDetails().setSelectedSpecification(newWorkingSpecification);
		Manager.get().storeAccountDetails();

		MeasurementSpecificationBuilder specificationBuilder = new MeasurementSpecificationBuilder(getSpecification());
		manager.getBuilder().setSpecificationBuilder(specificationBuilder);
	}

	/**
	 * Adding a new specification to the scenario and set it to the working
	 * specification.
	 * 
	 * @param name
	 */
	public void createNewSpecification(String name) {
		if (existSpecification(name)) {
			LOGGER.warning("Specification with the name '" + name + "' already exists.");
			return;
		}

		MeasurementSpecificationBuilder newBuilder = manager.getBuilder().addNewMeasurementSpecification();
		if (newBuilder == null) {
			// LOGGER.warn("Error at adding new specification '{}'", name);
			return;
		}

		newBuilder.setName(name);
		manager.storeScenario();

		MainLayoutPanel.get().getNavigationController().addSpecifications(name);
		EventControl.get().fireEvent(new SpecificationChangedEvent(name));
	}

	/**
	 * Returns whether a specification with the given name exists.
	 * 
	 * @param specification
	 *            specififcation name
	 * @return specification exists
	 */
	public boolean existSpecification(String specification) {
		for (MeasurementSpecification ms : manager.getBuilder().getBuiltScenario().getMeasurementSpecifications()) {
			if (specification.equals(ms.getName())) {
				return true;
			}
		}
		return false;
	}

	public MeasurementSpecification getSpecification() {
		return manager.getBuilder().getMeasurementSpecification(
				Manager.get().getAccountDetails().getSelectedSpecification());
	}

	/**
	 * Return the name of the current selected specification.
	 * 
	 * @return
	 */
	@Deprecated
	public String getSpecificationName() {
		 return currentSpecification;
		//return Manager.get().getAccountDetails().getSelectedSpecification();
	}

	/**
	 * Removes the current selected specification from the scenario.
	 * 
	 * @return
	 */
	public boolean removeCurrentSpecification() {
		int msSize = manager.getCurrentScenarioDefinition().getMeasurementSpecifications().size();

		if (msSize <= 1) {
			return false;
		}

		manager.getCurrentScenarioDefinition().getMeasurementSpecifications().remove(getSpecification());

		manager.storeScenario();
		String name = manager.getCurrentScenarioDefinition().getMeasurementSpecifications().get(0).getName();

		changeSpecification(name);
		MainLayoutPanel.get().getNavigationController().updateSpecifications();
		EventControl.get().fireEvent(new SpecificationChangedEvent(name));

		return true;
	}

	/**
	 * Renames the current workingSpecification to the given name.
	 */
	public void renameWorkingSpecification(String newName) {
		renameWorkingSpecification(newName, null);
	}

	/**
	 * Renames the current workingSpecification to the given name.
	 */
	public void renameWorkingSpecification(String newName, INotifyHandler<Boolean> handler) {
		manager.getBuilder().getSpecificationBuilder().setName(newName);
		MainLayoutPanel.get().getNavigationController().updateSpecifications();
		EventControl.get().fireEvent(new SpecificationChangedEvent(newName));

		manager.storeScenario();

		if (handler != null) {
			Result<Boolean> callResult = new Result<Boolean>(true, true);
			handler.call(callResult);
		}
	}

	/**
	 * @param newSpecification
	 *            the workingSpecification to set
	 */
	@Deprecated
	public void setSpecification(String newSpecification) {
		 this.currentSpecification = newSpecification;
		//Manager.get().getAccountDetails().setSelectedSpecification(newSpecification);
	}

	/**
	 * Handler which listens to the SpecificationChangedEvent.
	 */
	private SpecificationChangedEventHandler getSpecificationChangedEventHandler() {
		return new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				changeSpecification(event.getSelectedSpecification());
			}
		};
	}
}
