package org.sopeco.frontend.client.layout;

import java.util.HashMap;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.EnvironmentPanel;
import org.sopeco.frontend.client.layout.center.ExecutePanel;
import org.sopeco.frontend.client.layout.center.NoScenario;
import org.sopeco.frontend.client.layout.center.ResultPanel;
import org.sopeco.frontend.client.layout.center.SpecificationPanel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * The main-layout of the web-application.
 * 
 * @author Marius Oehler
 * 
 */
public class MainLayoutPanel extends DockLayoutPanel {

	private NorthPanel northPanel;
	private Navigation mainNavigation;
	private FrontendEntryPoint parentModule;
	private ScrollPanel centerScrollPanel;

	private CenterType currentCenterPanel;
	private HashMap<CenterType, CenterPanel> centerPanels = new HashMap<CenterType, CenterPanel>();

	public MainLayoutPanel(FrontendEntryPoint parent) {
		super(Unit.EM);

		parentModule = parent;

		initialize();
	}

	/**
	 * Initialize the main layout
	 */
	private void initialize() {
		centerScrollPanel = new ScrollPanel();

		addNorth(getNorthPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
		addWest(getMainNavigation(), Float.parseFloat(Navigation.PANEL_WIDTH));

		getWidgetContainerElement(getMainNavigation()).setId("mainNavigation");

		currentCenterPanel = CenterType.Environment;
		
		createNewCenterPanels();
	}

	/**
	 * Returns the Type of the current centerPanel.
	 * 
	 * @return
	 */
	public CenterType getCenterType() {
		return currentCenterPanel;
	}

	/**
	 * Creates new center panel for the main layout and updates the current panel.
	 */
	public void createNewCenterPanels() {
		centerPanels.put(CenterType.Environment, new EnvironmentPanel());
		centerPanels.put(CenterType.Specification, new SpecificationPanel());
		centerPanels.put(CenterType.Execute, new ExecutePanel());
		centerPanels.put(CenterType.Result, new ResultPanel());

		updateCenterPanel();
	}

	/**
	 * Set a new centerPanel with the current type.
	 */
	public void updateCenterPanel() {
		updateCenterPanel(currentCenterPanel);
	}

	/**
	 * Set the current centerPanel to the given type.
	 * 
	 * @param type
	 */
	public void updateCenterPanel(CenterType type) {
		if (getCenter() != null) {
			remove(getCenter());
		}
		currentCenterPanel = type;
		centerScrollPanel.clear();

		if (northPanel.getSelectedScenario().isEmpty() || type == CenterType.NoScenario) {
			add(new NoScenario());
			return;
		}

		centerScrollPanel.add(centerPanels.get(type));
		add(centerScrollPanel);
	}

	/**
	 * Returns the panel for the northern area.
	 * 
	 * @return see description
	 */
	private NorthPanel getNorthPanel() {
		if (northPanel == null) {
			northPanel = new NorthPanel(this);
		}

		return northPanel;
	}

	private Navigation getMainNavigation() {
		if (mainNavigation == null) {
			mainNavigation = new Navigation(this);
		}

		return mainNavigation;
	}

	public HashMap<CenterType, CenterPanel> getCenterPanels() {
		return centerPanels;
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
