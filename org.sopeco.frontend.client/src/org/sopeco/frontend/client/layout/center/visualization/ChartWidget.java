package org.sopeco.frontend.client.layout.center.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.ChartData;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartRowKey;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.frontend.shared.helper.AggregationOutputType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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

public class ChartWidget extends FlowPanel {
	
	private FlowPanel control;
	private ListBox aggregation;
	private ListBox interpolation;
	private Visualization visualization;
	private Widget chartWidget;
	private CoreChart chart = null;
	
	public ChartWidget (){
		this.getElement().getStyle().setPadding(1, Unit.EM);
		chartWidget = new FlowPanel();
		this.add(chartWidget);
		control = new FlowPanel();
		control.add(new Label("Aggregation: "));
		aggregation = new ListBox();
		for (AggregationOutputType t: AggregationOutputType.values()){
			aggregation.addItem(t.toString());
		}
		aggregation.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				if (aggregation.getItemText(aggregation.getSelectedIndex()).equals(AggregationOutputType.SCATTER.toString())){
					interpolation.setSelectedIndex(0);
					interpolation.setEnabled(false);
				} else {
					interpolation.setEnabled(true);
				}
				updateChart();
			}
		});
		control.add(aggregation);
		
		control.add(new Label("Interpolation: "));
		interpolation = new ListBox();
		for (Interpolation i: Interpolation.values()){
			interpolation.addItem(i.toString());
		}
		interpolation.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateChart();
			}
		});
		interpolation.setEnabled(false);
		control.add(interpolation);
	}
	
	public void switchChart(Visualization visualization){
		if (visualization == null){
			chartWidget = null;
			return;
		}
		this.visualization = visualization;
		showChart();
	}
	
	private Options createOptions() {
		ChartOptions chartOptions = visualization.getOptions();
		String name = visualization.getName();
		Options options = Options.create();
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
			switch(Interpolation.valueOf(interpolation.getItemText(interpolation.getSelectedIndex()))){
			case SPLINE:
				options.setLineWidth(2);
				options.setPointSize(0);
				break;
			default:
				options.setLineWidth(0);
				options.setPointSize(3);
			}
			break;
		}
		options.setWidth(900);
		options.setHeight(500);
		options.setTitle(name);
		return options;
	}
	
	public void updateChart(){
		if (chart != null){
			ChartData data = visualization.getData();
			final DataTable dataTable = DataTable.create();
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
			switch(Interpolation.valueOf(interpolation.getItemText(interpolation.getSelectedIndex()))){
			case SPLINE:
				RPC.getVisualizationRPC().applySplineInterpolation(values, 0, 10, 100, new AsyncCallback<Map<Double,List<Double>>>() {
					
					@Override
					public void onSuccess(Map<Double, List<Double>> result) {
						setData(dataTable, result, total);
						chart.draw(dataTable, createOptions());
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				break;
			default:
				setData(dataTable, values, total);
				chart.draw(dataTable, createOptions());
				break;
			}
		}
	}
	
	public void showChart() {
		if (visualization == null) {
			return;
		}
		switch (visualization.getType()) {
		case GCHART:
			Runnable onLoadCallback = new Runnable() {
				public void run() {
					ChartWidget.this.remove(chartWidget);
					chartWidget = new FlowPanel();
					switch (visualization.getOptions().getType()){
					case BARCHART:
						chart = new ColumnChart(createTable(),createOptions());
						break;
					case PIECHART:
						chart = new PieChart(createTable(),createOptions());
						break;
					default:
					chart = new LineChart(createTable(),createOptions());
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

	private AbstractDataTable createTable() {
		ChartData data = visualization.getData();
		final DataTable dataTable = DataTable.create();
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
		switch(Interpolation.valueOf(interpolation.getItemText(interpolation.getSelectedIndex()))){
		case SPLINE:
			RPC.getVisualizationRPC().applySplineInterpolation(values, 0, 10, 100, new AsyncCallback<Map<Double,List<Double>>>() {
				
				@Override
				public void onSuccess(Map<Double, List<Double>> result) {
					setData(dataTable, result, total);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		default:
			setData(dataTable, values, total);
			break;
		}
		return dataTable;
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
				switch (AggregationOutputType.valueOf(aggregation.getItemText(aggregation.getSelectedIndex()))){
				case AVERAGE:
					dataTable.setValue(index, 1, d/total.get(entry.getKey()));
					break;
				default:
					dataTable.setValue(index, 1, d);
				}
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
	}
	
	public static enum Interpolation {
		NONE, SPLINE;
	}
}
