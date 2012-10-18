package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.layout.center.CenterPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentView extends CenterPanel {

	private ExperimentSettingsView settingsView;

	public ExperimentView() {
		initialize();
	}

	/**
	 * Initialize all necessary objects.
	 */
	private void initialize() {
		settingsView = new ExperimentSettingsView();

		add(settingsView);
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

}
