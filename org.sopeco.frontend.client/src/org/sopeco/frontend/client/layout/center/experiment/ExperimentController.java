package org.sopeco.frontend.client.layout.center.experiment;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.experiment.assignment.AssignmentController;
import org.sopeco.frontend.client.layout.center.experiment.assignment.AssignmentController.Type;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController, ValueChangeHandler<String>, ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());

	private ExperimentView view;
	private ExperimentExtensionController explorationExtController;
	private ParameterTreeController treeController;
	private TerminationController terminationController;

	private AssignmentController assignmentPreperation, assignmentExperiment;

	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

		explorationExtController = new ExperimentExtensionController(this, ExperimentView.EXP_SETTINGS_PANEL_WIDTH);

		assignmentPreperation = new AssignmentController(Type.PREPERATION);
		assignmentExperiment = new AssignmentController(Type.EXPERIMENT);

		terminationController = new TerminationController();

		view = new ExperimentView();
		treeController = new ParameterTreeController();

		view.getSettingsView().addExtensionView(explorationExtController.getView());
		view.getSettingsView().add(terminationController.getView());

		view.getParameterView().add(assignmentPreperation.getView());
		view.getParameterView().add(assignmentExperiment.getView());
		view.add(treeController.getView());

		explorationExtController.setHeadline(R.get("explStrategy"));

		explorationExtController.setExtensionType(ExtensionTypes.EXPLORATIONSTRATEGY);

		view.getSettingsView().getTextboxName().addValueChangeHandler(this);
		view.getSettingsView().getRemoveExperimentImage().addClickHandler(this);

		registerEventHandlers();
	}

	@Override
	public void onClick(ClickEvent event) {
		Confirmation.confirm(R.get("removeTihsExp"), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ScenarioManager.get().experiment().removeCurrentExperimentSeries();

				List<ExperimentSeriesDefinition> expList = ScenarioManager.get().experiment()
						.getExperimentsOfCurrentSpecififcation();

				if (expList.isEmpty()) {
					MainLayoutPanel.get().setMessage(R.get("noExpSeries"), R.get("plsAddExpSeries"));
				} else {
					MainLayoutPanel.get().getViewSwitch().switchToExperiment(expList.get(0).getName());
				}
			}
		});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == view.getSettingsView().getTextboxName()) {
			ScenarioManager.get().experiment().renameCurrentExpSeries(event.getValue());
		}
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

		explorationExtController.setExtension(explorationName);
		explorationExtController.setConfigMap(explorationConfig);
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void reset() {
		view.reset();
	}

	/**
	 * @return the explorationExtController
	 */
	public ExperimentExtensionController getExplorationExtController() {
		return explorationExtController;
	}

}
