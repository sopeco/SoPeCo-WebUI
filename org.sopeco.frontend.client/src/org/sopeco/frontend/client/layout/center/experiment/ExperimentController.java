package org.sopeco.frontend.client.layout.center.experiment;

import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.builder.SimpleEntityFactory;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());

	private ExperimentView view;
	private ExperimentExtensionController /*terminationExtController,*/ explorationExtController;
	private PreperationAssignmentController preperationAssignmentController;
	private ExperimentAssignmentController experimentAssignmentController;
	
	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

//		terminationExtController = new ExperimentExtensionController(this, ExperimentView.EXP_SETTINGS_PANEL_WIDTH);
		explorationExtController = new ExperimentExtensionController(this, ExperimentView.EXP_SETTINGS_PANEL_WIDTH);
		preperationAssignmentController = new PreperationAssignmentController();
		experimentAssignmentController = new ExperimentAssignmentController();
		
		view = new ExperimentView();

		view.getSettingsView().addExtensionView(explorationExtController.getView());
//		view.getSettingsView().addExtensionView(terminationExtController.getView());

		view.getParameterView().add(preperationAssignmentController.getView());
		view.getParameterView().add(experimentAssignmentController.getView());
		
		explorationExtController.setHeadline(R.get("explStrategy"));
//		terminationExtController.setHeadline(R.get("termination"));

		explorationExtController.setExtensionType(ExtensionTypes.EXPLORATIONSTRATEGY);
//		terminationExtController.setExtensionType(ExtensionTypes.TERMINATIONCONDITION);

		registerEventHandlers();

		
		ParameterDefinition definition = SimpleEntityFactory.createParameterDefinition("name", "type",
				ParameterRole.INPUT);
		
		ParameterDefinition definition2 = SimpleEntityFactory.createParameterDefinition("name23233", "type",
				ParameterRole.INPUT);
		
		ParameterDefinition definition3 = SimpleEntityFactory.createParameterDefinition("na2", "type",
				ParameterRole.INPUT);
		
		preperationAssignmentController.addAssignment(definition);
		preperationAssignmentController.addAssignment(definition2);
		preperationAssignmentController.addAssignment(definition3);
		
		experimentAssignmentController.addAssignment(null);
		experimentAssignmentController.addAssignment(null);
		experimentAssignmentController.addAssignment(null);
	}

	/**
	 * Register handlers to catch events.
	 */
	private void registerEventHandlers() {
		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				experimentChange(event.getExperimentName());
			}
		});
	}

	/**
	 * Event method of the ExperimentChanged Event.
	 * 
	 * @param experimentName
	 */
	private void experimentChange(final String experimentName) {
		LOGGER.info("Change experiment to '" + experimentName + "'");

		ExperimentSeriesDefinition experiment = ScenarioManager.get().getBuilder().getSpecificationBuilder()
				.getExperimentSeries(experimentName);

		view.getSettingsView().getTextboxName().setText(experiment.getName());

		String explorationName = ScenarioManager.get().experiment().getCurrentExperiment().getExplorationStrategy()
				.getName();
		Map<String, String> explorationConfig = ScenarioManager.get().experiment().getCurrentExperiment()
				.getExplorationStrategy().getConfiguration();

		
//		String terminationName = ScenarioManager.get().experiment().getCurrentExperiment()
//				.getExperimentTerminationCondition().getName();
//		Map<String, String> terminationConfig = ScenarioManager.get().experiment().getCurrentExperiment()
//				.getExperimentTerminationCondition().getConfiguration();

		explorationExtController.setExtension(explorationName);
		explorationExtController.setConfigMap(explorationConfig);

//		terminationExtController.setExtension(terminationName);
//		terminationExtController.setConfigMap(terminationConfig);
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void reset() {
		view.reset();
	}

//	/**
//	 * @return the terminationExtController
//	 */
//	public ExperimentExtensionController getTerminationExtController() {
//		return terminationExtController;
//	}

	/**
	 * @return the explorationExtController
	 */
	public ExperimentExtensionController getExplorationExtController() {
		return explorationExtController;
	}

}
