package org.sopeco.frontend.client.rpc;

import java.util.Collection;

import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("visualizationRPC")
public interface VisualizationRPC extends RemoteService {

	Visualization getChart(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter, ChartOptions options);
	
	Double[][] getDataSet(SharedExperimentRuns experiementRun, ChartParameter[] chartParameter);
	
	ChartParameter[] getChartParameter(SharedExperimentRuns experiementRun);
	
	Visualization[] getAllVisualizations();
}
