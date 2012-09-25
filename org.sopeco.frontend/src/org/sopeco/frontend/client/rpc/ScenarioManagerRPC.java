package org.sopeco.frontend.client.rpc;

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
}
