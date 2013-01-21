package org.sopeco.frontend.shared.helper;

/**
 * Enumeration of the supported protocols, which can be used to communicate with
 * the controller.
 */
public enum MEControllerProtocol {
	/** Connection to the MEController via RMI. */
	RMI, 
	/** REST Connection to the MEController via HTTP. */
	REST_HTTP
}
