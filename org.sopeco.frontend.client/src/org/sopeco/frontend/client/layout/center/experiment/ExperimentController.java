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
import org.sopeco.frontend.client.layout.center.experiment.assignment.PreparationController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController, ValueChangeHandler<String>, ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());
	private static final String ENV_TREE_CSS_CLASS = "expEnvTree";

	private ExperimentView view;
	private ExperimentExtensionController explorationExtController;
	// private ParameterTreeController treeController;
	private TerminationController terminationController;
	private ExperimentEnvironmentTree expEnvironmentTree;

	private AssignmentController assignmentPreperation, assignmentExperiment;
	private PreparationController preparationController;

	private ExperimentTabPanel tabPanel;

	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

		tabPanel = new ExperimentTabPanel();

		explorationExtController = new ExperimentExtensionController(this, ExperimentView.EXP_SETTINGS_PANEL_WIDTH);

		// assignmentPreperation = new AssignmentController(Type.PREPERATION);
		assignmentExperiment = new AssignmentController(Type.EXPERIMENT);

		preparationController = new PreparationController();

		terminationController = new TerminationController();

		view = new ExperimentView();
		// treeController = new ParameterTreeController();
		expEnvironmentTree = new ExperimentEnvironmentTree();

		getSettingsView().addExtensionView(explorationExtController.getView());
		getSettingsView().add(terminationController.getView());
		// getParameterView().add(assignmentPreperation.getView());
		// getParameterView().add(preparationController.getView());

		// TODO clean this up
		FlowPanel rightCol = new FlowPanel();
		rightCol.getElement().getStyle().setPosition(Position.ABSOLUTE);
		rightCol.getElement().getStyle().setTop(0, Unit.PX);
		rightCol.getElement().getStyle().setLeft(400, Unit.PX);
		rightCol.getElement().getStyle().setRight(0, Unit.PX);
		rightCol.getElement().getStyle().setBottom(0, Unit.PX);
		rightCol.getElement().getStyle().setOverflow(Overflow.AUTO);

		rightCol.add(preparationController.getView());
		rightCol.add(assignmentExperiment.getView());
		getParameterView().add(rightCol);

		
		
		// getParameterView().add(treeController.getView());
		getParameterView().add(expEnvironmentTree.getView());

		expEnvironmentTree.getView().addStyleName(ENV_TREE_CSS_CLASS);

		explorationExtController.setHeadline(R.get("explStrategy"));

		explorationExtController.setExtensionType(ExtensionTypes.EXPLORATIONSTRATEGY);

		getSettingsView().getImgRemove().addClickHandler(this);
		getSettingsView().getImgRename().addClickHandler(this);
		getSettingsView().getImgDuplicate().addClickHandler(this);

		registerEventHandlers();
	}

	/**
	 * Returns the settings view of the experiment series.
	 * 
	 * @return
	 */
	public ExperimentSettingsView getSettingsView() {
		return tabPanel.getSettingsView();
	}

	/**
	 * Returns the parameter view of the experiment series.
	 * 
	 * @return
	 */
	public ExperimentParameterView getParameterView() {
		return tabPanel.getParameterView();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == getSettingsView().getImgRemove()) {
			removeExperiment();
		} else if (event.getSource() == getSettingsView().getImgRename()) {
			showRenameDialog();
		} else if (event.getSource() == getSettingsView().getImgDuplicate()) {
			duplicateExperiment();
		}
	}

	private void duplicateExperiment() {
		TextInput.doInput(R.get("cloneExperiment"), R.get("nameForExperimentClone") + ":", new TextInputOkHandler() {
			@Override
			public void onInput(ClickEvent event, String input) {
				ScenarioManager.get().experiment().cloneCurrentExperiment(input);
			}
		});
	}

	private void showRenameDialog() {
		TextInput.doInput(R.get("renameExperiment"), R.get("newExpName") + ":", new TextInputOkHandler() {
			@Override
			public void onInput(ClickEvent event, String input) {
				ScenarioManager.get().experiment().renameCurrentExpSeries(input);
			}
		});
	}

	private void removeExperiment() {
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
		// if (event.getSource() == getSettingsView().getTextboxName()) {
		// ScenarioManager.get().experiment().renameCurrentExpSeries(event.getValue());
		// }
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
		LOGGER.fine("Change experiment to '" + experimentName + "'");

		ExperimentSeriesDefinition experiment = ScenarioManager.get().getBuilder().getSpecificationBuilder()
				.getExperimentSeries(experimentName);

		getSettingsView().setExperimentName(experiment.getName());

		String explorationName = ScenarioManager.get().experiment().getCurrentExperiment().getExplorationStrategy()
				.getName();
		Map<String, String> explorationConfig = ScenarioManager.get().experiment().getCurrentExperiment()
				.getExplorationStrategy().getConfiguration();

		explorationExtController.setExtension(explorationName);
		explorationExtController.setConfigMap(explorationConfig);
	}

	@Override
	public Widget getView() {
		// return view;
		return tabPanel;
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
