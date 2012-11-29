package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.resources.FrontEndResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentTabPanel extends TabPanel implements ClickHandler {

	private static final String EXPERIMENT_TAB_PANEL_ID = "experimentTabPanel";

	private ExperimentSettingsView settingsView;
	private ExperimentParameterView parameterView;
	
	public ExperimentTabPanel() {
		FrontEndResources.loadExperimentTabPanelCSS();
		init();
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		getElement().setId(EXPERIMENT_TAB_PANEL_ID);

		settingsView = new ExperimentSettingsView();
		parameterView = new ExperimentParameterView();

		add(settingsView, R.get("expConfig"));
		add(parameterView, R.get("expParamAssignments"));

		selectTab(0);

		getTabBar().getTab(1).addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == getTabBar().getTab(1)) {

		}
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
	public ExperimentParameterView getParameterView() {
		return parameterView;
	}

}
