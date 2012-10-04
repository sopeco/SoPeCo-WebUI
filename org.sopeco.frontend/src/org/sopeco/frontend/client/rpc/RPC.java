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
	private static SystemDetailsRPCAsync systemDetails = null;

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

	public static SystemDetailsRPCAsync getSystemDetailsRPC() {
		if (systemDetails == null) {
			systemDetails = GWT.create(SystemDetailsRPC.class);
		}

		return systemDetails;
	}
}
