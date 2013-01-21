package org.sopeco.frontend.client.layout.center.execute;

import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.execute.tabFour.TabControllerFour;
import org.sopeco.frontend.client.layout.center.execute.tabOne.ExecuteTab;
import org.sopeco.frontend.client.layout.center.execute.tabOne.TabControllerOne;
import org.sopeco.frontend.client.layout.center.execute.tabThree.TabControllerThree;
import org.sopeco.frontend.client.layout.center.execute.tabTwo.TabControllerTwo;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.resources.FrontEndResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController implements ICenterController, ClickHandler, SelectionHandler<Integer> {

	private static final Logger LOGGER = Logger.getLogger(ExecuteController.class.getName());

	private TabControllerOne tabControllerOne;
	private TabControllerThree tabControllerThree;
	private TabControllerTwo tabControllerTwo;
	private TabControllerFour tabControllerFour;

	private ExecuteTabPanel view;

	/**
	 * Constructor. Loads the CSS resources and invokes the {@link #init()}
	 * method.
	 */
	public ExecuteController() {
		FrontEndResources.loadExecuteViewCSS();
		init();
	}

	/**
	 * Returns the TabController of the first tab.
	 * 
	 * @return TabControllerOne
	 */
	public TabControllerOne getTabControllerOne() {
		return tabControllerOne;
	}

	/**
	 * Returns the TabController of the third tab.
	 * 
	 * @return TabControllerThree
	 */
	public TabControllerThree getTabControllerThree() {
		return tabControllerThree;
	}

	/**
	 * Returns the TabController of the second tab.
	 * 
	 * @return TabControllerTwo
	 */
	public TabControllerTwo getTabControllerTwo() {
		return tabControllerTwo;
	}

	/**
	 * Returns the TabController of the fourth tab.
	 * 
	 * @return TabControllerFour
	 */
	public TabControllerFour getTabControllerFour() {
		return tabControllerFour;
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {

	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		selectedTab(event.getSelectedItem());
	}

	@Override
	public void onSwitchTo() {
		((ExecuteTab) tabControllerOne.getView()).generateTree();
		getTabControllerTwo().loadScheduledExperiments();

		((ExecuteTab) tabControllerOne.getView()).getEditController().setValue(Manager.get().getControllerUrl());

		selectedTab(view.getTabBar().getSelectedTab());
	}

	@Override
	public void reset() {
	}

	/**
	 * Initializes the controller of each tab.
	 */
	private void init() {
		tabControllerOne = new TabControllerOne(this);
		tabControllerTwo = new TabControllerTwo(this);
		tabControllerThree = new TabControllerThree(this);
		tabControllerFour = new TabControllerFour(this);

		view = new ExecuteTabPanel();
		view.add(tabControllerOne.getView(), R.get("ExecuteExperiment"));
		view.add(tabControllerTwo.getView(), R.get("ScheduledExperiments"));
		view.add(tabControllerThree.getView(), R.get("controllerQueue"));
		view.add(tabControllerFour.getView(), R.get("history"));
		view.selectTab(0);

		view.getTabBar().addSelectionHandler(this);
	}

	/**
	 * Invokes the {@link TabController#onSelection()} method of the
	 * TabController, which belongs to the selected Tab.
	 * 
	 * @param x
	 *            index of the selected tab
	 */
	private void selectedTab(int x) {
		switch (x) {
		case 0:
			tabControllerOne.onSelection();
			break;
		case 1:
			tabControllerTwo.onSelection();
			break;
		case 2:
			tabControllerThree.onSelection();
			break;
		case 3:
			tabControllerFour.onSelection();
			break;
		default:
		}
	}

}
