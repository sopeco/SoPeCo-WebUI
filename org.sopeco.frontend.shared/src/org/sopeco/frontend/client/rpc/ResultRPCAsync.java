package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.definitions.result.SharedScenarioInstance;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ResultRPCAsync {
	void getResults(AsyncCallback<Void> callback);

	void getInstances(String scenarioName, AsyncCallback<List<SharedScenarioInstance>> callback);

	void getResultAsR(String scenario, String exoerimentSeries, String url, long timestamp,
			AsyncCallback<String> callback);
}
