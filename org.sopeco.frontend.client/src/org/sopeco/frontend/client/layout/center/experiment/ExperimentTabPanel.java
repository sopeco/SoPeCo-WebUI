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

	private static final String EXPERIMENT_TAB_PANEL_CLASS = "sopeco-TabPanel";

	private ExperimentSettingsView settingsView;
	private ExperimentParameterView parameterView;

	public ExperimentTabPanel() {
		FrontEndResources.loadSopecoTabPanelCSS();
		init();
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		addStyleName(EXPERIMENT_TAB_PANEL_CLASS);

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
