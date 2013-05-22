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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.entities.ChartData;
import org.sopeco.webui.shared.entities.ChartOptions;
import org.sopeco.webui.shared.entities.ChartRowKey;
import org.sopeco.webui.shared.entities.RegressionInfo;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.helper.AggregationOutputType;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.AxisOptions;
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
	
	private Grid control;
	private ComboBox aggregation;
	private ComboBox dataProcessing;
	private ComboBox processingType;
	private Visualization visualization;
	private Widget chartWidget;
	private CoreChart chart = null;
	private Options options = null;
	private DataTable dataTable = null;
	private Label numberOfSplinesLabel;
	private TextBox numberOfSplines;
	private String initialNumberOfSplines = "100";
	private CheckBox showRegression;
	
	public ChartWidget (){
		this.getElement().getStyle().setPadding(1, Unit.EM);
		chartWidget = new FlowPanel();
		this.add(chartWidget);
		initControl();
	}

	private void initControl() {
		control = new Grid(2, 4);
		control.getElement().setAttribute("frame", "void");
		control.getElement().setAttribute("rules", "all");
		control.setBorderWidth(1);
		control.setCellPadding(3);
		control.setCellSpacing(0);
		control.setWidget(0, 0, new Label(R.lang.aggregation()));
		aggregation = new ComboBox();
		aggregation.setEditable(false);
		for (AggregationOutputType t: AggregationOutputType.values()){
			aggregation.addItem(t.name());
		}
		aggregation.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				dataProcessing.clear();
				if (aggregation.getText().equals(AggregationOutputType.SCATTER.toString())){
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
		control.setWidget(1, 0, aggregation);
		control.setWidget(0, 1, new Label(R.lang.regression()));
		showRegression = new CheckBox();
		showRegression.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				refreshChart();
			}
		});
		control.setWidget(1, 1, showRegression);
		control.setWidget(0, 2, new Label(R.lang.dataProcessing()));
		dataProcessing = new ComboBox();
		dataProcessing.setEditable(false);
		for (DataProcessing i: DataProcessing.values()){
			if (i != DataProcessing.INTERPOLATION){
				dataProcessing.addItem(i.toString());
			}
		}
		dataProcessing.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				switch (DataProcessing.valueOf(dataProcessing.getText())) {
				case INTERPOLATION:
					processingType.clear();
					for (Interpolation i : Interpolation.values()){
						processingType.addItem(i.name());
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
		HorizontalPanel hpProcessing = new HorizontalPanel();
		hpProcessing.add(dataProcessing);
		processingType = new ComboBox();
		processingType.setEditable(false);
		processingType.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				refreshChart();
			}
		});
		processingType.setEnabled(false);
		hpProcessing.add(processingType);
		control.setWidget(1, 2, hpProcessing);
		numberOfSplinesLabel = new Label(R.lang.numberOfPoints());
		control.setWidget(0, 3, numberOfSplinesLabel);
		numberOfSplines = new TextBox();
		numberOfSplines.setText(initialNumberOfSplines);
		numberOfSplines.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				try {
					Integer.parseInt(numberOfSplines.getText());
				} catch (NumberFormatException ex){
					numberOfSplines.setText(initialNumberOfSplines);
				}
				refreshChart();
			}
		});
		control.setWidget(1,3,numberOfSplines);
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
		HorizontalAxisOptions hOptions = HorizontalAxisOptions.create();
		hOptions.setTitle(chartOptions.getxAxisLabel());
		AxisOptions vOptions = AxisOptions.create();
		vOptions.setMinValue(0);
		options = Options.create();
		switch(chartOptions.getType()){
		case BARCHART:
			options.set("bar.groupWidth", "50");
			break;
		case PIECHART:
			options.set("is3D", true);
			break;
		case LINECHART:
			//All series' default type is 'line'
			options.setLineWidth(2);
			options.setPointSize(0);
			switch(DataProcessing.valueOf(dataProcessing.getText())){
			case NONE:
				//change type of first series to point
				Options seriesOptions = Options.create();
				Options series1Options = Options.create();
				series1Options.setLineWidth(0);
				series1Options.setPointSize(3);
				seriesOptions.set("0", series1Options);
				options.set("series", seriesOptions);
				break;
			default:
				
			}
			break;
		}
		options.setHAxisOptions(hOptions);
		options.setVAxisOptions(vOptions);
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
		dataTable.addColumn(ColumnType.NUMBER,visualization.getOutputParameter().getParameterName());
		Map<Double, List<Double>> values = new TreeMap<Double, List<Double>>();
		final Map<Double, Integer> total = new HashMap<Double, Integer>();
		calculateData(data, values, total);
		switch(DataProcessing.valueOf(dataProcessing.getText())){
		case INTERPOLATION:
			switch(Interpolation.valueOf(processingType.getText())){
			case SPLINE:
				RPC.getVisualizationRPC().applySplineInterpolation(values, 0, 10, Integer.parseInt(numberOfSplines.getText()), new AsyncCallback<Map<Double,List<Double>>>() {
					
					@Override
					public void onSuccess(Map<Double, List<Double>> result) {
						setData(result);
						if (chart != null){
							chart.draw(dataTable, options);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Could not apply spline interpolation.",caught);
					}
				});
				break;
			}
			
			break;
		default:
			setData(values);
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

	private void setData(Map<Double, List<Double>> values) {
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
	
	private void addRegression(Map<Double, List<Double>> values, String name) {
		dataTable.addColumn(ColumnType.NUMBER, name);
		int nr = dataTable.getNumberOfColumns()-1;
		int k = 0;
		int n = dataTable.getNumberOfRows()/values.size();
		for (Entry<Double, List<Double>> entry : values.entrySet()){
			for (int i = 0; i < n; i++){
				dataTable.setValue(k*n+i, nr, entry.getValue().get(0));
			}
			k++;
		}
	}

	private void calculateData(ChartData data, Map<Double, List<Double>> values,
			final Map<Double, Integer> total) {
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
			switch (AggregationOutputType.valueOf(aggregation.getText())){
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
		switch (AggregationOutputType.valueOf(aggregation.getText())){
		case AVERAGE:
			for (Entry<Double, List<Double>> entry : values.entrySet()){
				entry.getValue().set(0, entry.getValue().get(0)/total.get(entry.getKey()));
			}
			break;
		case MEDIAN:
			for (Entry<Double, List<Double>> entry : values.entrySet()){
				int n = entry.getValue().size();
				Collections.sort(entry.getValue());
				double d = 0.0;
				if (n%2 == 0){
					d = (entry.getValue().get(n/2-1)+entry.getValue().get(n/2))/2;
				} else {
					d = entry.getValue().get((n+1)/2-1);
				}
				entry.getValue().clear();
				entry.getValue().add(d);
			}
			break;
		default:
		}
		if (showRegression.getValue()){
			RPC.getVisualizationRPC().applySimpleRegression(values, new AsyncCallback<RegressionInfo>() {

				@Override
				public void onSuccess(RegressionInfo result) {
					addRegression(result.getData(), R.lang.regression());
					if (chart != null){
						chart.draw(dataTable, options);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("Could not apply simple regression.",caught);
				}
			});
		}
	}
	
	public static enum DataProcessing {
		NONE, INTERPOLATION;
	}
	
	public static enum Interpolation {
		SPLINE;
	}
	
	public static enum Regression {
		SIMPLE_REGRESSION;
	}
}
