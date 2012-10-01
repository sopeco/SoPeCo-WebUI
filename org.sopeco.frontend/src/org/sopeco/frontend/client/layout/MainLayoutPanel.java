package org.sopeco.frontend.client.layout;

import java.util.HashMap;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.MainNavigation.Navigation;
import org.sopeco.frontend.client.layout.center.CenterPanel;
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
	private MainNavigation mainNavigation;
	private FrontendEntryPoint parentModule;
	private ScrollPanel centerScrollPanel;

	private Navigation currentCenterPanel;
	private HashMap<Navigation, CenterPanel> centerPanels = new HashMap<Navigation, CenterPanel>();

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

		addNorth(getTopFilterPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
		addWest(getMainNavigation(), Float.parseFloat(MainNavigation.PANEL_WIDTH));

		getWidgetContainerElement(getMainNavigation()).setId("mainNavigation");

		currentCenterPanel = Navigation.Environment;
		
		createNewCenterPanels();
	}

	/**
	 * Returns the Type of the current centerPanel.
	 * 
	 * @return
	 */
	public Navigation getCenterType() {
		return currentCenterPanel;
	}

	/**
	 * Creates new center panel for the main layout and updates the current panel.
	 */
	public void createNewCenterPanels() {
		centerPanels.put(Navigation.Environment, new EnvironmentPanel());
		centerPanels.put(Navigation.Specification, new SpecificationPanel());
		centerPanels.put(Navigation.Execute, new ExecutePanel());
		centerPanels.put(Navigation.Result, new ResultPanel());

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
	public void updateCenterPanel(Navigation type) {
		if (getCenter() != null) {
			remove(getCenter());
		}
		currentCenterPanel = type;
		centerScrollPanel.clear();

		if (northPanel.getSelectedScenario().isEmpty() || type == Navigation.NoScenario) {
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
	private NorthPanel getTopFilterPanel() {
		if (northPanel == null) {
			northPanel = new NorthPanel(this);
		}

		return northPanel;
	}

	private MainNavigation getMainNavigation() {
		if (mainNavigation == null) {
			mainNavigation = new MainNavigation(this);
		}

		return mainNavigation;
	}

	public HashMap<Navigation, CenterPanel> getCenterPanels() {
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
