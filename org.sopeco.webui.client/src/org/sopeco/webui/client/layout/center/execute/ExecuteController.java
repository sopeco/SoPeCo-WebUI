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
package org.sopeco.webui.client.layout.center.execute;

import java.util.logging.Logger;

import org.sopeco.webui.client.layout.center.ICenterController;
import org.sopeco.webui.client.layout.center.execute.tabFour.TabControllerFour;
import org.sopeco.webui.client.layout.center.execute.tabOne.ExecuteTab;
import org.sopeco.webui.client.layout.center.execute.tabOne.TabControllerOne;
import org.sopeco.webui.client.layout.center.execute.tabThree.TabControllerThree;
import org.sopeco.webui.client.layout.center.execute.tabTwo.TabControllerTwo;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;

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
		R.css.cssExecuteView().ensureInjected();
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
	public void reload() {
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
		view.add(tabControllerOne.getView(), R.lang.ExecuteExperiment());
		view.add(tabControllerTwo.getView(), R.lang.ScheduledExperiments());
		view.add(tabControllerThree.getView(), R.lang.controllerQueue());
		view.add(tabControllerFour.getView(), R.lang.history());
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
