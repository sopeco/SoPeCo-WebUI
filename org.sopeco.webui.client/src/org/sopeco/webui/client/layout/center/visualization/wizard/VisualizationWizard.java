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
package org.sopeco.webui.client.layout.center.visualization.wizard;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.gwt.widgets.Headline;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.layout.center.visualization.VisualizationController;
import org.sopeco.webui.client.layout.center.visualization.VisualizationController.Status;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.webui.shared.entities.ChartOptions;
import org.sopeco.webui.shared.entities.ChartParameter;
import org.sopeco.webui.shared.entities.Visualization;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisualizationWizard extends DialogBox {
	
	private VerticalPanel rootWidget;
	private Button create;
	private SharedExperimentRuns experimentRun;
	private HorizontalPanel buttonPanel;
	private List<ChartParameter> inputParameter;
	private List<ChartParameter> outputParameter;
	private final ChartSelectionPanel chartSelectionPanel = new ChartSelectionPanel();
	private final ExtensionPanel extensionPanel = new ExtensionPanel();
	final ColumnSelectionPanel columnSelectionPanel = new ColumnSelectionPanel(this);

	public VisualizationWizard(final SharedExperimentRuns experimentRun) {
		super(true);
		R.css.visualizationWizardCss().ensureInjected();
		this.experimentRun = experimentRun;
		inputParameter = new ArrayList<ChartParameter>();
		outputParameter = new ArrayList<ChartParameter>();
		rootWidget = new VerticalPanel();
		rootWidget.add(new Headline(R.lang.newChart()));
		extensionPanel.addEventPartner(this);
		rootWidget.add(extensionPanel);
		rootWidget.setCellHorizontalAlignment(extensionPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		rootWidget.add(chartSelectionPanel);
		rootWidget.add(columnSelectionPanel);
		columnSelectionPanel.setVisible(false);
		buttonPanel = new HorizontalPanel();
		Button close = new Button(R.lang.cancel());
		close.getElement().getStyle().setMarginRight(1, Unit.EM);
		close.getElement().getStyle().setMarginBottom(1, Unit.EM);
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VisualizationWizard.this.hide();
			}
		});
		buttonPanel.add(close);
		create = new Button(R.lang.create());
		create.getElement().getStyle().setMarginRight(1, Unit.EM);
		create.getElement().getStyle().setMarginBottom(1, Unit.EM);
		create.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createChart();
			}
		});
		buttonPanel.add(create);
		create.setEnabled(false);
		rootWidget.add(buttonPanel);
		rootWidget.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		this.setWidget(rootWidget);
		chartSelectionPanel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				columnSelectionPanel.setVisible(true);
				create.setEnabled(true);
			}
		});
		loadInfos();
		center();
	}
	
	private void loadInfos(){
		RPC.getVisualizationRPC().getExtensions(new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not load extensions.",caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				extensionPanel.setExtensions(result);
			}
		});
		RPC.getVisualizationRPC().getChartParameter(experimentRun, new AsyncCallback<ChartParameter[]>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Could not load chart parameter.",caught);
			}

			@Override
			public void onSuccess(ChartParameter[] result) {
				for (ChartParameter param: result){
					if (param.getType() == ChartParameter.INPUT){
						inputParameter.add(param);
					}
					else {
						outputParameter.add(param);
					}
				}
				columnSelectionPanel.setChartParameter(inputParameter, outputParameter, VisualizationWizard.this);
			}
		});
	}

	private void createChart() {
		MainLayoutPanel.get().switchView(VisualizationController.class);
		MainLayoutPanel.get().getController(VisualizationController.class).setStatus(Status.BUSY);
		VisualizationWizard.this.hide();
		ChartOptions options = new ChartOptions();
		options.setType(chartSelectionPanel.getSelectedType());
		RPC.getVisualizationRPC().createVisualization(experimentRun, columnSelectionPanel.getSelectedInput(), columnSelectionPanel.getSelectedOutput(), options, extensionPanel.getExtension(), new AsyncCallback<Visualization>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("Chart could not be created.",caught);
			}

			@Override
			public void onSuccess(Visualization result) {
				MainLayoutPanel.get().getController(VisualizationController.class).setStatus(Status.LOADING);
				MainLayoutPanel.get().getController(VisualizationController.class).refreshVisualizationsAndSelect(result);
			}
		});
	}
}
