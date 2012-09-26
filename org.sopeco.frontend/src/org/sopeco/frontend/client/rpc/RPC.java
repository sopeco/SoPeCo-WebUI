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

	private static ScenarioManagerRPCAsync scenarioManager = null;
	private static MEControllerRPCAsync meController = null;

	/**
	 * Returns a instance of the ScenarioManagerRPCAsync.
	 * 
	 * @return
	 */
	public static ScenarioManagerRPCAsync getScenarioManager() {
		if (scenarioManager == null) {
			scenarioManager = GWT.create(ScenarioManagerRPC.class);
		}

		return scenarioManager;
	}

	/**
	 * Returns a instance of the MEControllerRPCAsync.
	 * 
	 * @return
	 */
	public static MEControllerRPCAsync getMEControllerRPC() {
		if (meController == null) {
			meController = GWT.create(MEControllerRPC.class);
		}

		return meController;
	}
}
