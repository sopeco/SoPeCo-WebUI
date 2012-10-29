package org.sopeco.frontend.client.layout.center.environment;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.client.widget.EParameterTreeItem;
import org.sopeco.frontend.client.widget.EnvironmentTreeItem;
import org.sopeco.frontend.client.widget.FrontendTree;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
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
		clear();

		generateTree(false);
		add(frontendTree);
	}

	public void generateTree(boolean forceUpdate) {
		double metering = Metering.start();
		frontendTree = new FrontendTree();

		if (currentEnvironmentDefinition == null || forceUpdate) {
			RPC.getMEControllerRPC().getCurrentMEDefinition(new AsyncCallback<MeasurementEnvironmentDefinition>() {
				@Override
				public void onSuccess(MeasurementEnvironmentDefinition result) {
					if (result == null) {
						Message.warning("null");
					} else {
						setEnvironmentDefiniton(result);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					Message.error(caught.getMessage());
				}
			});

			Metering.stop(metering);
			return;
		}

		ParameterNamespace root = currentEnvironmentDefinition.getRoot();

		EnvironmentTreeItem rootItem = new EnvironmentTreeItem(root.getName());

		addPNS(root, rootItem);

		frontendTree.setRoot(rootItem);

		Metering.stop(metering);
	}

	private void addPNS(ParameterNamespace namespace, EnvironmentTreeItem nsTItem) {
		double metering = Metering.start();

		for (ParameterNamespace pns : namespace.getChildren()) {
			EnvironmentTreeItem treeItem = new EnvironmentTreeItem(pns.getName());

			for (ParameterDefinition parameter : pns.getParameters()) {
				EParameterTreeItem pItem = new EParameterTreeItem(parameter.getName(), parameter.getType()
						.toUpperCase(), parameter.getRole());

				treeItem.addItem(pItem);
			}

			if (pns.getChildren().size() > 0) {
				addPNS(pns, treeItem);
			}

			nsTItem.addItem(treeItem);
		}

		Metering.stop(metering);
	}

	public void setEnvironmentDefiniton(MeasurementEnvironmentDefinition newEnvironmentDefinition) {
		currentEnvironmentDefinition = newEnvironmentDefinition;

		updateTree();
	}
}
