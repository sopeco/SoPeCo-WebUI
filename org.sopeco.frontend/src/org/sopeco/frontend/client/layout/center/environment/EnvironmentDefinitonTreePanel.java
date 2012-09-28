package org.sopeco.frontend.client.layout.center.environment;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.client.widget.EnvironmentTreeItem;
import org.sopeco.frontend.client.widget.FrontendTree;
import org.sopeco.frontend.client.widget.FrontendTreeItem;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentDefinitonTreePanel extends FlowPanel {

	private FrontendTree frontendTree;
	private MeasurementEnvironmentDefinition currentEnvironmentDefinition;

	public EnvironmentDefinitonTreePanel() {
		initialize();
	}

	private void initialize() {
		getElement().setId("medTreePanel");

		updateTree();
	}

	private void updateTree() {
		if (frontendTree != null) {
			remove(frontendTree);
		}
		generateTree();
		add(frontendTree);
	}

	private void generateTree() {
		frontendTree = new FrontendTree();

		if (currentEnvironmentDefinition == null) {
			RPC.getMEControllerRPC().getBlankMEDefinition(new AsyncCallback<MeasurementEnvironmentDefinition>() {
				@Override
				public void onSuccess(MeasurementEnvironmentDefinition result) {
					setEnvironmentDefiniton(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					Message.error(caught.getMessage());
				}
			});
			return;
		}

		ParameterNamespace root = currentEnvironmentDefinition.getRoot();

		EnvironmentTreeItem rootItem = new EnvironmentTreeItem(root.getName());

		addPNS(root, rootItem);

		frontendTree.setRoot(rootItem);
	}

	private void addPNS(ParameterNamespace namespace, EnvironmentTreeItem nsTItem) {
		for (ParameterNamespace pns : namespace.getChildren()) {
			EnvironmentTreeItem treeItem = new EnvironmentTreeItem(pns.getName());
			nsTItem.addItem(treeItem);

			if (pns.getChildren().size() > 0) {
				addPNS(pns, treeItem);
			}
		}
	}

	public void setEnvironmentDefiniton(MeasurementEnvironmentDefinition newEnvironmentDefinition) {
		currentEnvironmentDefinition = newEnvironmentDefinition;

		updateTree();
	}
}
