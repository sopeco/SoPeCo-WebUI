package org.sopeco.frontend.client.model;

import java.util.logging.Logger;

import org.sopeco.frontend.client.helper.SimpleNotify;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.helper.Duplicator;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.builder.ScenarioDefinitionBuilder;
import org.sopeco.frontend.shared.entities.ScenarioDetails;
import org.sopeco.frontend.shared.helper.Helper;
import org.sopeco.frontend.shared.helper.Utilities;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ScenarioManager {

	private static final Logger LOGGER = Logger.getLogger(ScenarioManager.class.getName());
	private static ScenarioManager modelManager;

	/**
	 * Creates a new scenario manager and stores the new object in a static
	 * attribute which is accessible by the static get() method.
	 */
	public static void clear() {
		modelManager = new ScenarioManager();
	}

	/**
	 * Returns a object of the ScenarioManager class.
	 * 
	 * @return
	 */
	public static ScenarioManager get() {
		if (modelManager == null) {
			modelManager = new ScenarioManager();
		}

		return modelManager;
	}

	private ScenarioDefinitionBuilder builder;
	private String currentScenarioName;
	private ExperimentModul experimentModul;

	private boolean scenarioLoaded = false;

	private SpecificationModul specificationModul;

	/**
	 * Constructor.
	 */
	private ScenarioManager() {
		builder = new ScenarioDefinitionBuilder();
	}

	/**
	 * Changes the value of the given InitAssignment Parameter.
	 * 
	 * @param path
	 * @param name
	 * @param newValue
	 * @return
	 */
	public boolean changeInitAssignmentValue(String path, String name, String newValue) {
		ParameterNamespace namespace = getBuilder().getEnvironmentBuilder().getNamespace(path, "\\.");
		ParameterDefinition parameter = getBuilder().getEnvironmentBuilder().getParameter(name, namespace);

		if (parameter == null) {
			return false;
		}

		for (ConstantValueAssignment cva : getBuilder().getSpecificationBuilder().getBuiltSpecification()
				.getInitializationAssignemts()) {
			if (cva.getParameter().getFullName().equals(parameter.getFullName())) {
				cva.setValue(newValue);
				return true;
			}
		}

		return false;
	}

	/**
	 * Clones the current/working scenario and sets the name of the new one to
	 * the given name. The new scenario will be set as the working scenario.
	 */
	public void cloneCurrentScenario(final String targetName) {
		ScenarioDefinition clone = Duplicator.cloneScenario(builder.getBuiltScenario());
		clone.setScenarioName(Utilities.cleanString(targetName));
		RPC.getScenarioManager().addScenario(clone, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				Manager.get().getAccountDetails().addScenarioDetails(Utilities.cleanString(targetName));
				Manager.get().getAccountDetails().setSelectedScenario(Utilities.cleanString(targetName));
				Manager.get().storeAccountDetails();

				MainLayoutPanel.get().getNorthPanel().updateScenarioListAndSwitch(Utilities.cleanString(targetName));
			}
		});
	}

	/**
	 * 
	 * @param scenarioName
	 * @param specificationName
	 * @param experiment
	 * @param simpleNotify
	 */
	public void createScenario(String scenarioName, String specificationName, ExperimentSeriesDefinition experiment,
			final SimpleNotify simpleNotify) {
		final String cleanedScenarioName = Utilities.cleanString(scenarioName);

		RPC.getScenarioManager().addScenario(cleanedScenarioName, specificationName, experiment,
				new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						LOGGER.severe(caught.getLocalizedMessage());
						Message.error("Failed adding new scenario.");
					}

					@Override
					public void onSuccess(Boolean result) {
						Manager.get().getAccountDetails().addScenarioDetails(cleanedScenarioName);
						Manager.get().getAccountDetails().setSelectedScenario(cleanedScenarioName);
						Manager.get().storeAccountDetails();

						if (simpleNotify != null) {
							simpleNotify.call();
						} else {
							MainLayoutPanel.get().getNorthPanel().updateScenarioList();
							switchScenario(cleanedScenarioName);
						}
					}
				});
	}

	/**
	 * Returns whether a scenario with the given name exists.
	 * 
	 * @param name
	 * @return
	 */
	public boolean existScenario(String name) {
		for (String sName : Manager.get().getAccountDetails().getScenarioNames()) {
			if (sName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the ExperimentModul, which contains all methods that were related
	 * to experiments.
	 * 
	 * @return experimentModul
	 */
	public ExperimentModul experiment() {
		if (experimentModul == null) {
			experimentModul = new ExperimentModul(this);
		}

		return experimentModul;
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
	 * Returns the scenario definition of the current scenario builder.
	 * 
	 * @return ScenarioDefinition
	 */
	public ScenarioDefinition getCurrentScenarioDefinition() {
		return builder.getBuiltScenario();
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
	 * Returns true if any scenario has been loaded and is available now.
	 * 
	 * @return
	 */
	public boolean isScenarioAvailable() {
		return scenarioLoaded;
	}

	/**
	 * Loading the scenario definition of the current selected scenario from the
	 * server and stored it at the client.
	 */
	public void loadCurrentScenarioFromServer() {
		RPC.getScenarioManager().getCurrentScenarioDefinition(new AsyncCallback<ScenarioDefinition>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(ScenarioDefinition result) {
				if (result == null) {
					LOGGER.severe("Error while loading scenario definition.");
					Message.error("Error while loading scenario definition.");
					return;
				}

				builder = ScenarioDefinitionBuilder.load(result);
				scenarioLoaded = true;

				String specification = Manager.get().getCurrentScenarioDetails().getSelectedSpecification();
				if (specification == null || !specification().existSpecification(specification)) {
					specification = builder.getBuiltScenario().getMeasurementSpecifications().get(0).getName();
				}
				MainLayoutPanel.get().createNewCenterPanels();

				specification().changeSpecification(specification);

				// EventControl.get().fireEvent(new
				// EnvironmentDefinitionChangedEvent());

				MainLayoutPanel.get().getViewSwitch().switchTo(CenterType.Specification);
			}
		});
	}

	/**
	 * 
	 */
	public void loadDefinitionFromCurrentController() {
		RPC.getMEControllerRPC().getMEDefinitionFromMEC(Manager.get().getControllerUrl(),
				new AsyncCallback<MeasurementEnvironmentDefinition>() {
					@Override
					public void onFailure(Throwable caught) {
						LOGGER.severe(caught.getLocalizedMessage());
						Message.error(caught.getMessage());
					}

					@Override
					public void onSuccess(MeasurementEnvironmentDefinition result) {
						setMeasurementDefinition(result);
					}
				});
	}

	/**
	 * Removes the scenario with the given name.
	 * 
	 * @param scenarioName
	 */
	public void removeScenario(final String scenarioName) {
		RPC.getScenarioManager().removeScenario(scenarioName, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				for (ScenarioDetails sd : Manager.get().getAccountDetails().getScenarioDetails()) {
					if (sd.getScenarioName().equals(scenarioName)) {
						Manager.get().getAccountDetails().getScenarioDetails().remove(sd);
						Manager.get().storeAccountDetails();
						break;
					}
				}
				MainLayoutPanel.get().getNorthPanel().updateScenarioList();
				if (Manager.get().getAccountDetails().getScenarioDetails().isEmpty()) {
					MainLayoutPanel.get().getViewSwitch().switchTo(CenterType.NoScenario);
				} else {
					switchScenario(Manager.get().getAccountDetails().getScenarioNames()[0]);
				}
			}
		});
	}

	/**
	 * Sets the given me-definition as the current definition and updates the
	 * EnvrionmentTrees of the SpecificationView and ExperimentView.
	 * 
	 * @param environment
	 *            new me-definition
	 */
	public void setMeasurementDefinition(MeasurementEnvironmentDefinition environment) {
		builder.getBuiltScenario().setMeasurementEnvironmentDefinition(environment);

		if (MainLayoutPanel.get().getSpecificationController().getEnvironmentTree() != null) {
			MainLayoutPanel.get().getSpecificationController().getEnvironmentTree().generateTree();
		}
		if (MainLayoutPanel.get().getExperimentController().getEnvironmentTree() != null) {
			MainLayoutPanel.get().getExperimentController().getEnvironmentTree().generateTree();
		}
	}

	/**
	 * Returns the SpecificationModul, which contains all methods that were
	 * related to specification.
	 * 
	 * @return experimentModul
	 */
	public SpecificationModul specification() {
		if (specificationModul == null) {
			specificationModul = new SpecificationModul(this);
		}

		return specificationModul;
	}

	/**
	 * Sends the current scenario to the server and stores them in the database.
	 */
	public void storeScenario() {
		Helper.whoCalledMe();

		RPC.getScenarioManager().storeScenarioDefinition(getCurrentScenarioDefinition(), new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
			}
		});
	}

	/**
	 * Switch the current scenario to the given scenario(name).
	 * 
	 * @param scenarioName
	 *            name of the new scenario
	 */
	public void switchScenario(final String scenarioName) {
		LOGGER.fine("switch scenario to: " + scenarioName);
		if (scenarioName == null) {
			return;
		}
		Manager.get().getAccountDetails().setSelectedScenario(scenarioName);
		Manager.get().storeAccountDetails();

		RPC.getScenarioManager().switchScenario(scenarioName, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				currentScenarioName = scenarioName;
				loadCurrentScenarioFromServer();

			}
		});
	}

	/**
	 * 
	 * @param path
	 * @param oldName
	 * @param newName
	 * @param type
	 * @param role
	 * @return
	 */
	public boolean updateParameter(String path, String oldName, String newName, String type, ParameterRole role) {
		LOGGER.info("rpc: updateParameter: " + oldName + " from '" + path + "'");

		ParameterNamespace ns = getBuilder().getEnvironmentBuilder().getNamespace(path);

		if (ns == null) {
			LOGGER.info("no namespace '" + ns + "' found");
			return false;
		}

		ParameterDefinition parameter = getBuilder().getEnvironmentBuilder().getParameter(oldName, ns);

		if (parameter == null) {
			LOGGER.info("no parameter '" + oldName + "' found");
			return false;
		}

		ConstantValueAssignment initialAssignmentParameter = null;
		for (ConstantValueAssignment cva : getBuilder().getSpecificationBuilder().getBuiltSpecification()
				.getInitializationAssignemts()) {
			if (cva.getParameter().getFullName().equals(parameter.getFullName())) {
				initialAssignmentParameter = cva;
			}
		}

		parameter.setName(newName);
		parameter.setType(type);
		parameter.setRole(role);

		if (initialAssignmentParameter != null) {
			initialAssignmentParameter.setParameter(parameter);
			((SpecificationController) MainLayoutPanel.get().getCenterController(CenterType.Specification))
					.addExistingAssignments();
		}

		storeScenario();

		return true;
	}
}
