package org.sopeco.frontend.client.model;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.AccountDetails;
import org.sopeco.frontend.shared.entities.ScenarioDetails;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

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
	private List<DatabaseInstance> availableDatabases;

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
		RPC.getDatabaseManagerRPC().storeAccountDetails(accountDetails, new AsyncCallback<Void>() {
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
	 * object exists, it creates one.
	 * 
	 * @return
	 */
	public ScenarioDetails getCurrentScenarioDetails() {
		if (accountDetails.getScenarioDetail(accountDetails.getSelectedScenario()) == null) {
			Manager.get().getAccountDetails().addScenarioDetails(accountDetails.getSelectedScenario());
			Manager.get().storeAccountDetails();
		}

		return accountDetails.getScenarioDetail(accountDetails.getSelectedScenario());
	}

	public List<DatabaseInstance> getAvailableDatabases() {
		return availableDatabases;
	}

	public void setAvailableDatabases(List<DatabaseInstance> pAvailableDatabases) {
		this.availableDatabases = pAvailableDatabases;
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

		return getCurrentScenarioDetails().getControllerProtocol() + getCurrentScenarioDetails().getControllerHost()
				+ ":" + getCurrentScenarioDetails().getControllerPort() + "/"
				+ getCurrentScenarioDetails().getControllerName();
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
