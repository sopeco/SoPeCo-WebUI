package org.sopeco.frontend.client.layout.center.experiment;

import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.model.ExperimentModul.TerminationCondition;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
@SuppressWarnings("rawtypes")
public class ExperimentController implements ICenterController, ValueChangeHandler {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());

	private ExperimentView view;
	private ExperimentExtensionController terminationExtController, explorationExtController;

	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

		terminationExtController = new ExperimentExtensionController(ExperimentSettingsView.EXP_SETTINGS_PANEL_WIDTH);
		explorationExtController = new ExperimentExtensionController(ExperimentSettingsView.EXP_SETTINGS_PANEL_WIDTH);

		view = new ExperimentView();

		view.getSettingsView().addExtensionView(explorationExtController.getView());
		view.getSettingsView().addExtensionView(terminationExtController.getView());
		
		explorationExtController.setHeadline(R.get("explStrategy"));
		terminationExtController.setHeadline(R.get("termination"));
		
		explorationExtController.setExtensionType(ExtensionTypes.ExplorationStrategy);
		terminationExtController.setExtensionType(ExtensionTypes.TerminationCondition);
		
		addHandlersToTerminationGrid();

		registerEventHandlers();
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

		view.getSettingsView().getTextboxName().setText(experimentName);
	}

	/**
	 * Adding the handlers of the termiantion grid in the SettingsView.
	 */
	@SuppressWarnings("unchecked")
	private void addHandlersToTerminationGrid() {
		// view.getSettingsView().getRadioRepetitions().addValueChangeHandler(this);
		// view.getSettingsView().getRadioTimeout().addValueChangeHandler(this);
		// view.getSettingsView().getTextboxRepetitions().addValueChangeHandler(this);
		// view.getSettingsView().getTextboxTimeout().addValueChangeHandler(this);
	}

	@Override
	public void onValueChange(ValueChangeEvent event) {
		// if (event.getSource() == view.getSettingsView().getRadioRepetitions()
		// || event.getSource() == view.getSettingsView().getRadioTimeout()) {
		// updateTerminationCondition();
		// } else if (event.getSource() ==
		// view.getSettingsView().getTextboxRepetitions()) {
		// setTerminationRadioButtons(TerminationCondition.Repetitions);
		// updateTerminationCondition();
		// } else if (event.getSource() ==
		// view.getSettingsView().getTextboxTimeout()) {
		// setTerminationRadioButtons(TerminationCondition.Timeout);
		// updateTerminationCondition();
		// }
	}

	/**
	 * Sets the termination radio buttons
	 */
	private void setTerminationRadioButtons(TerminationCondition condition) {
		// if (condition == TerminationCondition.Repetitions) {
		// view.getSettingsView().getRadioRepetitions().setValue(true);
		// view.getSettingsView().getRadioTimeout().setValue(false);
		// } else if (condition == TerminationCondition.Timeout) {
		// view.getSettingsView().getRadioRepetitions().setValue(false);
		// view.getSettingsView().getRadioTimeout().setValue(true);
		// }
	}

	/**
	 * Updates the termination condition of the current experiment according to
	 * the user's input.
	 */
	private void updateTerminationCondition() {
		// String value = "";
		// TerminationCondition condition;
		// if (view.getSettingsView().getRadioRepetitions().getValue()) {
		// LOGGER.info("New TerminationCondition Repetitions");
		// value = view.getSettingsView().getTextboxRepetitions().getText();
		// condition = TerminationCondition.Repetitions;
		// view.getSettingsView().setTerminationGridRowTransparent(1);
		// } else if (view.getSettingsView().getRadioTimeout().getValue()) {
		// LOGGER.info("New TerminationCondition Timeout");
		// value = view.getSettingsView().getTextboxTimeout().getText();
		// condition = TerminationCondition.Timeout;
		// view.getSettingsView().setTerminationGridRowTransparent(0);
		// } else {
		// LOGGER.info("Unknow TerminationCondition State..");
		// view.getSettingsView().setTerminationGridRowTransparent(-1);
		// return;
		// }
		//
		// ScenarioManager.get().experiment().setTerminationCondition(condition,
		// value);
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void reset() {
		view.reset();
	}

}
