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
package org.sopeco.frontend.client.layout.center.execute.tabOne;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.tree.Tree;
import org.sopeco.gwt.widgets.tree.TreeItem;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectionPanel extends FlowPanel implements ValueChangeHandler<Boolean> {

	private static final String TREE_ITEM_EXPERIMENT_CSS = "treeItem-Experiment";
	private static final String TREE_ITEM_SPECIFICATION_CSS = "treeItem-Specification";
	private static final String CSS_CLASS = "executeSelectionPanel";

	private Tree tree;
	private TreeItem root;
	private Headline headline;

	private Map<String, Map<String, SelectionTreeItem>> treeItems;

	public SelectionPanel() {
		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		tree = new Tree();

		treeItems = new HashMap<String, Map<String, SelectionTreeItem>>();

		headline = new Headline("Executed ExperimentSeries");
		add(headline);
		add(tree);
	}

	public void generateTree() {
		double metering = Metering.start();

		treeItems = new HashMap<String, Map<String, SelectionTreeItem>>();

		root = new TreeItem("", true);
		for (MeasurementSpecification ms : ScenarioManager.get().getCurrentScenarioDefinition()
				.getMeasurementSpecifications()) {
			SelectionTreeItem item = new SelectionTreeItem(ms.getName());
			root.addItem(item);
			item.addStyleName(TREE_ITEM_SPECIFICATION_CSS);
			item.addValueChangeHandler(this);

			treeItems.put(ms.getName(), new HashMap<String, SelectionTreeItem>());

			for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {
				SelectionTreeItem xItem = new SelectionTreeItem(esd.getName());
				item.addItem(xItem);
				xItem.addStyleName(TREE_ITEM_EXPERIMENT_CSS);
				xItem.addValueChangeHandler(this);

				treeItems.get(ms.getName()).put(esd.getName(), xItem);
			}
		}
		tree.setRoot(root);

		Metering.stop(metering);
	}

	public Map<String, Map<String, SelectionTreeItem>> getTreeItems() {
		return treeItems;
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		SelectionTreeItem item = (SelectionTreeItem) event.getSource();
		if (item.getParentItem() == root) {
			for (TreeItem i : item.getChildrenItems()) {
				((SelectionTreeItem) i).getCheckBox().setValue(event.getValue());
			}
		} else {
			for (TreeItem i : item.getParentItem().getChildrenItems()) {
				if (((SelectionTreeItem) i).getCheckBox().getValue()) {
					((SelectionTreeItem) i.getParentItem()).getCheckBox().setValue(true);
					return;
				}
			}
			((SelectionTreeItem) item.getParentItem()).getCheckBox().setValue(false);
		}
	}
}
