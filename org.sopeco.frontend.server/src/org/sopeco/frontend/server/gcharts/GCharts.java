package org.sopeco.frontend.server.gcharts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	}

	@Override
	public ISoPeCoExtension<?> getProvider() {
		return null;
	}

	@Override
	public List<Visualization> getVisualizations(int start, int length) {
		if (start > visualizations.size() - 1){
			return new ArrayList<Visualization>();
		}
		start = start < 0 ? 0 : start;
		length = start + length > visualizations.size() ? visualizations.size()-start : length;
		return visualizations.subList(start, start+length);
	}

	@Override
	public synchronized Visualization createVisualization(String experimentName,
			Double[][] data, List<ChartParameter> chartParameter,
			ChartOptions options) {
		Visualization visualization = new Visualization();
		visualization.setLink("");
		visualization.setName(experimentName);
		visualization.setData(data);
		visualization.setType(Type.GCHART);
		visualization.setChartParameters(chartParameter);
		visualization.setOptions(options);
		visualizations.add(visualization);
		return visualization;
	}

	@Override
	public void addAll(Collection<Visualization> collection) {
		visualizations.addAll(collection);
	}

	@Override
	public void remove(Visualization visualization) {
		visualizations.remove(visualization);
	}
}
