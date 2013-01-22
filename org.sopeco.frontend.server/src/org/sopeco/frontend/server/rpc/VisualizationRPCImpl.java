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
import java.util.List;

import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.frontend.client.rpc.VisualizationRPC;
import org.sopeco.frontend.server.chartconnector.IChartConnection;
import org.sopeco.frontend.server.chartconnector.IChartConnectionExtension;
import org.sopeco.frontend.server.chartconnector.IChartConnection.ChartTypes;
import org.sopeco.frontend.server.gcharts.GCharts;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.ParameterValue;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.dataset.SimpleDataSetColumn;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

public class VisualizationRPCImpl extends SuperRemoteServlet implements
		VisualizationRPC {
//	private IChartConnection chartCreator = ExtensionRegistry.getSingleton().getExtensionArtifact(IChartConnectionExtension.class, "PAP Connection");
	private IChartConnection chartCreator = new GCharts();

	@Override
	public Visualization getChart(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter, ChartOptions options) {
		Double[][] data;
		String[] columnNames = new String[chartParameter.length];
		data = new Double[chartParameter.length][];
		ExperimentSeriesRun run = getRun(experiementRun);
		DataSetAggregated dataset = run.getSuccessfulResultDataSet();
		SimpleDataSet simpledata = dataset.convertToSimpleDataSet();
		int i = 0;
		int j = 0;
		for (ChartParameter cp : chartParameter){
			columnNames[j] = cp.getParameterName();
			for (SimpleDataSetColumn<?> dataSetColumn : simpledata.getColumns()) {
				if (dataSetColumn.getParameter().getFullName().equals(cp.getParameterName())){
					List<ParameterValue<?>> values = dataSetColumn.getParameterValues();
					data[i] = new Double[values.size()];
					for (int k = 0; k < values.size(); k++){
						data[i][k] = values.get(k).getValueAsDouble();
					}
					i++;
				}
			}
			j++;
		}
		ChartTypes type;
		switch (options.getType()){
		case BARCHART:
			type = ChartTypes.BAR_CHART;
			break;
		case PIECHART:
			type = ChartTypes.PIE_CHART;
			break;
		default:
			type = ChartTypes.LINE_CHART;
		}
		System.out.println("type2: " + type);
		return chartCreator.getChartHTML(experiementRun.getLabel(), data, columnNames, type);

	}
	
	@Override
	public Visualization[] getAllVisualizations() {
		return chartCreator.getAllVisualizations();
	}

	@Override
	public Double[][] getDataSet(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter) {
		
		return new Double[0][0];
		
		
//		List<ParameterDefinition> chartParameters = new ArrayList<ParameterDefinition>();
//		int j = 0;
//		List<ParameterDefinition> param = new ArrayList<ParameterDefinition>(dataset.getParameterDefinitions());
//		for (int i = 0; i < param.size(); i++){
//			ParameterDefinition pd = param.get(i);
//			System.out.println("param: " + pd.getFullName());
//			if (pd.getFullName().equals(chartParameter[j].getParameterName())){
//				chartParameters.add(pd);
//				j++;
//			}
//			
//		}
//		DataSetAggregated subset = dataset.getSubSet(chartParameters);
//		for (ParameterDefinition pd : dataset.getParameterDefinitions()){
//			System.out.println("PrameterDefinition: " + pd.getFullName());
//		}
//		SimpleDataSet simpleset = dataset.convertToSimpleDataSet();
//		List<SimpleDataSetColumn> list = new ArrayList<SimpleDataSetColumn>(simpleset.getColumns());
//		data = new Double[list.size()][];
//		for (int i = 0; i < list.size(); i ++){
//			SimpleDataSetColumn<?> col = list.get(i);
//			List<ParameterValue<?>> values = col.getParameterValues();
//			data[i] = new Double[values.size()];
//			for (int k = 0; k < values.size(); k++) {
//				data[i][k] = values.get(k).getValueAsDouble();
//			}
//		}
//		return data;
//		Double[][] d = subset.convertToDoubleArray();
//		System.out.println("data: " + Arrays.toString(d));
//		return d;
	}
	
	public ExperimentSeriesRun getRun(SharedExperimentRuns experiementRun){
		ScenarioInstance instance;
		try {
			User user = UserManager.getUser(getSessionId());
			if (user == null) {
				throw new DataNotFoundException("No user at session found..");
			}
			instance = user.getCurrentPersistenceProvider()
					.loadScenarioInstance(
							experiementRun.getParentSeries()
									.getParentInstance().getScenarioName(),
							experiementRun.getParentSeries()
									.getParentInstance().getControllerUrl());
			ExperimentSeries series = instance
					.getExperimentSeries(experiementRun.getParentSeries()
							.getExperimentName());
			for (ExperimentSeriesRun r : series.getExperimentSeriesRuns()) {
				if (experiementRun.getTimestamp() == r.getTimestamp()) {
					return r;
				}
			}
		} catch (DataNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public ChartParameter[] getChartParameter(SharedExperimentRuns experiementRun){
		ExperimentSeriesRun run = getRun(experiementRun);
		DataSetAggregated dataset = run.getSuccessfulResultDataSet();
		List<ChartParameter> chartParameters = new ArrayList<ChartParameter>();
		for (ParameterDefinition pd : dataset.getParameterDefinitions()){
			int role = 0;
			switch(pd.getRole()){
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
		return chartParameters.toArray(new ChartParameter[chartParameters.size()]);
	}
}
