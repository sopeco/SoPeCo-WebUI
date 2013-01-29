/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
import org.sopeco.frontend.client.layout.navigation.NaviController;
import org.sopeco.frontend.client.layout.navigation.NaviItem;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
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

	// private NavigationController navigationController;
	private NaviController naviController;

	private ViewSwitch viewSwitch;

	// private CenterType currentCenterPanel;
	// private HashMap<CenterType, ICenterController> centerControllerMap = new
	// HashMap<CenterType, ICenterController>();

	// TODO
	private HashMap<Class<ICenterController>, ICenterController> controllerMap = new HashMap<Class<ICenterController>, ICenterController>();
	private Class currentCenterClass;

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
		// remove(navigationController.getView());
		remove(naviController.getView());
	}

	/**
	 * Show the navigation panel.
	 */
	public void showNavigation() {
		Widget center = getCenter();
		if (center != null) {
			remove(center);

		}
		// addWest(getNavigationController().getView(),
		// Float.parseFloat(NavigationView.PANEL_WIDTH));
		// TODO
		addWest(naviController.getView(), 15);
		getWidgetContainerElement(naviController.getView()).getStyle().setOverflow(Overflow.VISIBLE);

		// getWidgetContainerElement(getNavigationController().getView()).setId(NAVIGATION_PANEL_ID);

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
		// currentCenterPanel = DEFAULT_CENTER_TYPE;
		addNorth(getNorthPanel(), Float.parseFloat(NorthPanel.PANEL_HEIGHT));

		// addWest(getNavigationController().getView(),
		// Float.parseFloat(NavigationView.PANEL_WIDTH));
		// TODO
		naviController = new NaviController();
		addWest(naviController.getView(), 15);

		// getWidgetContainerElement(getNavigationController().getView()).setId(NAVIGATION_PANEL_ID);

		// centerControllerMap.put(CenterType.Specification, new
		// SpecificationController());
		//
		// centerControllerMap.put(CenterType.Execute, new ExecuteController());
		// centerController.put(CenterType.Execute, new ExecuteController());

		// centerControllerMap.put(CenterType.Result, new ResultController());
		// centerControllerMap.put(CenterType.Experiment, new
		// ExperimentController());
		// centerControllerMap.put(CenterType.Visualization, new
		// VisualizationController());

		registerCenterController(new SpecificationController());
		registerCenterController(new ExperimentController());
		registerCenterController(new ExecuteController());
		registerCenterController(new ResultController());
		registerCenterController(new VisualizationController());

		// updateCenterPanel(currentCenterPanel);

		refreshView();

		ScenarioManager.get().switchScenario(Manager.get().getAccountDetails().getSelectedScenario());
		GWT.log("< initialize");
	}

	private void registerCenterController(ICenterController controller) {
		Class clazz = controller.getClass();
		if (controllerMap.containsKey(clazz)) {
			throw new RuntimeException("ICenterController of class " + clazz.getName() + " already exists.");
		}

		controllerMap.put(clazz, controller);
	}

	public void buildNavigation() {
		GWT.log("Build Navi");

		naviController.clear();
		addCenterController(SpecificationController.class, "Specification", Manager.get().getCurrentScenarioDetails()
				.getSelectedSpecification());
		addExperiments();
		naviController.addAddExpSeriesItem();
		addCenterController(ExecuteController.class, "Execute");
		addCenterController(ResultController.class, "Result");
		addCenterController(VisualizationController.class, "Visualization");

		naviController.refreshSpecificationPopup();
	}

	private void addExperiments() {
		for (ExperimentSeriesDefinition experiment : ScenarioManager.get().experiment()
				.getExperimentsOfCurrentSpecififcation()) {
			addCenterController(ExperimentController.class, experiment.getName()).setAsExperiment();
		}
	}

	public <T extends ICenterController> T getController(Class<T> clazz) {
		for (ICenterController controller : controllerMap.values()) {
			if (clazz.equals(controller.getClass())) {
				return (T) controller;
			}
		}
		return null;
	}

	public <T extends ICenterController> NaviItem addCenterController(Class<T> controllerClass) {
		return addCenterController(controllerClass, null);
	}

	public <T extends ICenterController> NaviItem addCenterController(Class<T> controllerClass, String text) {
		return addCenterController(controllerClass, text, null);
	}

	public <T extends ICenterController> NaviItem addCenterController(Class<T> controllerClass, String text,
			String subText) {
		return naviController.addItem(controllerClass, text, subText);
	}

	public <T extends ICenterController> void switchView(Class<T> targetClass) {
		currentCenterClass = targetClass;
		controllerMap.get(targetClass).onSwitchTo();
		refreshView();
	}

	public void switchToExperiment(String experimentName) {
		ScenarioManager.get().experiment().setCurrentExperiment(experimentName);
		switchView(ExperimentController.class);
	}

	public NaviController getNaviController() {
		return naviController;
	}

	private void refreshView() {
		if (getCenter() != null) {
			remove(getCenter());
		}

		naviController.setSelectedItem(currentCenterClass);

		if (currentCenterClass == null) {

			hideNavigation();
			add(new EmptyCenterPanel());

		} else if (Manager.get().getAccountDetails().getScenarioNames().length == 0) {

			hideNavigation();
			add(new NoScenario());

		} else {

			showNavigation();
			add(controllerMap.get(currentCenterClass).getView());

		}
	}

	public void setSpecification(String specificationName) {
		buildNavigation();
		switchView(SpecificationController.class);
		naviController.getItem(SpecificationController.class).get(0).setSubText(specificationName);
		getController(SpecificationController.class).getView().setSpecificationName(specificationName);
		naviController.getSpecificationPopup().setSelectedItem(specificationName);
	}

	/**
	 * Returns the Type of the current centerPanel.
	 * 
	 * @return
	 */
	// public CenterType getCenterType() {
	// return currentCenterPanel;
	// }

	/**
	 * Creates new center panel for the main layout and updates the current
	 * panel.
	 */
	public void reloadPanels() {
		double metering = Metering.start();
		// for (ICenterController controller : centerControllerMap.values()) {
		// controller.reload();
		// }
		// TODO
		for (ICenterController controller : controllerMap.values()) {
			controller.reload();
		}
		// updateCenterPanel(currentCenterPanel);
		buildNavigation();
		refreshView();
		Metering.stop(metering);
	}

	/**
	 * Set the current centerPanel to the given type.
	 * 
	 * @param type
	 */
	// public void updateCenterPanel(CenterType type) {
	// double metering = Metering.start();
	//
	// if (getCenter() != null) {
	// remove(getCenter());
	// }
	// currentCenterPanel = type;
	//
	// getNavigationController().setCurrentCenterType(type);
	//
	// if (Manager.get().getAccountDetails().getScenarioNames().length == 0 ||
	// type == CenterType.NoScenario) {
	// hideNavigation();
	// add(new NoScenario());
	// Metering.stop(metering);
	// return;
	// } else if (type == CenterType.Other) {
	// hideNavigation();
	// add(new EmptyCenterPanel());
	// Metering.stop(metering);
	// return;
	// }
	//
	// showNavigation();
	// add(centerControllerMap.get(type).getView());
	// Metering.stop(metering);
	// }

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
	// public NavigationController getNavigationController() {
	// if (navigationController == null) {
	// navigationController = new NavigationController();
	// }
	// return navigationController;
	// }

	// public ICenterController getCenterController(CenterType type) {
	// return centerControllerMap.get(type);
	// }
}
