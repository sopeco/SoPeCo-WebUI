package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.resources.FrontEndResources;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteTabPanel extends TabPanel {

	private static final String CSS_CLASS = "sopeco-TabPanel";
	private static final String EXECUTE_CSS_CLASS = "executeTabPanel";

	public ExecuteTabPanel() {
		FrontEndResources.loadSopecoTabPanelCSS();
		init();
	}

	/**
	 * 
	 */
	private void init() {
		addStyleName(CSS_CLASS);
		addStyleName(EXECUTE_CSS_CLASS);
	}
}
