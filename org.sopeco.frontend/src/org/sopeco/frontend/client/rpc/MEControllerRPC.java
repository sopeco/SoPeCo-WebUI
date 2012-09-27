package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;

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
	 * Checked controller is online but can't return any information about the
	 * me.
	 */
	int STATUS_ONLINE_NO_META = 2;
	/**
	 * The given url is not valid.
	 */
	int NO_VALID_MEC_URL = 3;

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

	/**
	 * Returns a String array with valid url patterns.
	 * 
	 * @return string array
	 */
	String[] getValidUrlPattern();

	MeasurementEnvironmentDefinition getMEDefinitionFromMEC(String controllerUrl);
	
	MeasurementEnvironmentDefinition getBlankMEDefinition();
}
