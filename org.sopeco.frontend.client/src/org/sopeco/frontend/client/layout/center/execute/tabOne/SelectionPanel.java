package org.sopeco.frontend.client.layout.center.execute.tabOne;

import org.sopeco.frontend.client.model.ScenarioManager;
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

	public SelectionPanel() {
		init();
	}

	private void init() {
		addStyleName(CSS_CLASS);

		tree = new Tree();

		headline = new Headline("Executed ExperimentSeries");
		add(headline);
		add(tree);
	}

	public void generateTree() {
		double metering = Metering.start();

		root = new TreeItem("", true);
		for (MeasurementSpecification ms : ScenarioManager.get().getCurrentScenarioDefinition()
				.getMeasurementSpecifications()) {
			SelectionTreeItem item = new SelectionTreeItem(ms.getName());
			root.addItem(item);
			item.addStyleName(TREE_ITEM_SPECIFICATION_CSS);
			item.addValueChangeHandler(this);

			for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {
				SelectionTreeItem xItem = new SelectionTreeItem(esd.getName());
				item.addItem(xItem);
				xItem.addStyleName(TREE_ITEM_EXPERIMENT_CSS);
				xItem.addValueChangeHandler(this);
			}
		}
		tree.setRoot(root);

		Metering.stop(metering);
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
