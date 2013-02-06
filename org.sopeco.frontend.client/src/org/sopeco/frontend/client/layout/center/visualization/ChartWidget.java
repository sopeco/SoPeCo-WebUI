package org.sopeco.frontend.client.layout.center.visualization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.sopeco.frontend.shared.entities.ChartData;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartRowKey;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.frontend.shared.helper.AggregationOutputType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
				updateChart();
			}
		});
		control.add(aggregation);
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
			options.setLineWidth(0);
			options.setPointSize(3);
			break;
		}
		options.setWidth(900);
		options.setHeight(500);
		options.setTitle(name);
		return options;
	}
	
	public void updateChart(){
		if (chart != null){
			chart.draw(createTable(), createOptions());
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
		DataTable dataTable = DataTable.create();
		List<List<Double>> dataList = data.getDatarows();
		List<ChartRowKey> names = data.getxAxis();
		if (names.size() <= 0 || dataList.size() <= 0){
			return dataTable;
		}
		switch (visualization.getOptions().getType()) {
		case PIECHART:
		case BARCHART:
			dataTable.addColumn(ColumnType.STRING, "Input");
			break;
		default:
			dataTable.addColumn(ColumnType.NUMBER, "Input");
		}
		switch (AggregationOutputType.valueOf(aggregation.getItemText(aggregation.getSelectedIndex()))) {
		case AVERAGE:
			dataTable.addColumn(ColumnType.NUMBER,AggregationOutputType.AVERAGE.toString());
			Map<Double, Double> average = new TreeMap<Double, Double>();
			Map<Double, Integer> total = new HashMap<Double, Integer>();
			for (int row = 0; row < dataList.size(); row++){
				Double key = names.get(row).getKeyValue(data.getInputParameter());
				if (average.get(key) == null){
					average.put(key, 0.0);
				}
				if (total.get(key) == null){
					total.put(key, 0);
				}
				for (int column = 0; column < dataList.get(row).size(); column++){
					average.put(key,  dataList.get(row).get(column) + average.get(key));
					total.put(key, total.get(key)+1);
				}
			}
			int j = 0;
			for (Entry<Double, Double> entry : average.entrySet()){
				dataTable.addRow();
				switch (visualization.getOptions().getType()) {
				case PIECHART:
				case BARCHART:
					dataTable.setValue(j, 0, ""+entry.getKey());
					break;
				default:
					dataTable.setValue(j, 0, entry.getKey());
				}
				dataTable.setValue(j, 1, entry.getValue()/total.get(entry.getKey()));
				j++;
			}
			break;
		case SUM:
			dataTable.addColumn(ColumnType.NUMBER,AggregationOutputType.AVERAGE.toString());
			Map<Double, Double> average2 = new TreeMap<Double, Double>();
			for (int row = 0; row < dataList.size(); row++){
				Double key = names.get(row).getKeyValue(data.getInputParameter());
				if (average2.get(key) == null){
					average2.put(key, 0.0);
				}
				for (int column = 0; column < dataList.get(row).size(); column++){
					average2.put(key,  dataList.get(row).get(column) + average2.get(key));
				}
			}
			int j2 = 0;
			for (Entry<Double, Double> entry : average2.entrySet()){
				dataTable.addRow();
				switch (visualization.getOptions().getType()) {
				case PIECHART:
				case BARCHART:
					dataTable.setValue(j2, 0, ""+entry.getKey());
					break;
				default:
					dataTable.setValue(j2, 0, entry.getKey());
				}
				dataTable.setValue(j2, 1, entry.getValue());
				j2++;
			}
			break;
		default:
			dataTable.addColumn(ColumnType.NUMBER,visualization.getOptions().getxAxisLabel());
			for (int row = 0; row < dataList.size(); row++){
				for (int column = 0; column < dataList.get(row).size(); column++){
					dataTable.addRow();
					switch (visualization.getOptions().getType()) {
					case PIECHART:
					case BARCHART:
						dataTable.setValue(column+row*dataList.get(row).size(), 0, ""+names.get(row).getKeyValue(data.getInputParameter()));
						break;
					default:
						dataTable.setValue(column+row*dataList.get(row).size(), 0, names.get(row).getKeyValue(data.getInputParameter()));
					}
					dataTable.setValue(column+row*dataList.get(row).size(), 1,dataList.get(row).get(column));
				}
			}
			break;
		}
		return dataTable;
	}

}
