package org.sopeco.frontend.client.layout;

import java.util.HashMap;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.NoScenario;
import org.sopeco.frontend.client.layout.center.environment.EnvironmentController;
import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.result.ResultController;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;
import org.sopeco.frontend.client.layout.navigation.NavigationController;
import org.sopeco.frontend.client.layout.navigation.NavigationView;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * The main-layout of the web-application.
 * 
 * @author Marius Oehler
 * 
 */
public final class MainLayoutPanel extends DockLayoutPanel {

	private static MainLayoutPanel singletonLayoutPanel;

	private NorthPanel northPanel;
	private NavigationController navigationController;
	private FrontendEntryPoint parentModule;
	private ScrollPanel centerScrollPanel;

	private CenterType currentCenterPanel;
	private HashMap<CenterType, ICenterController> centerController = new HashMap<CenterType, ICenterController>();

	private MainLayoutPanel(FrontendEntryPoint parent) {
		super(Unit.EM);

		parentModule = parent;

		initialize();
	}

	/**
	 * 
	 * @return
	 */
	public static MainLayoutPanel get() {
		if (singletonLayoutPanel == null) {
			singletonLayoutPanel = new MainLayoutPanel(FrontendEntryPoint.getFrontendEP());
		}
		return singletonLayoutPanel;
	}

	public static void destroy() {
		if (singletonLayoutPanel != null) {
			singletonLayoutPanel = null;
			// TODO
		}
	}

	/**
	 * Initialize the main layout
	 */
	private void initialize() {
		centerScrollPanel = new ScrollPanel();

		addNorth(getNorthPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
		addWest(getNavigationController().getView(), Float.parseFloat(NavigationView.PANEL_WIDTH));

		getWidgetContainerElement(getNavigationController().getView()).setId("mainNavigation");

		centerController.put(CenterType.Environment, new EnvironmentController());
		centerController.put(CenterType.Specification, new SpecificationController());
		centerController.put(CenterType.Execute, new ExecuteController());
		centerController.put(CenterType.Result, new ResultController());

		currentCenterPanel = CenterType.Specification;
		getNavigationController().setCurrentCenterType(currentCenterPanel);

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
	 * Creates new center panel for the main layout and updates the current
	 * panel.
	 */
	public void createNewCenterPanels() {
		for (ICenterController controller : centerController.values()) {
			controller.reset();
		}

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

		getNavigationController().setCurrentCenterType(type);

		if (northPanel.getSelectedScenario().isEmpty() || type == CenterType.NoScenario) {
			add(new NoScenario());
			return;
		}

		centerScrollPanel.add(centerController.get(type).getView());
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

	/**
	 * 
	 * @return
	 */
	public NavigationController getNavigationController() {
		if (navigationController == null) {
			navigationController = new NavigationController(this);
		}

		return navigationController;
	}

	public ICenterController getCenterController(CenterType type) {
		return centerController.get(type);
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
