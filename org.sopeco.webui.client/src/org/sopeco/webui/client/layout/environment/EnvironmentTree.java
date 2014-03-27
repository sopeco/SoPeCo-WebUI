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
package org.sopeco.webui.client.layout.environment;

/**
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.webui.client.event.EventControl;
import org.sopeco.webui.client.event.InitialAssignmentChangedEvent;
import org.sopeco.webui.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.widget.TreeItem;
import org.sopeco.webui.shared.helper.Metering;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class EnvironmentTree implements ClickHandler, ValueChangeHandler<Boolean> {

	private TreeView view;

	private List<EnvTreeItem> allTreeItemsList;
	private List<TreeItem> observationItemsList;
	private List<TreeItem> treeNodesList;

	private boolean twoCheckboxes;

	/** */
	public enum ECheckBox {
		/** */
		FIRST, SECOND
	}

	public EnvironmentTree(boolean hasTwoCheckboxes, String headline) {
		view = new TreeView(hasTwoCheckboxes, headline);
		view.getTbtnObservationButton().addClickHandler(this);
		view.getTbtnInitialAssignments().addClickHandler(this);

		twoCheckboxes = hasTwoCheckboxes;

		allTreeItemsList = new ArrayList<EnvTreeItem>();
		observationItemsList = new ArrayList<TreeItem>();
		treeNodesList = new ArrayList<TreeItem>();

		generateTree();
	}

	/**
	 * 
	 * @return
	 */
	public TreeView getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getTbtnObservationButton()) {
			updateVisibility();
		} else if (event.getSource() == view.getTbtnInitialAssignments()) {
			updateVisibility();
		}
	}

	private void updateVisibility() {
		boolean showInitAssignments = view.getTbtnInitialAssignments().isDown();
		boolean showObservation = view.getTbtnObservationButton().isDown();

		for (TreeItem x : allTreeItemsList) {
			x.setVisible(true);
		}

		if (!showObservation) {
			for (TreeItem x : observationItemsList) {
				x.setVisible(false);
			}
		}

		if (!showInitAssignments) {
			for (EnvTreeItem x : allTreeItemsList) {
				boolean isIA = ScenarioManager.get().getScenarioDefinitionBuilder().getSpecificationBuilder()
						.containsInitialAssignment(x.getParameter());

				if (isIA) {
					x.setVisible(false);
				}
			}
		}

		updateNodesVisibility();
	}

	private void updateNodesVisibility() {
		for (TreeItem n : treeNodesList) {
			n.setVisible(childrenAreVisible(n));
		}
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private boolean childrenAreVisible(TreeItem item) {
		for (TreeItem x : item.getChildrenItems()) {
			if (x.isVisible()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		EnvTreeItem item = (EnvTreeItem) event.getSource();

		ChangeType type;
		if (event.getValue()) {
			type = ChangeType.Added;
		} else {
			type = ChangeType.Removed;
		}

		GWT.log("add: " + item.getPath());
		InitialAssignmentChangedEvent changeEvent = new InitialAssignmentChangedEvent(item.getPath(), type);
		EventControl.get().fireEvent(changeEvent);
	}

	/**
	 * 
	 */
	public void generateTree() {
		double metering = Metering.start();

		ParameterNamespace root = ScenarioManager.get().getScenarioDefinitionBuilder().getMEDefinition().getRoot();

		for (ParameterDefinition pd : root.getAllParameters()) {
			GWT.log("2: " + pd.getFullName());
		}
		 
		TreeItem rootItem = new TreeItem("", true);

		recursiveAddTreeItems(root, rootItem);

		view.getTree().setRoot(rootItem);

		updateVisibility();

		Metering.stop(metering);
	}

	private boolean recursiveAddTreeItems(ParameterNamespace namespace, TreeItem treeItem) {
		boolean hasChildrenWithLeafs = false;
		for (ParameterNamespace pns : namespace.getChildren()) {
			boolean addedLeafs = false;
			TreeItem newItem = new TreeItem(pns.getName());

			for (ParameterDefinition parameter : pns.getParameters()) {
				boolean firstChecked = isFirstChecked(parameter);
				boolean secondChecked = isSecondChecked(parameter);

				EnvTreeItem leaf = new EnvTreeItem(this, parameter, twoCheckboxes);
				leaf.setFirstCheckboxValue(firstChecked);
				leaf.setSecondCheckboxValue(secondChecked);

				newItem.addItem(leaf);
				allTreeItemsList.add(leaf);

				if (parameter.getRole() == ParameterRole.OBSERVATION) {
					leaf.setCheckboxesEnable(false);
					observationItemsList.add(leaf);
				}

				addedLeafs = true;
			}

			if (pns.getChildren().size() > 0) {
				boolean temp = recursiveAddTreeItems(pns, newItem);
				if (temp) {
					addedLeafs = true;
				}
			}

			if (addedLeafs) {
				hasChildrenWithLeafs = true;
				treeItem.addItem(newItem);
				treeNodesList.add(newItem);
			}
		}

		return hasChildrenWithLeafs;
	}

	public abstract boolean isFirstChecked(ParameterDefinition parameter);

	public abstract boolean isSecondChecked(ParameterDefinition parameter);

	public abstract void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value);
}
