package org.sopeco.frontend.server.gcharts;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.frontend.server.chartconnector.IChartConnection;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.frontend.shared.entities.ChartOptions.ChartType;
import org.sopeco.frontend.shared.entities.Visualization.Type;

public class GCharts implements IChartConnection {
	private List<Visualization> visualizations;
	
	public GCharts() {
		visualizations = new ArrayList<Visualization>();
		ChartParameter[] chartParameter = new ChartParameter[3];
		chartParameter[0] = new ChartParameter();
		chartParameter[0].setParameterName("x");
		chartParameter[1] = new ChartParameter();
		chartParameter[1].setParameterName("y");
		chartParameter[2] = new ChartParameter();
		chartParameter[2].setParameterName("z");
		ChartOptions options = new ChartOptions();
		options.setType(ChartType.LINECHART);
		createVisualization("test_experiment", new Double[][]{{1.0,2.0},{2.0,1.5},{3.0,3.0}}, chartParameter, options);
	}

	@Override
	public ISoPeCoExtension<?> getProvider() {
		return null;
	}

	@Override
	public Visualization[] getAllVisualizations() {
		return visualizations.toArray(new Visualization[visualizations.size()]);
	}
	
	private static final String html = "<div id='chart_div' style='width: 900px; height: 500px;'></div>";

	@Override
	public synchronized Visualization createVisualization(String experimentName,
			Double[][] data, ChartParameter[] chartParameter,
			ChartOptions options) {
		Visualization visualization = new Visualization();
		visualization.setId(System.nanoTime());
		visualization.setLink("");
		visualization.setName(experimentName);
		visualization.setData(data);
		visualization.setType(Type.GCHART);
		visualization.setChartParameters(chartParameter);
		visualization.setOptions(options);
		visualizations.add(visualization);
		return visualization;
	}
}
