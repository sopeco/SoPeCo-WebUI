/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.shared.rpc;

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
	private static VisualizationRPCAsync visualizationRPC = null;
	private static GetRPCAsync getRPC = null;
	private static AccountManagementRPCAsync accountManagementRPC = null;

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

	/**
	 * Returns a instance of the VisualizationRPC.
	 * 
	 * @return
	 */
	public static VisualizationRPCAsync getVisualizationRPC() {
		if (visualizationRPC == null) {
			visualizationRPC = GWT.create(VisualizationRPC.class);
		}

		return visualizationRPC;
	}

	/**
	 * Returns a instance of the GetRPC.
	 * 
	 * @return
	 */
	public static GetRPCAsync getGetRPC() {
		if (getRPC == null) {
			getRPC = GWT.create(GetRPC.class);
		}

		return getRPC;
	}

	/**
	 * Returns a instance of the AccountManagementRPC.
	 * 
	 * @return
	 */
	public static AccountManagementRPCAsync getAccountManagementRPC() {
		if (accountManagementRPC == null) {
			accountManagementRPC = GWT.create(AccountManagementRPC.class);
		}
		return accountManagementRPC;
	}

	/**
	 * @param getRPC the getRPC to set
	 */
	public static void setGetRPC(GetRPCAsync getRPC) {
		RPC.getRPC = getRPC;
	}

}
