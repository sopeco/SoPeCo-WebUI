package org.sopeco.frontend.client.rpc;

import com.google.gwt.core.client.GWT;

/**
 * This class stores instances of all remote services.
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
	private static MSpecificationRPCAsync specificationRPC = null;
	private static ExtensionRPCAsync extensionRPC = null;
	private static ExecuteRPCAsync executeRPC = null;
	private static ResultRPCAsync resultRPC = null;

	/**
	 * Returns a instance of the ScenarioManagerRPCAsync.
	 * 
	 * @return
	 */
	public static ResultRPCAsync getResultRPC() {
		if (resultRPC == null) {
			resultRPC = GWT.create(ResultRPC.class);
		}

		return resultRPC;
	}
	
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

	/**
	 * Returns a instance of the SystemDetailsRPCAsync.
	 * 
	 * @return
	 */
	public static SystemDetailsRPCAsync getSystemDetailsRPC() {
		if (systemDetails == null) {
			systemDetails = GWT.create(SystemDetailsRPC.class);
		}

		return systemDetails;
	}

	/**
	 * Returns a instance of the MSpecificationRPCAsync.
	 * 
	 * @return
	 */
	public static MSpecificationRPCAsync getMSpecificationRPC() {
		if (specificationRPC == null) {
			specificationRPC = GWT.create(MSpecificationRPC.class);
		}

		return specificationRPC;
	}

	/**
	 * Returns a instance of the ExtensionRPCAsync.
	 * 
	 * @return
	 */
	public static ExtensionRPCAsync getExtensionRPC() {
		if (extensionRPC == null) {
			extensionRPC = GWT.create(ExtensionRPC.class);
		}

		return extensionRPC;
	}

	/**
	 * Returns a instance of the ExecuteRPCAsync.
	 * 
	 * @return
	 */
	public static ExecuteRPCAsync getExecuteRPC() {
		if (executeRPC == null) {
			executeRPC = GWT.create(ExecuteRPC.class);
		}

		return executeRPC;
	}
}
