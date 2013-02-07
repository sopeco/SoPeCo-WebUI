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
package org.sopeco.webui.client.rpc;

import java.util.List;

import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.webui.shared.helper.MEControllerProtocol;

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
	// List<String> getMEControllerList();

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

	/**
	 * 
	 * @param controllerUrl
	 * @return
	 */
	MeasurementEnvironmentDefinition getMEDefinitionFromMEC(String controllerUrl);

	/**
	 * 
	 * @return
	 */
	MeasurementEnvironmentDefinition getBlankMEDefinition();

	MeasurementEnvironmentDefinition getCurrentMEDefinition();

	boolean addNamespace(String path);

	boolean removeNamespace(String path);

	boolean renameNamespace(String namespacePath, String newName);

	boolean addParameter(String path, String name, String type, ParameterRole role);

	boolean removeParameter(String path, String name);

	boolean updateParameter(String path, String oldName, String newName, String type, ParameterRole role);

	boolean isPortReachable(String host, int port);

	List<String> getController(MEControllerProtocol protocol, String host, int port);
}
