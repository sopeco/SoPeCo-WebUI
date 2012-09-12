package org.sopeco.frontend.client.layout;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainPanel extends VerticalPanel {

	private TopFilterPanel topFilterPanel;

	public MainPanel() {
		super();

		initialize();
	}

	/*
	 * Initialize the main layout
	 */
	private void initialize () {
		setWidth("100%");
		setSpacing(20);
		
		add(getTopFilterPanel());
	}
	
	/*
	 * Creates the top panel (database selection etc)
	 */
	private Widget getTopFilterPanel() {
		if ( topFilterPanel == null ) {
			topFilterPanel = new TopFilterPanel();
		}
		
		return topFilterPanel;
	}

}
