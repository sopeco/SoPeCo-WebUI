package org.sopeco.frontend.client.rpc;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("scenarioManagerRPC")
public interface ScenarioManagerRPC extends RemoteService {

	/**
	 * Return a String-Array with all existing scenario names.
	 * 
	 * @return scenario names
	 */
	String[] getScenarioNames();

	boolean addScenario(String name);

	boolean removeScenario(String name);

	boolean switchScenario(String name);

	ScenarioDefinition getCurrentScenarioDefinition();

	boolean storeScenarioDefinition(ScenarioDefinition definition);

	String getScenarioAsXML();

}
