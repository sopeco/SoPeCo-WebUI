package org.sopeco.frontend.client.rpc;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ScenarioManagerRPCAsync {

	/**
	 * Return a String-Array with all existing scenario names.
	 * 
	 * @param callback
	 */
	void getScenarioNames(AsyncCallback<String[]> callback);

	void addScenario(String name, AsyncCallback<Boolean> callback);

	void removeScenario(String name, AsyncCallback<Boolean> callback);

	void switchScenario(String name, AsyncCallback<Boolean> callback);

	void getCurrentScenarioDefinition(AsyncCallback<ScenarioDefinition> callback);

	void storeScenarioDefinition(ScenarioDefinition definition, AsyncCallback<Boolean> callback);

	void getScenarioAsXML(AsyncCallback<String> callback);
}
