package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.resources.R;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NaviView extends FlowPanel {
	private static final String CSS_CLASS = "navigation";

	public NaviView() {
		R.resc.cssNavigation().ensureInjected();

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);
	}
}
