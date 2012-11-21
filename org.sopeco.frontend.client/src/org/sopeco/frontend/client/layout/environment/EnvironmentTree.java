package org.sopeco.frontend.client.layout.environment;

/**
 * 
 */
import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.event.EnvironmentDefinitionChangedEvent;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.EnvironmentDefinitionChangedEventHandler;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.gwt.widgets.tree.TreeItem;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;

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

	public EnvironmentTree(boolean hasTwoCheckboxes) {
		view = new TreeView(hasTwoCheckboxes);
		view.getTbtnObservationButton().addClickHandler(this);
		view.getTbtnInitialAssignments().addClickHandler(this);

		twoCheckboxes = hasTwoCheckboxes;

		allTreeItemsList = new ArrayList<EnvTreeItem>();
		observationItemsList = new ArrayList<TreeItem>();
		treeNodesList = new ArrayList<TreeItem>();

		generateTree();

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				GWT.log("event - SpecificationChangedEvent");
				generateTree();
			}
		});

		EventControl.get().addHandler(EnvironmentDefinitionChangedEvent.TYPE,
				new EnvironmentDefinitionChangedEventHandler() {
					@Override
					public void onEnvironmentChangedEvent(EnvironmentDefinitionChangedEvent event) {
						GWT.log("event - EnvironmentDefinitionChangedEvent");
						generateTree();
					}
				});
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
				boolean isIA = ScenarioManager.get().getBuilder().getSpecificationBuilder()
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
	protected void generateTree() {
		double metering = Metering.start();

		ParameterNamespace root = ScenarioManager.get().getBuilder().getMEDefinition().getRoot();

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
