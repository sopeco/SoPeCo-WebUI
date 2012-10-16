package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.widget.tree.TreeItem;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

import com.google.gwt.core.client.GWT;
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

		generateTree();
	}

	public void updateTree() {
		generateTree();
	}

	private void generateTree() {
		ParameterNamespace root = ScenarioManager.get().getBuilder().getMEDefinition().getRoot();

		TreeItem rootItem = new TreeItem(root.getName());

		recursiveAddTreeItems(root, rootItem);

		view.getTree().setRoot(rootItem);
	}

	private void recursiveAddTreeItems(ParameterNamespace namespace, TreeItem treeItem) {
		for (ParameterNamespace pns : namespace.getChildren()) {
			TreeItem newItem = new TreeItem(pns.getName());
			GWT.log("new item");
			
			for (ParameterDefinition parameter : pns.getParameters()) {
				SelectionTreeItemLeaf leaf = new SelectionTreeItemLeaf(parameter.getName());
				// EParameterTreeItem pItem = new
				// EParameterTreeItem(parameter.getName(), parameter.getType()
				// .toUpperCase(), parameter.getRole());
				//
				// pItem.removeActionPanel();
				// newItem.addItem(pItem);
				newItem.add(leaf);
			}

			if (pns.getChildren().size() > 0) {
				recursiveAddTreeItems(pns, newItem);
			}

			treeItem.addItem(newItem);
		}
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
