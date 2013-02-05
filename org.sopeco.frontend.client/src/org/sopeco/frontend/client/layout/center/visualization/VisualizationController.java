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
package org.sopeco.frontend.client.layout.center.visualization;

import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class VisualizationController implements ICenterController {
	private static final String ADD_IMAGE = "images/add.png";
	private static final String REMOVE_IMAGE = "images/remove_cross.png";
	private static final String REFRESH_IMAGE = "images/refresh_small.png";
	private static final String STATUS_READY_IMAGE = "images/status-green.png";
	private static final String STATUS_LOADING_IMAGE = "images/status-yellow.png";
	private static final String STATUS_BUSY_IMAGE = "images/status-red.png";
	private static final String STATUS_UNKOWN_IMAGE = "images/status-gray.png";
	
	private DockLayoutPanel rootWidget;
	private FlowPanel controlWidget;
	private CellList<Visualization> visualizationList;
	private Anchor chartLink;
	private ChartWidget chartWidget;
	private Image statusImage;
	private Status status = Status.UNKNOWN;

	public static ChartsDataProvider visualizationDataProvider;
	public static final ProvidesKey<Visualization> KEY_PROVIDER = new ProvidesKey<Visualization>() {
		@Override
		public Object getKey(Visualization item) {
			return item == null ? null : item.getId();
		}
	};

	public VisualizationController() {
		FrontEndResources.loadVisualizationViewCSS();
		rootWidget = new DockLayoutPanel(Unit.EM);
		controlWidget = new FlowPanel();
		final SingleSelectionModel<Visualization> ssm = new SingleSelectionModel<Visualization>(KEY_PROVIDER);
		visualizationDataProvider = new ChartsDataProvider(this);
		controlWidget.addStyleName("visualizationControl");
		statusImage = new Image(STATUS_UNKOWN_IMAGE);
		statusImage.setUrl(STATUS_READY_IMAGE);
		statusImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		statusImage.getElement().getStyle().setMarginTop(1, Unit.EM);
		controlWidget.add(statusImage);
		Image addVisualization = new Image(ADD_IMAGE);
		addVisualization.setTitle(R.get("addchart"));
		addVisualization.getElement().getStyle().setMarginLeft(1, Unit.EM);
		addVisualization.getElement().getStyle().setMarginTop(1, Unit.EM);
		addVisualization.getElement().getStyle().setCursor(Cursor.POINTER);
		controlWidget.add(addVisualization);
		
		Image remove = new Image(REMOVE_IMAGE);
		remove.setTitle(R.get("removechart"));
		remove.getElement().getStyle().setMarginLeft(1, Unit.EM);
		remove.getElement().getStyle().setMarginTop(1, Unit.EM);
		remove.getElement().getStyle().setCursor(Cursor.POINTER);
		remove.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				RPC.getVisualizationRPC().deleteVisualization(ssm.getSelectedObject(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void result) {
						refreshVisualizations();
					}
				});
			}
		});
		controlWidget.add(remove);
		
		Image refresh = new Image(REFRESH_IMAGE);
		refresh.setTitle(R.get("refreshcharts"));
		refresh.getElement().getStyle().setMarginLeft(1, Unit.EM);
		refresh.getElement().getStyle().setMarginTop(1, Unit.EM);
		refresh.getElement().getStyle().setCursor(Cursor.POINTER);
		refresh.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				refreshVisualizations();
			}
		});
		controlWidget.add(refresh);
		
		chartLink = new Anchor("");
		controlWidget.add(chartLink);
		visualizationList = new CellList<Visualization>(new VisualizationCell(),KEY_PROVIDER);
		visualizationList.setPageSize(8);
		visualizationList
				.setEmptyListWidget(new Label(
						"Either the visualizations are still loading or there are no visualizations yet."));
		ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				chartWidget.switchChart(ssm.getSelectedObject());
				chartLink.setHref(ssm.getSelectedObject().getLink());
				chartLink.setText(ssm.getSelectedObject().getLink());

			}
		});
		visualizationList.setSelectionModel(ssm);
		visualizationList.addStyleName("visualizationList");
		visualizationDataProvider.addDataDisplay(visualizationList);
		visualizationList.setWidth("300px");
		rootWidget.addNorth(controlWidget, 3);
		FlowPanel listPanel = new FlowPanel();
		listPanel.addStyleName("visualizationPanel");
		SimplePager pager = new SimplePager(TextLocation.CENTER);
		listPanel.add(pager);
		pager.setPageSize(8);
		pager.getElement().getStyle().setWidth(24, Unit.EM);
		pager.setRangeLimited(true);
		pager.setDisplay(visualizationList);
		listPanel.add(visualizationList);
		rootWidget.addWest(listPanel, 25);
		chartWidget = new ChartWidget();
		rootWidget.add(chartWidget);
	}
	
	public void setStatus(Status status){
		this.status = status;
		switch (status){
		case READY:
			statusImage.setUrl(STATUS_READY_IMAGE);
			break;
		case BUSY:
			statusImage.setUrl(STATUS_BUSY_IMAGE);
			break;
		case LOADING:
			statusImage.setUrl(STATUS_LOADING_IMAGE);
			break;
		default:
			statusImage.setUrl(STATUS_UNKOWN_IMAGE);
		}
	}
	
	public Status getStatus(){
		return status;
	}

	@Override
	public Widget getView() {
		return rootWidget;
	}

	@Override
	public void reload() {
	}

	@Override
	public void onSwitchTo() {
		refreshVisualizations();
	}

	
	
	public void refreshVisualizations(){
		if (getStatus() != Status.BUSY){
			setStatus(Status.LOADING);
		}
		Range range = visualizationList.getVisibleRange();
		visualizationList.setVisibleRangeAndClearData(range, true);
	}

	private static class VisualizationCell extends AbstractCell<Visualization> {

		private static final String EXPERIMENT_IMAGE = "images/experiment.png";
		private Image image;

		public VisualizationCell() {
			image = new Image(EXPERIMENT_IMAGE);
		}

		@Override
		public void render(Context context, Visualization value,
				SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			sb.appendHtmlConstant("<table>");

			sb.appendHtmlConstant("<tr><td rowspan='3'>");
			sb.appendHtmlConstant(image.toString());
			sb.appendHtmlConstant("</td>");

			sb.appendHtmlConstant("<td style='font-size:95%;'>");
			sb.appendHtmlConstant("<div>" + value.getName() + "</div>");
			sb.appendHtmlConstant("</td></tr><tr><td>");
			sb.appendHtmlConstant("<div>" + value.getType() + "/" + value.getOptions().getType().toString());
			sb.appendHtmlConstant("</div>");
			sb.appendHtmlConstant("</td></tr><tr><td>");
			sb.appendHtmlConstant("<div>" + value.getId());
			sb.appendHtmlConstant("</div>");
			sb.appendHtmlConstant("</td></tr></table>");

		}

	}
	
	public static enum Status {
		READY, BUSY, LOADING, UNKNOWN;
	}
}
