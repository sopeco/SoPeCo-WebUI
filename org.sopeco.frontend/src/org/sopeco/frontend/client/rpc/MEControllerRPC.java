package org.sopeco.frontend.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("meControllerRPC")
public interface MEControllerRPC extends RemoteService {

	/**
	 * Checked controller is offline.
	 */
	int STATUS_OFFLINE = 0;
	/**
	 * Checked controller is online.
	 */
	int STATUS_ONLINE = 1;

	/**
	 * Returns all existing Controller URLs.
	 * 
	 * @return List of Controller URLs
	 */
	List<String> getMEControllerList();

	/**
	 * Checks the current status of a controller.
	 * 
	 * @param url
	 *            controller url
	 * @return staus
	 */
	int checkControllerStatus(String url);

}
