package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.FrontendEntryPoint;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The main-layout of the web-application.
 * 
 * @author Marius Oehler
 * 
 */
public class MainLayoutPanel extends DockLayoutPanel {

	private NorthPanel topFilterPanel;
	private FrontendEntryPoint parentModule;

	public MainLayoutPanel(FrontendEntryPoint parent) {
		super(Unit.EM);

		parentModule = parent;

		initialize();
	}

	/**
	 * Initialize the main layout
	 */
	private void initialize() {
		addNorth(getTopFilterPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
	}

	/**
	 * Returns the panel for the northern area.
	 * 
	 * @return see description
	 */
	private Widget getTopFilterPanel() {
		if (topFilterPanel == null) {
			topFilterPanel = new NorthPanel(this);
		}

		return topFilterPanel;
	}

	/**
	 * Returns the parent object.
	 * 
	 * @return parent object
	 */
	public FrontendEntryPoint getParentModule() {
		return parentModule;
	}
}
