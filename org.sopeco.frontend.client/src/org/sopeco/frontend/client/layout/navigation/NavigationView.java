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
package org.sopeco.frontend.client.layout.navigation;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.resources.R;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * The Navigation Widget.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationView extends FlowPanel {

	/**
	 * Width of this panel.
	 */
	public static final String PANEL_WIDTH = "15";
	private static final int ITEM_MAP_SIZE = 4;
	private static final float ITEM_MAP_LOAD_FACTOR = 1F;

	private Map<CenterType, NavigationItem> naviItems;
	private Map<String, NavigationSubItem> experimentItems;
	private FlowPanel experimentsPanel;
	private ChangeSpecificationPanel changeSpecificationPanel;

	public NavigationView() {
		initialize();
	}

	/**
	 * Initializes all necessary widgets.
	 */
	private void initialize() {
		setWidth(PANEL_WIDTH + "em");
		setHeight("100%");

		experimentsPanel = new FlowPanel();

		naviItems = new HashMap<CenterType, NavigationItem>(ITEM_MAP_SIZE, ITEM_MAP_LOAD_FACTOR);
		experimentItems = new HashMap<String, NavigationSubItem>();

		// addNaviItem(CenterType.Environment, R.get("environment"));
		addNaviItem(CenterType.Specification, R.get("specification"));

		add(experimentsPanel);

		addNaviItem(CenterType.Execute, R.get("execute"));
		addNaviItem(CenterType.Result, R.get("result"));
		addNaviItem(CenterType.Visualization, R.get("visualization"));

		changeSpecificationPanel = naviItems.get(CenterType.Specification).addChangeSpecificationIcon();

		HTML lastModified = new HTML("Last modified: " + FrontendEntryPoint.getDocumentLastModifiedDate());
		lastModified.getElement().getStyle().setFontSize(11, Unit.PX);
		lastModified.getElement().getStyle().setColor("#CCC");
		lastModified.getElement().getStyle().setPosition(Position.ABSOLUTE);
		lastModified.getElement().getStyle().setBottom(0, Unit.PX);
		lastModified.getElement().getStyle().setLeft(1, Unit.EM);
		add(lastModified);
	}

	/**
	 * Adding a new item to the navigation and add it to the itemMap, where all
	 * items of the navigation are stored.
	 * 
	 * @param type
	 *            which panel should be displayed, when this item is clicked
	 * @param text
	 *            the text on the item
	 */
	private void addNaviItem(CenterType type, String text) {
		NavigationItem item = new NavigationItem(type, text);
		naviItems.put(type, item);
		add(item);
	}

	/**
	 * Returns panel, to change the specifications.
	 * 
	 * @return ChangeSpecificationPanel
	 */
	public ChangeSpecificationPanel getChangeSpecificationPanel() {
		return changeSpecificationPanel;
	}

	/**
	 * Returns the HashMap, which contains all items of the navigation.
	 * 
	 * @return hashmap
	 */
	public Map<CenterType, NavigationItem> getNaviItemsMap() {
		return naviItems;
	}

	/**
	 * Returns a Map, which contains all navi-items (NavigationSubItem) to
	 * switch between the experiments.
	 * 
	 * @return the experimentItems
	 */
	public Map<String, NavigationSubItem> getExperimentItems() {
		return experimentItems;
	}

	/**
	 * Adds a new, indented item to the list of experiment-items in the
	 * navigation.
	 * 
	 * @param text
	 *            on the item
	 */
	public NavigationSubItem addExperimentItem(String text) {
		NavigationSubItem newItem = new NavigationSubItem(text, CenterType.Experiment);
		experimentsPanel.add(newItem);
		experimentItems.put(text, newItem);
		return newItem;
	}

	/**
	 * Removes all experiments from the navigation.
	 */
	public void clearExperiments() {
		experimentsPanel.clear();
	}

}
