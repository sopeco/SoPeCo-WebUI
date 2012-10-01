package org.sopeco.frontend.client.helper.serverstatus;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface Deactivatable {
	/**
	 * Executed when the connection to the required database is closed or
	 * interrupted.
	 */
	void goOffline();

	/**
	 * Executed when the connection to the required database is online.
	 */
	void goOnline();
}
