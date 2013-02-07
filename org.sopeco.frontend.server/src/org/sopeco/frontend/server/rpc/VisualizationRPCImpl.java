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
package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.frontend.client.rpc.VisualizationRPC;
import org.sopeco.frontend.server.chartconnector.IChartConnection;
import org.sopeco.frontend.server.chartconnector.IChartConnectionExtension;
import org.sopeco.frontend.server.gcharts.GCharts;
import org.sopeco.frontend.server.persistence.UiPersistence;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.entities.ChartData;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.ChartRowKey;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetRow;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.exceptions.DataNotFoundException;

public class VisualizationRPCImpl extends SuperRemoteServlet implements
		VisualizationRPC {
	private IChartConnection chartCreator;
	private List<IChartConnectionExtension> extensions;
	private static final String G_CHARTS = "Google Charts";

	public VisualizationRPCImpl() {
		chartCreator = new GCharts();
		extensions = (ExtensionRegistry.getSingleton()
				.getExtensions(IChartConnectionExtension.class)).getList();
	}

	@Override
	public Visualization createChart(SharedExperimentRuns experiementRun,
			ChartParameter inputParameter, ChartParameter outputParameterd, ChartOptions options, String extension) {
		ChartData data;
		loadExtension(extension);
		String scenarioName = experiementRun.getParentSeries()
				.getParentInstance().getScenarioName();
		String measurementEnvironmentUrl = experiementRun.getParentSeries()
				.getParentInstance().getControllerUrl();
		String experimentName = experiementRun.getParentSeries()
				.getExperimentName();
		Long timestamp = experiementRun.getTimestamp();
		String accountName = getUser().getCurrentAccount().getId();
		ExperimentSeriesRun run = getRun(scenarioName,
				measurementEnvironmentUrl, experimentName, timestamp);
		data = loadData(run, inputParameter, outputParameterd, options);
		Visualization visualization = chartCreator.createVisualization(
				run.getLabel(), data, inputParameter, outputParameterd, options);
		visualization.setScenarioName(scenarioName);
		visualization.setMeasurementEnvironmentUrl(measurementEnvironmentUrl);
		visualization.setExperimentName(experimentName);
		visualization.setTimestamp(timestamp);
		visualization.setAccountId(accountName);
		visualization.setId(System.currentTimeMillis());
		System.out.println("saving chart...");
		UiPersistence.getUiProvider().storeVisualization(visualization);
		return visualization;

	}

	@Override
	public List<Visualization> getVisualizations(int start, int length) {
		List<Visualization> visualizations = new ArrayList<Visualization>();
		String accountName = getUser().getCurrentAccount().getId();
		System.out.println("loading charts...");
		visualizations.addAll(UiPersistence.getUiProvider()
				.loadVisualizationsByAccount(accountName));
		if (start > visualizations.size() - 1){
			return new ArrayList<Visualization>();
		}
		start = start < 0 ? 0 : start;
		length = start + length > visualizations.size() ? visualizations.size()-start : length;
		List<Visualization> vis = new ArrayList<Visualization>(visualizations.subList(start, start+length));
		List<Visualization> visualizationsToRemove = new ArrayList<Visualization>();
		for (Visualization visualization : vis) {
			if (visualization.getData() == null
					&& visualization.getType() == Visualization.Type.GCHART) {
				ExperimentSeriesRun run = getRun(visualization);
				if (run == null){
					deleteVisualization(visualization);
					visualizationsToRemove.add(visualization);
				} else {
					visualization.setData(loadData(run,
							visualization.getInputParameter(), visualization.getOutputParameter(), visualization.getOptions()));
				}
				
			}
		}
		vis.removeAll(visualizationsToRemove);
		return vis;
	}

	private ChartData loadData(ExperimentSeriesRun run,
			ChartParameter inputParameter, ChartParameter outputParameter, ChartOptions chartOptions) {
		ChartData data;
		data = new ChartData();
		data.setInputParameter(inputParameter);
		DataSetAggregated dataset = run.getSuccessfulResultDataSet();
		SimpleDataSet simpledata = dataset.convertToSimpleDataSet();
		chartOptions.setxAxisLabel(inputParameter.getParameterName());
		
		Map<ChartRowKey, List<Double>> datamap = new TreeMap<ChartRowKey, List<Double>>();
		for (SimpleDataSetRow row : simpledata) {
			ChartRowKey key = new ChartRowKey();
			for (ParameterValue<?> value : row.getRowValues()){
				if(value.getParameter().getRole() == ParameterRole.INPUT){
					ChartParameter cp = new ChartParameter();
					cp.setParameterName(value.getParameter().getFullName());
					key.set(cp, value.getValueAsDouble());
				}
			}
			for (ParameterValue<?> value : row.getRowValues()){
				if(value.getParameter().getRole() == ParameterRole.OBSERVATION && value.getParameter().getFullName().equals(outputParameter.getParameterName())){
					List<Double> list = datamap.get(key);
					if (list == null){
						list = new ArrayList<Double>();
						datamap.put(key, list);
					}
					list.add(value.getValueAsDouble());
				}
			}
			
		}
		data.setData(datamap);
		return data;
	}

	public ExperimentSeriesRun getRun(SharedExperimentRuns experiementRun) {
		return getRun(experiementRun.getParentSeries().getParentInstance()
				.getScenarioName(), experiementRun.getParentSeries()
				.getParentInstance().getControllerUrl(), experiementRun
				.getParentSeries().getExperimentName(),
				experiementRun.getTimestamp());
	}

	public ExperimentSeriesRun getRun(Visualization visualization) {
		return getRun(visualization.getScenarioName(),
				visualization.getMeasurementEnvironmentUrl(),
				visualization.getExperimentName(), visualization.getTimestamp());
	}

	public ExperimentSeriesRun getRun(String scenarioName,
			String measurementEnvironmentUrl, String experimentName,
			Long timestamp) {
		ScenarioInstance instance;
		try {
			User user = UserManager.getUser(getSessionId());
			if (user == null) {
				throw new DataNotFoundException("No user at session found..");
			}
			instance = user.getCurrentPersistenceProvider()
					.loadScenarioInstance(scenarioName,
							measurementEnvironmentUrl);
			ExperimentSeries series = instance
					.getExperimentSeries(experimentName);
			for (ExperimentSeriesRun r : series.getExperimentSeriesRuns()) {
				if (timestamp.equals(r.getTimestamp())) {
					return r;
				}
			}
		} catch (DataNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ChartParameter[] getChartParameter(
			SharedExperimentRuns experiementRun) {
		ExperimentSeriesRun run = getRun(experiementRun);
		DataSetAggregated dataset = run.getSuccessfulResultDataSet();
		List<ChartParameter> chartParameters = new ArrayList<ChartParameter>();
		for (ParameterDefinition pd : dataset.getParameterDefinitions()) {
			int role = 0;
			switch (pd.getRole()) {
			case OBSERVATION:
				role = ChartParameter.OBSERVATION;
				break;
			case INPUT:
				role = ChartParameter.INPUT;
				break;
			}
			ChartParameter chartParameter = new ChartParameter();
			chartParameter.setParameterName(pd.getFullName());
			chartParameter.setType(role);
			chartParameters.add(chartParameter);
		}
		return chartParameters.toArray(new ChartParameter[chartParameters
				.size()]);
	}

	@Override
	public Void deleteVisualization(Visualization visualization) {
		System.out.println("deleting chart...");
		UiPersistence.getUiProvider().removeVisualization(visualization);
		return null;
	}
	
	private void loadExtension(String name){
		if (name.equals(G_CHARTS)){
			chartCreator = new GCharts();
			return;
		}
		for (IChartConnectionExtension ex : extensions){
			if (ex.getName().equals(name)){
				chartCreator = ex.createExtensionArtifact();
				return;
			}
		}
	}

	@Override
	public List<String> getExtensions() {
		List<String> extensionNames = new ArrayList<String>();
		extensionNames.add(G_CHARTS);
		for (IChartConnectionExtension ex : extensions){
			extensionNames.add(ex.getName());
		}
		return extensionNames;
	}

	@Override
	public Map<Double, List<Double>> applySplineInterpolation(
			Map<Double, List<Double>> values, double min, double max, double step) {
		List<Double> xValues = new ArrayList<Double>();
		List<Double> yValues = new ArrayList<Double>();
		min = values.entrySet().iterator().next().getKey();
		max = min;
		step = (step < 2) ? 2 : step;
		for (Entry<Double, List<Double>> entry : values.entrySet()){
			min = (entry.getKey() < min) ? entry.getKey() : min;
			max = (entry.getKey() > max) ? entry.getKey() : max;
			yValues.addAll(entry.getValue());
			for (int i = 0; i < entry.getValue().size(); i++){
				xValues.add(entry.getKey());
			}
		}
		UnivariateInterpolator interpolator = new SplineInterpolator();
		UnivariateFunction function = interpolator.interpolate(ArrayUtils.toPrimitive(xValues.toArray(new Double[xValues.size()])), ArrayUtils.toPrimitive(yValues.toArray(new Double[yValues.size()])));
		values.clear();
		for (double d = min; d < max; d += (max-min)/step){
			values.put(d, new ArrayList<Double>());
			values.get(d).add(function.value(d));
		}
		return values;
	}
}
