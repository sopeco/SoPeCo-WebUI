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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartOptions.ChartType;
import org.sopeco.frontend.shared.entities.ChartParameter;
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
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

public class VisualizationController implements ICenterController {
	private static final String ADD_IMAGE = "images/add.png";
	private static final String REMOVE_IMAGE = "images/remove_cross.png";
	private static final String REFRESH_IMAGE = "images/refresh_small.png";
	
	private DockLayoutPanel rootWidget;
	private FlowPanel controlWidget;
	private CellList<Visualization> visualizationList;
	private Anchor chartLink;
	private FlowPanel centerPanel;
	private Widget chartWidget;

	public static ChartsDataProvider visualizationDataProvider = new ChartsDataProvider();
	public static final ProvidesKey<Visualization> KEY_PROVIDER = new ProvidesKey<Visualization>() {
		@Override
		public Object getKey(Visualization item) {
			return item == null ? null : item.getId();
		}
	};

	public VisualizationController() {
		FrontEndResources.loadVisualizationViewCSS();
		centerPanel = new FlowPanel();
		rootWidget = new DockLayoutPanel(Unit.EM);
		controlWidget = new FlowPanel();
		final SingleSelectionModel<Visualization> ssm = new SingleSelectionModel<Visualization>(KEY_PROVIDER);
		controlWidget.addStyleName("visualizationControl");
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
						visualizationDataProvider.updateRowCount(visualizationList.getRowCount(), false);
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
				createChart(ssm.getSelectedObject());
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
		rootWidget.add(centerPanel);
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

	public Widget createChart(final Visualization visualization) {
		if (visualization == null) {
			return null;
		}
		switch (visualization.getType()) {
		case GCHART:
			Runnable onLoadCallback = new Runnable() {
				public void run() {
					chartWidget = new FlowPanel();
					
					CoreChart chart = null;
					switch (visualization.getOptions().getType()){
					case BARCHART:
						chart = new ColumnChart(createTable(
								visualization.getData(),
								visualization.getChartParameters(), ChartType.BARCHART),
								createOptions(visualization.getOptions(),
										visualization.getName()));
						break;
					case PIECHART:
						chart = new PieChart(createTable(
								visualization.getData(),
								visualization.getChartParameters(), ChartType.PIECHART),
								createOptions(visualization.getOptions(),
										visualization.getName()));
						break;
					default:
					chart = new LineChart(createTable(
							visualization.getData(),
							visualization.getChartParameters(), ChartType.LINECHART),
							createOptions(visualization.getOptions(),
									visualization.getName()));
					}
					((FlowPanel) chartWidget).add(chart);
					centerPanel.clear();
					centerPanel.add(chartWidget);
				}
			};

			// Load the visualization api, passing the onLoadCallback to be
			// called
			// when loading is done.
			VisualizationUtils.loadVisualizationApi(onLoadCallback,
					CoreChart.PACKAGE);

			break;
		default:
			chartWidget = new Frame(visualization.getLink());
			chartWidget.getElement().getStyle().setWidth(100, Unit.PCT);
			chartWidget.getElement().getStyle().setHeight(100, Unit.PCT);
			chartWidget.getElement().getStyle().setBorderWidth(0, Unit.EM);
			centerPanel.clear();
			centerPanel.add(chartWidget);
		}
		return chartWidget;
	}

	private Options createOptions(ChartOptions chartOptions, String name) {
		Options options = Options.create();
		switch(chartOptions.getType()){
		case BARCHART:
			options.set("bar.groupWidth", "50");
			break;
		case PIECHART:
			options.set("is3D", true);
			break;
		case LINECHART:
			options.setLineWidth(0);
			options.setPointSize(3);
			break;
		}
		options.setWidth(900);
		options.setHeight(500);
		options.setTitle(name);
		return options;
	}

	private AbstractDataTable createTable(Double[][] data,
			List<ChartParameter> chartParameters, ChartType type) {
		DataTable dataTable = DataTable.create();
		switch (type) {
		case PIECHART:
			dataTable.addColumn(ColumnType.STRING,
					chartParameters.get(0).getParameterName());
			Map<String, Double> values = new TreeMap<String, Double>();
			for (int row = 0; row < data[0].length; row++) {
				values.put(""+data[0][row], 0.0);
			}
			dataTable.addRows(values.size());
			int j = 0;
			for (String key : values.keySet()){
				dataTable.setValue(j, 0, chartParameters.get(0).getParameterName() + " = "+key);
				j++;
			}
			
			for (int col = 1; col < data.length; col++) {
				dataTable.addColumn(ColumnType.NUMBER,
						chartParameters.get(0).getParameterName());
				for (String key : values.keySet()){
					values.put(key, 0.0);
				}
				for (int row = 0; row < data[col].length; row++) {
					values.put(""+data[0][row], values.get(""+data[0][row])+data[col][row]);
				}
				j = 0;
				for (Double value : values.values()){
					dataTable.setValue(j, col, value);
					j++;
				}
			}
			break;
		case BARCHART:
			dataTable.addColumn(ColumnType.STRING,
					chartParameters.get(0).getParameterName());
			Map<String, Double> values2 = new TreeMap<String, Double>();
			for (int row = 0; row < data[0].length; row++) {
				values2.put(""+data[0][row], 0.0);
			}
			dataTable.addRows(values2.size());
			j = 0;
			for (String key : values2.keySet()){
				dataTable.setValue(j, 0, key);
				j++;
			}
			
			for (int col = 1; col < data.length; col++) {
				dataTable.addColumn(ColumnType.NUMBER,
						chartParameters.get(0).getParameterName());
				for (String key : values2.keySet()){
					values2.put(key, 0.0);
				}
				for (int row = 0; row < data[col].length; row++) {
					values2.put(""+data[0][row], values2.get(""+data[0][row])+data[col][row]);
				}
				j = 0;
				for (Double value : values2.values()){
					dataTable.setValue(j, col, value);
					j++;
				}
			}
			break;
		default:
			dataTable.addRows(data[0].length);
			dataTable.addColumn(ColumnType.NUMBER,
					chartParameters.get(0).getParameterName());
			for (int row = 0; row < data[0].length; row++) {
				dataTable.setValue(row, 0, data[0][row]);
			}
			for (int col = 1; col < data.length; col++) {
				dataTable.addColumn(ColumnType.NUMBER,
						chartParameters.get(col).getParameterName());
				for (int row = 0; row < data[col].length; row++) {
					dataTable.setValue(row, col, data[col][row]);
				}
			}
			break;
		}
		return dataTable;
	}
	
	public void refreshVisualizations(){
		RPC.getVisualizationRPC().getVisualizations(visualizationList.getPageStart(), visualizationList.getPageSize(), new AsyncCallback<List<Visualization>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(List<Visualization> result) {
				visualizationDataProvider.updateRowData(visualizationList.getPageStart(), result);
			}
		});
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
			sb.appendHtmlConstant("<div>" + value.getType() + "/" + value.getOptions().getType().toString().toLowerCase());
			sb.appendHtmlConstant("</div>");
			sb.appendHtmlConstant("</td></tr></table>");

		}

	}
}
