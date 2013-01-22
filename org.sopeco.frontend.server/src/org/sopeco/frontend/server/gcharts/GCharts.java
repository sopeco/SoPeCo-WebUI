package org.sopeco.frontend.server.gcharts;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.frontend.server.chartconnector.IChartConnection;
import org.sopeco.frontend.shared.entities.Visualization;

public class GCharts implements IChartConnection {
	private List<Visualization> visualizations;
	
	public GCharts() {
		visualizations = new ArrayList<Visualization>();
	}

	@Override
	public ISoPeCoExtension<?> getProvider() {
		return null;
	}

	@Override
	public Visualization getChartHTML(String experimentName, Double[][] data,
			String[] columnNames, ChartTypes type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Visualization[] getAllVisualizations() {
		// TODO Auto-generated method stub
		return null;
	}

	public Visualization createVisualization(String experimentName, Double[][] data,
			String[] columnNames, ChartTypes type){
		Visualization visualization = new Visualization();
		visualization.setChart(html);
		visualization.setLink("");
		visualization.setName("Test Google Chart");
		return visualization;
	}
	
	private static final String html = "<iframe href=''><html>" +
			"<head>" +
			"<script type='text/javascript' src='https://www.google.com/jsapi'></script>" +
			"<script type='text/javascript'>" +
			"google.load('visualization', '1', {packages:['corechart']});" +
			"google.setOnLoadCallback(drawChart);" +
			"function drawChart() {" +
			"var data = google.visualization.arrayToDataTable([" +
			"['Year', 'Sales', 'Expenses']," +
			"['2004',  1000,      400]," +
			"['2005',  1170,      460]," +
			"['2006',  660,       1120]," +
			"['2007',  1030,      540]" +
			"]);" +
			"var options = {" +
			"title: 'Company Performance'" +
			"};" +
			"var chart = new google.visualization.LineChart(document.getElementById('chart_div'));" +
			"chart.draw(data, options);" +
			"}" +
			"</script>" +
			"</head>" +
			"<body>" +
			"<div id='chart_div' style='width: 900px; height: 500px;'></div>" +
					"</body>" +
					"</html></iframe>";
}
