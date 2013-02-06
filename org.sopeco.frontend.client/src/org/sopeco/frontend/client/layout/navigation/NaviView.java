package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.resources.R;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NaviView extends FlowPanel {
	private static final String CSS_CLASS = "navigation";
	private static final String CSS_CLASS_LOGO_PANEL = "logoPanel";

	private Image logo;
	private FlowPanel logoPanel;

	public NaviView() {
		R.resc.cssNavigation().ensureInjected();

		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		logo = new Image(R.resc.imgSDQLogo());
		logoPanel = new FlowPanel();

		logoPanel.addStyleName(CSS_CLASS_LOGO_PANEL);
		logoPanel.add(new HTML("powered by"));
		logoPanel.add(logo);

		add(logoPanel);
	}

	@Override
	public void clear() {
		super.clear();
		add(logoPanel);
	}
}
