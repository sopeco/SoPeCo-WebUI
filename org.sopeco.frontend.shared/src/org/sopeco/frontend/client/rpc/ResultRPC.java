package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.definitions.result.SharedScenarioInstance;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("resultRPC")
public interface ResultRPC extends RemoteService {

	void getResults();

	List<SharedScenarioInstance> getInstances(String scenarioName);
	
	String getResultAsR(String scenario, String exoerimentSeries, String url, long timestamp);
}
