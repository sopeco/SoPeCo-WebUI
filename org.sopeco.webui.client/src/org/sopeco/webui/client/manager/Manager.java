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
package org.sopeco.webui.client.manager;

import java.util.logging.Logger;

import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.shared.entities.ScenarioDetails;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Manager {

	private static final Logger LOGGER = Logger.getLogger(Manager.class.getName());

	private static Manager manager;

	/** Possible status of an MEController. */
	public enum ControllerStatus {
		/** Controller is online. */
		ONLINE,
		/** Controller is offline. */
		OFFLINE,
		/** Unknown status. E.g: controller was not checked. */
		UNKNOWN
	}

	/** Attributes with default values */
	private long controllerLastCheck = -1;
	private ControllerStatus controllerLastStatus = ControllerStatus.UNKNOWN;
	private AccountDetails accountDetails = null;
	private String selectedExperiment;
	private DatabaseInstance currentDatabaseInstance;
	private int selectedDatabaseIndex = -1;

	public void reset() {
		selectedExperiment = null;
		accountDetails = null;
		controllerLastStatus = ControllerStatus.UNKNOWN;
		controllerLastCheck = -1;
	}

	/**
	 * Sends the current AccountDetails to the backend to store it in the
	 * database.
	 */
	public void storeAccountDetails() {
		LOGGER.info("Store AccountDetails");
		RPC.getAccountManagementRPC().storeAccountDetails(accountDetails, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/**
	 * Returns the ScenariODetail object of the current selected scenario. If no
	 * object exists, it returns null.
	 * 
	 * @return
	 */
	public ScenarioDetails getCurrentScenarioDetails() {
		if (accountDetails.getScenarioDetail(accountDetails.getSelectedScenario()) == null) {
			return null;
		}

		return accountDetails.getScenarioDetail(accountDetails.getSelectedScenario());
	}

	public void setSelectedDatabaseIndex(int selectedDatabaseIndex) {
		this.selectedDatabaseIndex = selectedDatabaseIndex;
	}

	/**
	 * @return the accountDetails
	 */
	public AccountDetails getAccountDetails() {
		return accountDetails;
	}

	/**
	 * @return the selectedExperiment
	 */
	public String getSelectedExperiment() {
		return selectedExperiment;
	}

	/**
	 * @param pSelectedExperiment
	 *            the selectedExperiment to set
	 */
	public void setSelectedExperiment(String pSelectedExperiment) {
		this.selectedExperiment = pSelectedExperiment;
	}

	/**
	 * @param pAccountDetails
	 *            the accountDetails to set
	 */
	public void setAccountDetails(AccountDetails pAccountDetails) {
		this.accountDetails = pAccountDetails;
	}

	/**
	 * The full compounded URL of the MEController.
	 * 
	 * @return
	 */
	public String getControllerUrl() {
		if (getCurrentScenarioDetails() == null) {
			return "";
		}

		String url = getCurrentScenarioDetails().getControllerProtocol()
				+ getCurrentScenarioDetails().getControllerHost();
		if (getCurrentScenarioDetails().getControllerPort() > 0) {
			url += ":" + getCurrentScenarioDetails().getControllerPort();
		}
		url += "/" + getCurrentScenarioDetails().getControllerName();
		return url;
	}

	/**
	 * @return the controllerLastStatus
	 */
	public ControllerStatus getControllerLastStatus() {
		return controllerLastStatus;
	}

	/**
	 * @param pControllerLastStatus
	 *            the controllerLastStatus to set
	 */
	public void setControllerLastStatus(ControllerStatus pControllerLastStatus) {
		this.controllerLastStatus = pControllerLastStatus;
	}

	/**
	 * @return the controllerLastCheck
	 */
	public long getControllerLastCheck() {
		return controllerLastCheck;
	}

	/**
	 * @param pControllerLastCheck
	 *            the controllerLastCheck to set
	 */
	public void setControllerLastCheck(long pControllerLastCheck) {
		this.controllerLastCheck = pControllerLastCheck;
	}

	private Manager() {
	}

	public static Manager get() {
		if (manager == null) {
			manager = new Manager();
		}
		return manager;
	}
}
