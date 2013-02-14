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
package org.sopeco.webui.client.layout.center.result;

import java.util.List;

import org.sopeco.webui.client.layout.center.CenterPanel;
import org.sopeco.webui.client.layout.center.ICenterController;
import org.sopeco.webui.client.layout.dialog.ExportCsvDialog;
import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.client.widget.TreeItem;
import org.sopeco.webui.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.webui.shared.definitions.result.SharedExperimentSeries;
import org.sopeco.webui.shared.definitions.result.SharedScenarioInstance;

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
		reload();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void onSwitchTo() {	
		refreshTree();
	}
	
	@Override
	public void reload() {
		view = new ResultView();
	}

	@Override
	public void onClick(ClickEvent event) {
	 if (event.getSource() instanceof TreeLeaf) {
			SharedExperimentRuns run = ((TreeLeaf) event.getSource()).getExperimentRun();

			StringBuffer param = new StringBuffer();
			
			String timestamp = ""+run.getTimestamp();
			 String experimentName = run.getParentSeries().getExperimentName();
			 String controllerURL = run.getParentSeries().getParentInstance().getControllerUrl();
			 String scenarioName = run.getParentSeries().getParentInstance().getScenarioName();
			
//			param.append(run.getTimestamp());
//			param.append("|");
//			param.append(run.getParentSeries().getExperimentName());
//			param.append("|");
//			param.append(run.getParentSeries().getParentInstance().getControllerUrl());
//			param.append("|");
//			param.append(run.getParentSeries().getParentInstance().getScenarioName());
			
			//downloadUrl += "?param=" + Base64.encodeString(param.toString());

			//Window.open(downloadUrl, "_blank", "");
//			ExportCsvDialog.show(param.toString());
			 
			 ExportCsvDialog.show(timestamp, experimentName, controllerURL, scenarioName);
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
