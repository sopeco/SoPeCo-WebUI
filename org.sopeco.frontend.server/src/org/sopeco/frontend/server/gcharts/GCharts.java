package org.sopeco.frontend.server.gcharts;

import java.util.List;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.frontend.server.chartconnector.IChartConnection;
import org.sopeco.frontend.shared.entities.ChartData;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.frontend.shared.entities.Visualization.Type;

public class GCharts implements IChartConnection {
	
	public GCharts() {
	}

	@Override
	public ISoPeCoExtension<?> getProvider() {
		return null;
	}

	@Override
	public synchronized Visualization createVisualization(String experimentName,
			ChartData data, ChartParameter inputParameter, ChartParameter outputParameter,
			ChartOptions options) {
		Visualization visualization = new Visualization();
		visualization.setLink("");
		visualization.setName(experimentName);
		visualization.setData(data);
		visualization.setType(Type.GCHART);
		visualization.setInputParameter(inputParameter);
		visualization.setOutputParameter(outputParameter);
		visualization.setOptions(options);
		return visualization;
	}
}
