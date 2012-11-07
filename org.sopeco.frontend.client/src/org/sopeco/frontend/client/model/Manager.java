package org.sopeco.frontend.client.model;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Manager {
	private static Manager manager;

	public enum ControllerStatus {
		ONLINE, OFFLINE, UNKNOWN
	}

	/** Attributes with default values */
	private String controllerProtocol = "rmi://";
	private String controllerHost = "localhost";
	private String controllerName = "";
	private int controllerPort = 1099;
	private long controllerLastCheck = -1;
	private ControllerStatus controllerLastStatus = ControllerStatus.UNKNOWN;

	public String getControllerUrl() {
		return controllerProtocol + controllerHost + ":" + controllerPort + "/" + controllerName;
	}

	/**
	 * @return the controllerProtocol
	 */
	public String getControllerProtocol() {
		return controllerProtocol;
	}

	/**
	 * @return the controllerLastStatus
	 */
	public ControllerStatus getControllerLastStatus() {
		return controllerLastStatus;
	}

	/**
	 * @param controllerLastStatus
	 *            the controllerLastStatus to set
	 */
	public void setControllerLastStatus(ControllerStatus controllerLastStatus) {
		this.controllerLastStatus = controllerLastStatus;
	}

	/**
	 * @return the controllerLastCheck
	 */
	public long getControllerLastCheck() {
		return controllerLastCheck;
	}

	/**
	 * @param controllerLastCheck
	 *            the controllerLastCheck to set
	 */
	public void setControllerLastCheck(long controllerLastCheck) {
		this.controllerLastCheck = controllerLastCheck;
	}

	/**
	 * @param pControllerProtocol
	 *            the controllerProtocol to set
	 */
	public void setControllerProtocol(String pControllerProtocol) {
		this.controllerProtocol = pControllerProtocol;
	}

	/**
	 * @return the controllerHost
	 */
	public String getControllerHost() {
		return controllerHost;
	}

	/**
	 * @param pControllerHost
	 *            the controllerHost to set
	 */
	public void setControllerHost(String pControllerHost) {
		this.controllerHost = pControllerHost;
	}

	/**
	 * @return the controllerName
	 */
	public String getControllerName() {
		return controllerName;
	}

	/**
	 * @param pControllerName
	 *            the controllerName to set
	 */
	public void setControllerName(String pControllerName) {
		this.controllerName = pControllerName;
	}

	/**
	 * @return the controllerPort
	 */
	public int getControllerPort() {
		return controllerPort;
	}

	/**
	 * @param pControllerPort
	 *            the controllerPort to set
	 */
	public void setControllerPort(int pControllerPort) {
		this.controllerPort = pControllerPort;
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
