package org.sopeco.frontend.client.rpc;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class RPC {

	private RPC() {
	}

	private static ScenarioManagerRPCAsync scenarioManager = GWT.create(ScenarioManagerRPC.class);

	public static ScenarioManagerRPCAsync getScenarioManager() {
		return scenarioManager;
	}
}
