package org.sopeco.frontend.client.model;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioChangedEvent;
import org.sopeco.frontend.client.event.ScenarioLoadedEvent;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.ScenarioChangedEventHandler;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.builder.MeasurementSpecificationBuilder;
import org.sopeco.frontend.shared.builder.ScenarioDefinitionBuilder;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ScenarioManager {

	private static ScenarioManager modelManager;

	private ScenarioDefinitionBuilder builder;
	private String currentScenarioName;
	private String workingSpecification;

	private ScenarioManager() {
		builder = new ScenarioDefinitionBuilder();

		EventControl.get().addHandler(ScenarioChangedEvent.TYPE, new ScenarioChangedEventHandler() {
			@Override
			public void onScenarioChanged(ScenarioChangedEvent scenarioChangedEvent) {
				switchScenario(scenarioChangedEvent.getScenarioName());
			}
		});

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				changeWorkingSpecification(event.getSelectedSpecification());
			}
		});
	}

	/**
	 * Changing the current working specification.
	 */
	private void changeWorkingSpecification(String newWorkingSpecification) {
		workingSpecification = newWorkingSpecification;

		MeasurementSpecification specification = builder.getMeasurementSpecification(workingSpecification);
		MeasurementSpecificationBuilder specificationBuilder = new MeasurementSpecificationBuilder(specification);
		builder.setSpecificationBuilder(specificationBuilder);
	}

	/**
	 * Returns an object (Singelton) of the ModelManager Class.
	 * 
	 * @return
	 */
	public static ScenarioManager get() {
		if (modelManager == null) {
			modelManager = new ScenarioManager();
		}

		return modelManager;
	}

	/**
	 * Removes the ModelManager object.
	 */
	public static void clear() {
		modelManager = new ScenarioManager();
	}

	/**
	 * Retuns the name of the current scnenario.
	 * 
	 * @return
	 */
	public String getCurrentScenarioName() {
		return currentScenarioName;
	}

	/**
	 * Switch the current scenario to the given scenario(name).
	 * 
	 * @param scenarioName
	 *            name of the new scenario
	 */
	private void switchScenario(final String scenarioName) {
		RPC.getScenarioManager().switchScenario(scenarioName, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				currentScenarioName = scenarioName;
				loadCurrentScenarioFromServer();
			}
		});
	}

	// /**
	// * Loading all necessary data of the current selected scenario and update
	// * all related (ui)elements.
	// */
	// private void loadingCurrentScenario() {
	//
	// MainLayoutPanel.get().createNewCenterPanels();
	//
	// loadCurrentScenarioFromServer();
	//
	// ((SpecificationController)
	// MainLayoutPanel.get().getCenterController(CenterType.Specification))
	// .loadSpecificationNames();
	// }

	/**
	 * Returns the scenario definition of the current scenario builder.
	 * 
	 * @return ScenarioDefinition
	 */
	public ScenarioDefinition getCurrentScenarioDefinition() {
		return builder.getBuiltScenario();
	}

	/**
	 * Returns the current scenario builder.
	 * 
	 * @return ScenarioDefinitionBuilder
	 */
	public ScenarioDefinitionBuilder getBuilder() {
		return builder;
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
	 * Renames the current workingSpecification to the given name.
	 */
	public void renameWorkingSpecification(String newName) {
		getBuilder().getSpecificationBuilder().setName(newName);
		MainLayoutPanel.get().getNavigationController().updateSpecifications();
		EventControl.get().fireEvent(new SpecificationChangedEvent(newName));

		storeScenario();
	}

	/**
	 * Loading the scenario definition of the current selected scenario from the
	 * server and stored it at the client.
	 */
	private void loadCurrentScenarioFromServer() {
		RPC.getScenarioManager().getCurrentScenarioDefinition(new AsyncCallback<ScenarioDefinition>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(ScenarioDefinition result) {
				if (result == null) {
					Message.error("Error while loading scenario definition.");
					return;
				}

				builder = ScenarioDefinitionBuilder.load(result);
				workingSpecification = builder.getBuiltScenario().getMeasurementSpecifications().get(0).getName();

				EventControl.get().fireEvent(new ScenarioLoadedEvent());
				EventControl.get().fireEvent(new SpecificationChangedEvent(workingSpecification));

			}
		});
	}

	/**
	 * Sends the current scenario to the server and stores them in the database.
	 */
	public void storeScenario() {
		RPC.getScenarioManager().storeScenarioDefinition(getCurrentScenarioDefinition(), new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
			}
		});
	}

	
	
	/**
	 * Adding a new specification to the scenario and set it to the working
	 * specification.
	 * 
	 * @param name
	 */
	public void createNewSpecification(String name) {
		if (existSpecification(name)) {
			// LOGGER.warn("Specification with the name '{}' already exists.",
			// name);
			return;
		}

		MeasurementSpecificationBuilder newBuilder = getBuilder().addNewMeasurementSpecification();
		if (newBuilder == null) {
			// LOGGER.warn("Error at adding new specification '{}'", name);
			return;
		}

		newBuilder.setName(name);
		storeScenario();

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
	private boolean existSpecification(String specification) {
		for (MeasurementSpecification ms : getBuilder().getBuiltScenario().getMeasurementSpecifications()) {
			if (specification.equals(ms.getName())) {
				return true;
			}
		}
		return false;
	}
}
