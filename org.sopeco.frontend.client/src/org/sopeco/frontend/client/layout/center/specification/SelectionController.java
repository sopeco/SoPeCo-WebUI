package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.widget.tree.TreeItem;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectionController {

	private SelectionView view;

	public SelectionController() {
		reset();
	}

	/**
	 * Init all necessary objects.
	 */
	public void reset() {
		view = new SelectionView();

		// generateTree();
		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				generateTree();
			}
		});
	}

	// public void updateTree() {
	// generateTree();
	// }

	private void generateTree() {
		double metering = Metering.start();

		ParameterNamespace root = ScenarioManager.get().getBuilder().getMEDefinition().getRoot();

		TreeItem rootItem = new TreeItem(root.getName());

		recursiveAddTreeItems(root, rootItem);

		view.getTree().setRoot(rootItem);

		Metering.stop(metering);
	}

	private boolean recursiveAddTreeItems(ParameterNamespace namespace, TreeItem treeItem) {
		boolean hasChildrenWithLeafs = false;
		for (ParameterNamespace pns : namespace.getChildren()) {
			boolean addedLeafs = false;
			TreeItem newItem = new TreeItem(pns.getName());

			for (ParameterDefinition parameter : pns.getParameters()) {
				boolean isInitAssignment = ScenarioManager.get().getBuilder().getSpecificationBuilder()
						.containsInitialAssignment(parameter);

				SelectionTreeItemLeaf leaf = new SelectionTreeItemLeaf(parameter.getName(), isInitAssignment);
				newItem.addItem(leaf);

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
			}
		}

		return hasChildrenWithLeafs;
	}

	/**
	 * Returns the selection view.
	 * 
	 * @return SelectionView
	 */
	public Widget getView() {
		return view;
	}
}
