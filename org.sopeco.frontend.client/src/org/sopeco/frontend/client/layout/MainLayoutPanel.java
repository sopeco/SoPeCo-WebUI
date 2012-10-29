package org.sopeco.frontend.client.layout;

import java.util.HashMap;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ScenarioLoadedEvent;
import org.sopeco.frontend.client.event.handler.ScenarioLoadedEventHandler;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.NoScenario;
import org.sopeco.frontend.client.layout.center.environment.EnvironmentController;
import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.experiment.ExperimentController;
import org.sopeco.frontend.client.layout.center.result.ResultController;
import org.sopeco.frontend.client.layout.center.specification.SpecificationController;
import org.sopeco.frontend.client.layout.navigation.NavigationController;
import org.sopeco.frontend.client.layout.navigation.NavigationView;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;

/**
 * The main-layout of the web-application.
 * 
 * @author Marius Oehler
 * 
 */
public final class MainLayoutPanel extends DockLayoutPanel implements ValueChangeHandler<String> {

	private static MainLayoutPanel singletonLayoutPanel;
	private static final CenterType DEFAULT_CENTER_TYPE = CenterType.Experiment;

	private NorthPanel northPanel;
	private NavigationController navigationController;
	private FrontendEntryPoint parentModule;

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
		// History.addValueChangeHandler(this);
		// History.fireCurrentHistoryState();

		// centerScrollPanel = new ScrollPanel();
		currentCenterPanel = DEFAULT_CENTER_TYPE;

		addNorth(getNorthPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));
		addWest(getNavigationController().getView(), Float.parseFloat(NavigationView.PANEL_WIDTH));

		getWidgetContainerElement(getNavigationController().getView()).setId("mainNavigation");

		centerController.put(CenterType.Environment, new EnvironmentController());
		centerController.put(CenterType.Specification, new SpecificationController());
		centerController.put(CenterType.Execute, new ExecuteController());
		centerController.put(CenterType.Result, new ResultController());
		centerController.put(CenterType.Experiment, new ExperimentController());

		getNavigationController().setCurrentCenterType(currentCenterPanel);

		updateCenterPanel();

		EventControl.get().addHandler(ScenarioLoadedEvent.TYPE, new ScenarioLoadedEventHandler() {
			@Override
			public void onScenarioLoadedEvent(ScenarioLoadedEvent scenarioLoadedEvent) {
				createNewCenterPanels();
			}
		});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// CenterType oldType = CenterType.valueOf(event.getValue());
		// updateCenterPanel(oldType, false);
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

		updateCenterPanel();

		Metering.stop(metering);
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
		updateCenterPanel(type, true);
	}

	/**
	 * Set the current centerPanel to the given type.
	 * 
	 * @param type
	 */
	public void updateCenterPanel(CenterType type, boolean newHistoryItem) {
		double metering = Metering.start();

		if (getCenter() != null) {
			getCenter().removeFromParent();
		}
		currentCenterPanel = type;
		// centerScrollPanel.clear();

		// if (newHistoryItem) {
		// History.newItem(type.name());
		// }

		getNavigationController().setCurrentCenterType(type);

		if (northPanel.getSelectedScenario().isEmpty() || type == CenterType.NoScenario) {
			add(new NoScenario());

			Metering.stop(metering);
			return;
		}

		// centerScrollPanel.add(centerController.get(type).getView());
		// add(centerScrollPanel);
		add(centerController.get(type).getView());

		Metering.stop(metering);
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
