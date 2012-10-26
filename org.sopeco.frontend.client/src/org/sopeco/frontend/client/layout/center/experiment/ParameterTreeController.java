package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.event.EnvironmentDefinitionChangedEvent;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.EnvironmentDefinitionChangedEventHandler;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.helper.EnvironmentFilter;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.widget.tree.Tree;
import org.sopeco.frontend.client.widget.tree.TreeItem;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ParameterTreeController {

	/** temporarily. */
	public static ParameterTreeController treeController;

	private ParameterTreeView view;
	private Tree tree;

	private EnvironmentFilter environmentFilter;

	public ParameterTreeController() {
		initialize();

		treeController = this;
	}

	/**
	 * Init
	 */
	private void initialize() {
		tree = new Tree();

		environmentFilter = new EnvironmentFilter();
		environmentFilter.setFilterObservation(true);
		environmentFilter.setFilterEmptyNamespaces(true);

		updateTree();

		getView().add(tree);

		EventControl.get().addHandler(EnvironmentDefinitionChangedEvent.TYPE,
				new EnvironmentDefinitionChangedEventHandler() {
					@Override
					public void onEnvironmentChangedEvent(EnvironmentDefinitionChangedEvent event) {
						updateTree();
					}
				});

		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				updateTree();
			}
		});
	}

	/**
	 * Returns the view with the Tree.
	 * 
	 * @return ParameterTreeView
	 */
	public ParameterTreeView getView() {
		if (view == null) {
			view = new ParameterTreeView();
		}
		return view;
	}

	/**
	 * Updates the tree with the current model stored in the Scenariomanager.
	 */
	private void updateTree() {
		MeasurementEnvironmentDefinition treeData = ScenarioManager.get().getBuilder().getMEDefinition();

		generateTree(environmentFilter.filter(treeData));
	}

	/**
	 * Generates a new Tree.
	 */
	private void generateTree(MeasurementEnvironmentDefinition treeData) {
		double metering = Metering.start();

		ParameterNamespace root = treeData.getRoot();
		TreeItem rootItem = new TreeItem(root.getName());

		recursiveAddTreeItems(root, rootItem);

		tree.setRoot(rootItem);

		Metering.stop(metering);
	}

	/**
	 * 
	 * @param namespace
	 * @param treeItem
	 * @return
	 */
	private void recursiveAddTreeItems(ParameterNamespace namespace, TreeItem treeItem) {
		for (ParameterNamespace pns : namespace.getChildren()) {
			TreeItem newItem = new TreeItem(pns.getName());

			for (ParameterDefinition parameter : pns.getParameters()) {
				boolean isPreperation = ScenarioManager.get().experiment().isPreperationAssignment(parameter);
				boolean isExperiment = ScenarioManager.get().experiment().isExperimentAssignment(parameter);

				ParameterTreeItemLeaf leaf = new ParameterTreeItemLeaf(parameter, isPreperation, isExperiment);
				newItem.addItem(leaf);
			}

			recursiveAddTreeItems(pns, newItem);

			treeItem.addItem(newItem);

		}
	}

	/**
	 * 
	 */
	public boolean showView() {
		if (!view.isVisible()) {
			view.setVisible(true);
			return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void hideView() {
		view.setVisible(false);
	}
}
