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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface MEControllerRPCAsync {

	void checkControllerStatus(String url, AsyncCallback<Integer> callback);

	// void getMEControllerList(AsyncCallback<List<String>> callback);

	void getValidUrlPattern(AsyncCallback<String[]> callback);

	void getMEDefinitionFromMEC(String controllerUrl, AsyncCallback<MeasurementEnvironmentDefinition> callback);

	void getBlankMEDefinition(AsyncCallback<MeasurementEnvironmentDefinition> callback);

	void removeNamespace(String path, AsyncCallback<Boolean> callback);

	void addNamespace(String path, AsyncCallback<Boolean> callback);

	void getCurrentMEDefinition(AsyncCallback<MeasurementEnvironmentDefinition> callback);

	void renameNamespace(String namespacePath, String newName, AsyncCallback<Boolean> callback);

	void addParameter(String path, String name, String type, ParameterRole role, AsyncCallback<Boolean> callback);

	void removeParameter(String path, String name, AsyncCallback<Boolean> callback);

	void updateParameter(String path, String oldName, String newName, String type, ParameterRole role,
			AsyncCallback<Boolean> callback);

	void isPortReachable(String host, int port, AsyncCallback<Boolean> callback);

	void getController(MEControllerProtocol protocol, String host, int port,
			AsyncCallback<List<String>> callback);
}