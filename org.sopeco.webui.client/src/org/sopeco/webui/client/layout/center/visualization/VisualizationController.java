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
package org.sopeco.webui.client.layout.center.visualization;

import org.sopeco.gwt.widgets.ImageHover;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.layout.center.ICenterController;
import org.sopeco.webui.client.layout.center.result.ResultController;
import org.sopeco.webui.client.layout.center.visualization.wizard.ChartSelectionPanel;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.rpc.RPC;

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
	private static final String STATUS_READY_IMAGE = "images/status-green.png";
	private static final String STATUS_UNKOWN_IMAGE = "images/status-gray.png";

	private DockLayoutPanel rootWidget;
	private FlowPanel controlWidget;
	private CellList<Visualization> visualizationList;
	private Anchor chartLink;
	private ChartWidget chartWidget;
	private Image statusImage;
	private Status status = Status.UNKNOWN;
	private SingleSelectionModel<Visualization> visualizationSelection;
	private SimplePager pager;

	private ChartsDataProvider chartDataProvider;
	public static final ProvidesKey<Visualization> KEY_PROVIDER = new ProvidesKey<Visualization>() {
		@Override
		public Object getKey(Visualization item) {
			return item == null ? null : item.getId();
		}
	};

	public VisualizationController() {
		R.css.visualizationViewCss().ensureInjected();
		rootWidget = new DockLayoutPanel(Unit.EM);
		controlWidget = new FlowPanel();
		visualizationSelection = new SingleSelectionModel<Visualization>(KEY_PROVIDER);
		chartDataProvider = new ChartsDataProvider(this);
		controlWidget.addStyleName("visualizationControl");
		statusImage = new Image(STATUS_UNKOWN_IMAGE);
		statusImage.setUrl(STATUS_READY_IMAGE);
		statusImage.getElement().getStyle().setMarginLeft(1, Unit.EM);
		statusImage.getElement().getStyle().setMarginTop(1, Unit.EM);
		controlWidget.add(statusImage);
		Image addVisualization = new ImageHover(R.img.icoAdd(), R.img.icoAddHover());
		addVisualization.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MainLayoutPanel.get().switchView(ResultController.class);
			}
		});
		addVisualization.setTitle(R.lang.newChart());
		addVisualization.getElement().getStyle().setMarginLeft(1, Unit.EM);
		addVisualization.getElement().getStyle().setMarginTop(1, Unit.EM);
		addVisualization.getElement().getStyle().setCursor(Cursor.POINTER);
		controlWidget.add(addVisualization);

		Image remove = new ImageHover(R.img.icoTrash(), R.img.icoTrashHover());
		remove.setTitle(R.lang.removeChart());
		remove.getElement().getStyle().setMarginLeft(1, Unit.EM);
		remove.getElement().getStyle().setMarginTop(1, Unit.EM);
		remove.getElement().getStyle().setCursor(Cursor.POINTER);
		remove.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RPC.getVisualizationRPC().deleteVisualization(visualizationSelection.getSelectedObject(), new AsyncCallback<Void>() {

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

		Image refresh = new Image(R.img.iconSet().getSafeUri(), 120, 150, 16, 15);
		refresh.setTitle(R.lang.refreshCharts());
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
		visualizationList = new CellList<Visualization>(new VisualizationCell(), KEY_PROVIDER);
		visualizationList.setPageSize(8);
		visualizationList.setEmptyListWidget(new Label(
				"Either the visualizations are still loading or there are no visualizations yet."));
		visualizationSelection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				chartWidget.switchChart(visualizationSelection.getSelectedObject());
				chartLink.setHref(visualizationSelection.getSelectedObject().getLink());
				chartLink.setText(visualizationSelection.getSelectedObject().getLink());

			}
		});
		visualizationList.setSelectionModel(visualizationSelection);
		visualizationList.addStyleName("visualizationList");
		chartDataProvider.addDataDisplay(visualizationList);
		visualizationList.setWidth("300px");
		rootWidget.addNorth(controlWidget, 3);
		FlowPanel listPanel = new FlowPanel();
		listPanel.addStyleName("visualizationPanel");
		pager = new SimplePager(TextLocation.CENTER);
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

	public void setStatus(Status status) {
		this.status = status;
		switch (status) {
		case READY:
			statusImage.setUrlAndVisibleRect(R.img.iconSet().getSafeUri(), 60, 150, 10, 10);
			break;
		case BUSY:
			statusImage.setUrlAndVisibleRect(R.img.iconSet().getSafeUri(), 30, 150, 10, 10);
			break;
		case LOADING:
			statusImage.setUrlAndVisibleRect(R.img.iconSet().getSafeUri(), 90, 150, 10, 10);
			break;
		default:
			statusImage.setUrlAndVisibleRect(R.img.iconSet().getSafeUri(), 0, 150, 10, 10);
		}
	}

	public Status getStatus() {
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

	public void refreshVisualizations() {
		if (getStatus() != Status.BUSY) {
			setStatus(Status.LOADING);
		}
		Range range = visualizationList.getVisibleRange();
		visualizationList.setVisibleRangeAndClearData(range, true);
	}
	
	public void refreshVisualizationsAndSelect(final Visualization visualization) {
		if (getStatus() != Status.BUSY) {
			setStatus(Status.LOADING);
		}
		Range range = visualizationList.getVisibleRange();
		visualizationList.setVisibleRangeAndClearData(range, true);
		pager.setPage(pager.getPageCount()-1);
		visualizationSelection.setSelected(visualization, true);
	}

	private static class VisualizationCell extends AbstractCell<Visualization> {

		public VisualizationCell() {
		}

		@Override
		public void render(Context context, Visualization value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}

			sb.appendHtmlConstant("<table>");

			sb.appendHtmlConstant("<tr><td rowspan='3'>");
			switch(value.getOptions().getType()){
			case BARCHART:
				sb.appendHtmlConstant(ChartSelectionPanel.BAR_CHART_IMAGE.toString());
				break;
			case PIECHART:
				sb.appendHtmlConstant(ChartSelectionPanel.PIE_CHART_IMAGE.toString());
				break;
			case LINECHART:
				sb.appendHtmlConstant(ChartSelectionPanel.LINE_CHART_IMAGE.toString());
				break;
			}
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
