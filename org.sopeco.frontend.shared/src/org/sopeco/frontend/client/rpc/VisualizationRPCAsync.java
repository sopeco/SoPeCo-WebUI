package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VisualizationRPCAsync {
	
	void getChart(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter, ChartOptions options, AsyncCallback<Visualization> callback);
	
	void getDataSet(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter, AsyncCallback<Double[][]> callback);
	
	void getChartParameter(SharedExperimentRuns experiementRun, AsyncCallback<ChartParameter[]> callback);
	
	void getAllVisualizations(AsyncCallback<Visualization[]> callback);
}
