package org.sopeco.frontend.client.layout;

import java.util.HashMap;

import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.EmptyCenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.NoScenario;
import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.experiment.ExperimentController;
import org.sopeco.frontend.client.layout.center.result.ResultController;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;
import org.sopeco.frontend.client.layout.center.visualization.VisualizationController;
import org.sopeco.frontend.client.layout.navigation.NavigationController;
import org.sopeco.frontend.client.layout.navigation.NavigationView;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The main-layout of the web-application.
 * 
 * @author Marius Oehler
 * 
 */
public final class MainLayoutPanel extends DockLayoutPanel {

	private static final String NAVIGATION_PANEL_ID = "mainNavigation";

	private static MainLayoutPanel layoutPanel;
	private static final CenterType DEFAULT_CENTER_TYPE = CenterType.Other;

	private NorthPanel northPanel;
	private NavigationController navigationController;
	private ViewSwitch viewSwitch;

	private CenterType currentCenterPanel;
	private HashMap<CenterType, ICenterController> centerController = new HashMap<CenterType, ICenterController>();

	private MainLayoutPanel() {
		super(Unit.EM);
		initialize();
	}

	/**
	 * 
	 * @return
	 */
	public static MainLayoutPanel get() {
		if (layoutPanel == null) {
			layoutPanel = new MainLayoutPanel();
		}
		return layoutPanel;
	}

	public static void destroy() {
		if (layoutPanel != null) {
			layoutPanel = null;
			// TODO
		}
	}

	/**
	 * Hides the navigation panel.
	 */
	public void hideNavigation() {
		remove(navigationController.getView());
	}

	/**
	 * Show the navigation panel.
	 */
	public void showNavigation() {
		// if (!getNavigationController().getView().isAttached()) {
		Widget center = getCenter();
		if (center != null) {
			remove(center);
		}
		addWest(getNavigationController().getView(), Float.parseFloat(NavigationView.PANEL_WIDTH));
		getWidgetContainerElement(getNavigationController().getView()).setId(NAVIGATION_PANEL_ID);
		if (center != null) {
			add(center);
		}
		// }
	}

	/**
	 * @return the viewSwitch
	 */
	public ViewSwitch getViewSwitch() {
		if (viewSwitch == null) {
			viewSwitch = new ViewSwitch();
		}
		return viewSwitch;
	}

	/**
	 * Initialize the main layout
	 */
	private void initialize() {
		GWT.log("initialize >");
		currentCenterPanel = DEFAULT_CENTER_TYPE;

		addNorth(getNorthPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
		addWest(getNavigationController().getView(), Float.parseFloat(NavigationView.PANEL_WIDTH));
		getWidgetContainerElement(getNavigationController().getView()).setId(NAVIGATION_PANEL_ID);

		centerController.put(CenterType.Specification, new SpecificationController());

		centerController.put(CenterType.Execute, new ExecuteController());
		// centerController.put(CenterType.Execute, new ExecuteController());

		centerController.put(CenterType.Result, new ResultController());
		centerController.put(CenterType.Experiment, new ExperimentController());
		centerController.put(CenterType.Visualization, new VisualizationController());

		updateCenterPanel(currentCenterPanel);

		ScenarioManager.get().switchScenario(Manager.get().getAccountDetails().getSelectedScenario());
		GWT.log("< initialize");
	}

	/**
	 * Returns the SpecificationController of the layout, which is stored in the
	 * centerController-Map.
	 * 
	 * @return SpecificationController
	 */
	public SpecificationController getSpecificationController() {
		if (!centerController.containsKey(CenterType.Specification)) {
			return null;
		}
		return (SpecificationController) centerController.get(CenterType.Specification);
	}

	/**
	 * Returns the SpecificationController of the layout, which is stored in the
	 * centerController-Map.
	 * 
	 * @return SpecificationController
	 */
	public ExperimentController getExperimentController() {
		if (!centerController.containsKey(CenterType.Experiment)) {
			return null;
		}
		return (ExperimentController) centerController.get(CenterType.Experiment);
	}

	/**
	 * Returns the ExecuteController of the layout, which is stored in the
	 * centerController-Map.
	 * 
	 * @return ExecuteController
	 */
	public ExecuteController getExecuteController() {
		if (!centerController.containsKey(CenterType.Execute)) {
			return null;
		}
		return (ExecuteController) centerController.get(CenterType.Execute);
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
		double metering = Metering.start();
		for (ICenterController controller : centerController.values()) {
			controller.reset();
		}
		updateCenterPanel(currentCenterPanel);
		Metering.stop(metering);
	}

	/**
	 * Set the current centerPanel to the given type.
	 * 
	 * @param type
	 */
	public void updateCenterPanel(CenterType type) {
		double metering = Metering.start();

		if (getCenter() != null) {
			remove(getCenter());
		}
		currentCenterPanel = type;

		getNavigationController().setCurrentCenterType(type);

		if (Manager.get().getAccountDetails().getScenarioNames().length == 0 || type == CenterType.NoScenario) {
			hideNavigation();
			add(new NoScenario());
			Metering.stop(metering);
			return;
		} else if (type == CenterType.Other) {
			hideNavigation();
			add(new EmptyCenterPanel());
			Metering.stop(metering);
			return;
		}

		showNavigation();
		add(centerController.get(type).getView());
		Metering.stop(metering);
	}

	/**
	 * Returns the panel for the northern area.
	 * 
	 * @return see description
	 */
	public NorthPanel getNorthPanel() {
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
			navigationController = new NavigationController();
		}
		return navigationController;
	}

	public ICenterController getCenterController(CenterType type) {
		return centerController.get(type);
	}
}
