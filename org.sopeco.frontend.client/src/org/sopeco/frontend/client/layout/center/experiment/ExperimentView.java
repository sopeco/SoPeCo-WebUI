package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.layout.center.CenterPanel;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentView extends CenterPanel {

	/** Width of the SettingsPanel. */
	public static final int EXP_SETTINGS_PANEL_WIDTH = 400;

	private ExperimentSettingsView settingsView;
	private ExperimentParameterView parameterView;


	public ExperimentView() {
		initialize();
	}

	/**
	 * Initialize all necessary objects.
	 */
	private void initialize() {
		settingsView = new ExperimentSettingsView();
		parameterView = new ExperimentParameterView();
		
		add(settingsView);
		add(parameterView);
	}

	/**
	 * Sets all important values to a default value.
	 */
	public void reset() {
		settingsView.reset();
	}

	/**
	 * @return the settingsView
	 */
	public ExperimentSettingsView getSettingsView() {
		return settingsView;
	}

	/**
	 * @return the parameterView
	 */
	public FlowPanel getParameterView() {
		return parameterView;
	}

}
