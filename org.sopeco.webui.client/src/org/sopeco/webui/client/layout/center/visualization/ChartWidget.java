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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.entities.ChartData;
import org.sopeco.webui.shared.entities.ChartOptions;
import org.sopeco.webui.shared.entities.ChartRowKey;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.helper.AggregationOutputType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.HorizontalAxisOptions;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

/** A gwt widget displaying a {@link Visualization}.
 * <br/>If the {@link Visualization} is of the type {@link Visualization.Type#GCHART} a google chart widget will be created.
 * <br/>If the {@link Visualization} is of the type {@link Visualization.Type#LINK} an &lt;iframe&gt; with the link will be created
 * 
 * @author Benjamin Ebling
 *
 */
public class ChartWidget extends FlowPanel {
	
	private FlowPanel control;
	private ListBox aggregation;
	private ListBox dataProcessing;
	private ListBox processingType;
	private Visualization visualization;
	private Widget chartWidget;
	private CoreChart chart = null;
	private Options options = null;
	private DataTable dataTable = null;
	private Label numberOfSplinesLabel;
	private TextBox numberOfSplines;
	
	public ChartWidget (){
		this.getElement().getStyle().setPadding(1, Unit.EM);
		chartWidget = new FlowPanel();
		this.add(chartWidget);
		control = new FlowPanel();
		control.add(new Label("Aggregation: "));
		aggregation = new ListBox();
		for (AggregationOutputType t: AggregationOutputType.values()){
			aggregation.addItem(t.name());
		}
		aggregation.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				dataProcessing.clear();
				if (aggregation.getItemText(aggregation.getSelectedIndex()).equals(AggregationOutputType.SCATTER.toString())){
					for (DataProcessing i: DataProcessing.values()){
						if (i != DataProcessing.INTERPOLATION){
							dataProcessing.addItem(i.toString());
						}
					}
				} else {
					for (DataProcessing i: DataProcessing.values()){
						dataProcessing.addItem(i.toString());
					}
				}
				refreshChart();
			}
		});
		control.add(aggregation);
		
		control.add(new Label(R.lang.dataProcessing()));
		dataProcessing = new ListBox();
		for (DataProcessing i: DataProcessing.values()){
			if (i != DataProcessing.INTERPOLATION){
				dataProcessing.addItem(i.toString());
			}
		}
		dataProcessing.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				switch (DataProcessing.valueOf(dataProcessing.getItemText(dataProcessing.getSelectedIndex()))) {
				case INTERPOLATION:
					processingType.clear();
					for (Interpolation i : Interpolation.values()){
						processingType.addItem(i.name());
					}
					processingType.setEnabled(true);
					break;
				case REGRESSION:
					processingType.clear();
					for (Regression r : Regression.values()){
						processingType.addItem(r.name());
					}
					processingType.setEnabled(true);
					break;
				default:
					processingType.clear();
					processingType.setEnabled(false);
					break;
				}
				refreshChart();
			}
		});
		control.add(dataProcessing);
		processingType = new ListBox();
		processingType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				refreshChart();
			}
		});
		processingType.setEnabled(false);
		control.add(processingType);
		numberOfSplinesLabel = new Label(R.lang.numberOfPoints());
		control.add(numberOfSplinesLabel);
		numberOfSplines = new TextBox();
		numberOfSplines.setText("100");
		numberOfSplines.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				try {
					Integer.parseInt(numberOfSplines.getText());
				} catch (NumberFormatException ex){
					numberOfSplines.setText("100");
				}
				refreshChart();
			}
		});
		control.add(numberOfSplines);
	}
	
	public void switchChart(Visualization visualization){
		if (visualization == null){
			chartWidget = null;
			return;
		}
		this.visualization = visualization;
		showChart();
	}
	
	private void refreshOptions() {
		ChartOptions chartOptions = visualization.getOptions();
		String name = visualization.getName();
		options = Options.create();
		HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
		hOptions.setTitle(chartOptions.getxAxisLabel());
		options.setHAxisOptions(hOptions);
		switch(chartOptions.getType()){
		case BARCHART:
			options.set("bar.groupWidth", "50");
			break;
		case PIECHART:
			options.set("is3D", true);
			break;
		case LINECHART:
			switch(DataProcessing.valueOf(dataProcessing.getItemText(dataProcessing.getSelectedIndex()))){
			case NONE:
				options.setLineWidth(0);
				options.setPointSize(3);
				break;
			default:
				options.setLineWidth(2);
				options.setPointSize(0);
			}
			break;
		}
		options.setWidth(900);
		options.setHeight(500);
		options.setTitle(name);
	}
	
	public void refreshDataTable(){
		ChartData data = visualization.getData();
		dataTable = DataTable.create();
		switch (visualization.getOptions().getType()) {
		case PIECHART:
		case BARCHART:
			dataTable.addColumn(ColumnType.STRING, "Input");
			break;
		default:
			dataTable.addColumn(ColumnType.NUMBER, "Input");
		}
		dataTable.addColumn(ColumnType.NUMBER,visualization.getOptions().getxAxisLabel());
		Map<Double, List<Double>> values = new TreeMap<Double, List<Double>>();
		final Map<Double, Integer> total = new HashMap<Double, Integer>();
		calculateData(data, values, total);
		switch(DataProcessing.valueOf(dataProcessing.getItemText(dataProcessing.getSelectedIndex()))){
		case INTERPOLATION:
			switch(Interpolation.valueOf(processingType.getItemText(processingType.getSelectedIndex()))){
			case SPLINE:
				RPC.getVisualizationRPC().applySplineInterpolation(values, 0, 10, Integer.parseInt(numberOfSplines.getText()), new AsyncCallback<Map<Double,List<Double>>>() {
					
					@Override
					public void onSuccess(Map<Double, List<Double>> result) {
						setData(dataTable, result, total);
						if (chart != null){
							chart.draw(dataTable, options);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});
				break;
			}
			
			break;
		case REGRESSION:
			switch(Regression.valueOf(processingType.getItemText(processingType.getSelectedIndex()))){
			case SIMPLE_REGRESSION:
				RPC.getVisualizationRPC().applySimpleRegression(values, 0, 10, Integer.parseInt(numberOfSplines.getText()), new AsyncCallback<Map<Double,List<Double>>>() {
					
					@Override
					public void onSuccess(Map<Double, List<Double>> result) {
						setData(dataTable, result, total);
						if (chart != null){
							chart.draw(dataTable, options);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
				});
				break;
			}
			
			break;
		default:
			setData(dataTable, values, total);
			break;
		}
	}
	
	private void refreshChart(){
		if (visualization == null) {
			return;
		}
		refreshOptions();
		refreshDataTable();
		chart.draw(dataTable, options);
	}
	
	public void showChart() {
		if (visualization == null) {
			return;
		}
		switch (visualization.getType()) {
		case GCHART:
			Runnable onLoadCallback = new Runnable() {
				public void run() {
					refreshOptions();
					refreshDataTable();
					ChartWidget.this.remove(chartWidget);
					chartWidget = new FlowPanel();
					switch (visualization.getOptions().getType()){
					case BARCHART:
						chart = new ColumnChart(dataTable,options);
						break;
					case PIECHART:
						chart = new PieChart(dataTable,options);
						break;
					default:
					chart = new LineChart(dataTable,options);
					}
					((FlowPanel) chartWidget).add(chart);
					ChartWidget.this.add(chartWidget);
					ChartWidget.this.add(control);
				}
			};

			// Load the visualization api, passing the onLoadCallback to be
			// called
			// when loading is done.
			VisualizationUtils.loadVisualizationApi(onLoadCallback,
					CoreChart.PACKAGE);
			break;
		default:
			this.clear();
			chartWidget = new Frame(visualization.getLink());
			chartWidget.getElement().getStyle().setWidth(100, Unit.PCT);
			chartWidget.getElement().getStyle().setHeight(100, Unit.PCT);
			chartWidget.getElement().getStyle().setBorderWidth(0, Unit.EM);
			this.add(chartWidget);
			chart = null;
		}
		return;
	}

	private void setData(DataTable dataTable, Map<Double, List<Double>> values,
			Map<Double, Integer> total) {
		int row = 0;
		for (Entry<Double, List<Double>> entry : values.entrySet()){
			int col = 0;
			for (Double d : entry.getValue()){
				dataTable.addRow();
				int index = col+row*entry.getValue().size();
				switch (visualization.getOptions().getType()) {
				case PIECHART:
				case BARCHART:
					dataTable.setValue(index, 0, ""+entry.getKey());
					break;
				default:
					dataTable.setValue(index, 0, entry.getKey());
				}
				dataTable.setValue(index, 1, d);
				col++;
			}
			
			row++;
		}
	}

	private void calculateData(ChartData data, Map<Double, List<Double>> values,
			Map<Double, Integer> total) {
		List<List<Double>> dataList = data.getDatarows();
		List<ChartRowKey> names = data.getxAxis();
		for (int row = 0; row < dataList.size(); row++){
			Double key = names.get(row).getKeyValue(data.getInputParameter());
			if (values.get(key) == null){
				values.put(key, new ArrayList<Double>());
			}
			if (total.get(key) == null){
				total.put(key, 0);
			}
			switch (AggregationOutputType.valueOf(aggregation.getItemText(aggregation.getSelectedIndex()))){
			case AVERAGE:
			case SUM:
				if (values.get(key).size() <= 0){
					values.get(key).add(0.0);
				}
				for (int column = 0; column < dataList.get(row).size(); column++){
					values.get(key).set(0, dataList.get(row).get(column) + values.get(key).get(0));
					total.put(key, total.get(key)+1);
				}
				break;
			default:
				for (int column = 0; column < dataList.get(row).size(); column++){
					values.get(key).add(dataList.get(row).get(column));
				}
			}
			
		}
		switch (AggregationOutputType.valueOf(aggregation.getItemText(aggregation.getSelectedIndex()))){
		case AVERAGE:
			for (Entry<Double, List<Double>> entry : values.entrySet()){
				entry.getValue().set(0, entry.getValue().get(0)/total.get(entry.getKey()));
			}
			break;
		default:
		}
	}
	
	public static enum DataProcessing {
		NONE, INTERPOLATION, REGRESSION;
	}
	
	public static enum Interpolation {
		SPLINE;
	}
	
	public static enum Regression {
		SIMPLE_REGRESSION;
	}
}
