package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.rpc.ResultRPC;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentSeries;
import org.sopeco.frontend.shared.definitions.result.SharedScenarioInstance;
import org.sopeco.persistence.dataset.DataSetAggregated;
import org.sopeco.persistence.dataset.SimpleDataSet;
import org.sopeco.persistence.entities.ExperimentSeries;
import org.sopeco.persistence.entities.ExperimentSeriesRun;
import org.sopeco.persistence.entities.ScenarioInstance;
import org.sopeco.persistence.exceptions.DataNotFoundException;
import org.sopeco.persistence.util.DataSetCsvHandler;

public class ResultRPCImpl extends SuperRemoteServlet implements ResultRPC {

	/**	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void getResults() {
		// getUser().getCurrentPersistenceProvider().loadAllScenarioInstances().get(0).getExperimentSeriesList().get(0).getLatestExperimentSeriesRun().getSuccessfulResultDataSet().

		try {
			List<ScenarioInstance> instances = getUser().getCurrentPersistenceProvider().loadAllScenarioInstances();

			System.out.println("");

			DataSetAggregated d = new DataSetAggregated();

			SimpleDataSet sd = d.convertToSimpleDataSet();

			DataSetCsvHandler handler = new DataSetCsvHandler(';', '#', true);
			// handler.store(dataset, fileName)
		} catch (DataNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<SharedScenarioInstance> getInstances(String scenarioName) {
		try {
			List<ScenarioInstance> scenarioList = getUser().getCurrentPersistenceProvider().loadScenarioInstances(scenarioName);

			List<SharedScenarioInstance> retList = new ArrayList<SharedScenarioInstance>();
			for (ScenarioInstance instance : scenarioList) {
				retList.add(convertInstance(instance));
			}
			return retList;
		} catch (DataNotFoundException e) {
			return new ArrayList<SharedScenarioInstance>();
		}
	}

	/**
	 * Creates a SharedScenarioInstance out of a ScenarioInstance which can send
	 * to the FrontEnd.
	 * 
	 * @param instance
	 * @return
	 */
	private SharedScenarioInstance convertInstance(ScenarioInstance instance) {
		SharedScenarioInstance retInstance = new SharedScenarioInstance();

		retInstance.setScenarioName(instance.getName());
		retInstance.setControllerUrl(instance.getMeasurementEnvironmentUrl());

		for (ExperimentSeries series : instance.getExperimentSeriesList()) {
			SharedExperimentSeries sharedSeries = new SharedExperimentSeries();
			sharedSeries.setExperimentName(series.getName());
			sharedSeries.setVersion(series.getVersion());

			for (ExperimentSeriesRun run : series.getExperimentSeriesRuns()) {
				SharedExperimentRuns sharedRun = new SharedExperimentRuns();
				sharedRun.setTimestamp(run.getTimestamp());
				sharedRun.setLabel(run.getLabel());

				sharedSeries.addExperimentRun(sharedRun);
			}

			retInstance.addExperimentSeries(sharedSeries);
		}

		return retInstance;
	}
}
