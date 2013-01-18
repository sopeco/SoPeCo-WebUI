package org.sopeco.frontend.server.papconnector;

import org.sopeco.frontend.shared.entities.Visualization;

public interface ChartCreator {
	
	public Visualization getChartHTML(String experimentName, Double[][] data, String[] columnNames, ChartTypes type);

	public Visualization[] getAllVisualizations();
	
	public static enum ChartTypes{
		LINE_CHART, BAR_CHART, PIE_CHART;
	}
}
