package org.sopeco.frontend.client.layout.center.experiment;

import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController {

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

		explorationExtController.setExtensionType(ExtensionTypes.EXPLORATIONSTRATEGY);
		terminationExtController.setExtensionType(ExtensionTypes.TERMINATIONCONDITION);

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

		ExperimentSeriesDefinition experiment = ScenarioManager.get().getBuilder().getSpecificationBuilder()
				.getExperimentSeries(experimentName);

		view.getSettingsView().getTextboxName().setText(experiment.getName());
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
