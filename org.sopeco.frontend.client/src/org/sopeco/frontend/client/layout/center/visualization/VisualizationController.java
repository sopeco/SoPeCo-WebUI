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
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class VisualizationController implements ICenterController {
	private DockLayoutPanel rootWidget;
	private HorizontalPanel controlWidget;
	private CellList<Visualization> visualizationList;
	private Anchor chartLink;
	private FlowPanel centerPanel;
	private Timer timer;
	private boolean refreshing = false;
	private Widget chartWidget;

	public static ListDataProvider<Visualization> visualizationDataProvider = new ListDataProvider<Visualization>();
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
		controlWidget = new HorizontalPanel();
		controlWidget.addStyleName("visualizationControl");
		Button addVisualization = new Button(R.get("addvisualization"));
		controlWidget.add(addVisualization);
		controlWidget.setCellVerticalAlignment(addVisualization,
				HasVerticalAlignment.ALIGN_MIDDLE);
		chartLink = new Anchor("");
		controlWidget.add(chartLink);
		controlWidget.setCellVerticalAlignment(chartLink,
				HasVerticalAlignment.ALIGN_MIDDLE);
		visualizationList = new CellList<Visualization>(new VisualizationCell(),KEY_PROVIDER);
		// visualizationList.setPageSize(8);
		visualizationList
				.setEmptyListWidget(new Label(
						"Either the visualizations are still loading or there are no visualizations yet."));
		final SingleSelectionModel<Visualization> ssm = new SingleSelectionModel<Visualization>(KEY_PROVIDER);
		ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				createChart(ssm.getSelectedObject());
				chartLink.setHref(ssm.getSelectedObject().getLink());
				chartLink.setText(ssm.getSelectedObject().getLink());

			}
		});
		visualizationList.setSelectionModel(ssm);
		visualizationDataProvider.addDataDisplay(visualizationList);
		visualizationList.setWidth("300px");
		rootWidget.addNorth(controlWidget, 5);
		ScrollPanel scrollPanel = new ScrollPanel(visualizationList);
		scrollPanel.addStyleName("visualizationList");
		rootWidget.addWest(scrollPanel, 25);
		rootWidget.add(centerPanel);
		loadAllCharts();
		timer = new Timer() {

			@Override
			public void run() {
				if (rootWidget.isAttached()) {
					loadAllCharts();
				}
			}
		};
		timer.scheduleRepeating(10000);
	}

	@Override
	public Widget getView() {
		return rootWidget;
	}

	@Override
	public void reset() {
	}

	@Override
	public void onSwitchTo() {
		loadAllCharts();
	}

	public void loadAllCharts() {
		if (refreshing) {
			return;
		}
		refreshing = true;
		RPC.getVisualizationRPC().getAllVisualizations(
				new AsyncCallback<Visualization[]>() {

					@Override
					public void onSuccess(Visualization[] result) {
						visualizationDataProvider.getList().clear();
						for (Visualization visualization : result) {
							addVisualization(visualization);
						}
						refreshing = false;
					}

					@Override
					public void onFailure(Throwable caught) {
						refreshing = false;
					}
				});

	}

	public void addVisualization(Visualization visualization) {
		visualizationDataProvider.getList().add(visualization);
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

					// Create a pie chart visualization.
					LineChart pie = new LineChart(createTable(
							visualization.getData(),
							visualization.getChartParameters()),
							createOptions(visualization.getOptions(),
									visualization.getName()));
					((FlowPanel) chartWidget).add(pie);
					centerPanel.clear();
					centerPanel.add(chartWidget);
				}
			};

			// Load the visualization api, passing the onLoadCallback to be
			// called
			// when loading is done.
			VisualizationUtils.loadVisualizationApi(onLoadCallback,
					LineChart.PACKAGE);

			break;
		default:
			chartWidget = new Frame(visualization.getLink());
			centerPanel.clear();
			centerPanel.add(chartWidget);
		}
		return chartWidget;
	}

	private Options createOptions(ChartOptions chartOptions, String name) {
		Options options = Options.create();
		options.setLineWidth(0);
		options.setPointSize(3);
		options.setWidth(900);
		options.setHeight(500);
		options.setTitle(name);
		return options;
	}

	private AbstractDataTable createTable(Double[][] data,
			ChartParameter[] chartParameters) {
		DataTable dataTable = DataTable.create();
		dataTable.addRows(data[0].length);
		for (int col = 0; col < data.length; col++) {
			dataTable.addColumn(ColumnType.NUMBER,
					chartParameters[col].getParameterName());
			for (int row = 0; row < data[col].length; row++) {
				dataTable.setValue(row, col, data[col][row]);
			}
		}
		return dataTable;
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
			sb.appendHtmlConstant("<div>" + value.getLink());
			sb.appendHtmlConstant("</div>");
			sb.appendHtmlConstant("</td></tr></table>");

		}

	}
}
