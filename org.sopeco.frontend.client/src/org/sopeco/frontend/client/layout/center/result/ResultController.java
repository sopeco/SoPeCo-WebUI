package org.sopeco.frontend.client.layout.center.result;

import java.util.List;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.dialog.ExportCsvDialog;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentSeries;
import org.sopeco.frontend.shared.definitions.result.SharedScenarioInstance;
import org.sopeco.gwt.widgets.tree.TreeItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ResultController implements ICenterController, ClickHandler {

	private ResultView view;
	private List<SharedScenarioInstance> currentScenarioInstanceList;

	public ResultController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void onSwitchTo() {	
	}
	
	@Override
	public void reset() {
		view = new ResultView();

		view.getBtnTest().addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getBtnTest()) {
			refreshTree();
		} else if (event.getSource() instanceof TreeLeaf) {
			SharedExperimentRuns run = ((TreeLeaf) event.getSource()).getExperimentRun();

			StringBuffer param = new StringBuffer();
			
			param.append(run.getTimestamp());
			param.append("|");
			param.append(run.getParentSeries().getExperimentName());
			param.append("|");
			param.append(run.getParentSeries().getParentInstance().getControllerUrl());
			param.append("|");
			param.append(run.getParentSeries().getParentInstance().getScenarioName());
			
			//downloadUrl += "?param=" + Base64.encodeString(param.toString());

			//Window.open(downloadUrl, "_blank", "");
			ExportCsvDialog.show(param.toString());
		}
	}

	/**
	 * 
	 */
	private void refreshTree() {
		RPC.getResultRPC().getInstances(ScenarioManager.get().getCurrentScenarioName(),
				new AsyncCallback<List<SharedScenarioInstance>>() {
					@Override
					public void onSuccess(List<SharedScenarioInstance> result) {
						fillTree(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Message.error(caught.getMessage());
					}
				});
	}

	/**
	 * 
	 */
	private void fillTree(List<SharedScenarioInstance> newList) {
		currentScenarioInstanceList = newList;

		TreeItem root = new TreeItem("Results");

		for (SharedScenarioInstance instance : currentScenarioInstanceList) {
			TreeItem instanceItem = new TreeItem("Controller: " + instance.getControllerUrl());

			for (SharedExperimentSeries series : instance.getExperimentSeries()) {
				TreeItem seriesItem = new TreeItem(series.getExperimentName());

				for (SharedExperimentRuns run : series.getExperimentRuns()) {
					TreeLeaf runItem = new TreeLeaf(run);

//					runItem.addClickHandler(this);

					seriesItem.addItem(runItem);
				}
				instanceItem.addItem(seriesItem);
			}
			root.addItem(instanceItem);
		}
		view.getTree().setRoot(root);
	}
}
