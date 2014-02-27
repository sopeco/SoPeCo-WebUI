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
package org.sopeco.webui.server.rpc.scenario;

import java.util.List;

import javax.validation.constraints.Null;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.service.rest.exchange.MECStatus;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.shared.helper.MEControllerProtocol;
import org.sopeco.webui.shared.rpc.MEControllerRPC;

import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerRPCImpl extends SPCRemoteServlet implements MEControllerRPC {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MEControllerRPCImpl.class);

	@Override
	public int checkControllerStatus(String url) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEC,
						     								 ServiceConfiguration.SVC_MEC_STATUS);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_URL, url);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			throw new IllegalStateException("Fetching controller status failed.");
		}
		
		return r.readEntity(MECStatus.class).getStatus();
	}


	/**
	 * Checks if the given url is like a valid pattern.
	 * 
	 * @param url
	 * @return
	 */
	private boolean checkUrlIsValid(String url) {
		String[] patterns = getValidUrlPattern();
		
		for (String pattern : patterns) {
			if (url.matches(pattern)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String[] getValidUrlPattern() {
		requiredLoggedIn(); // why log in is needed?
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEC,
						 									 ServiceConfiguration.SVC_MEC_VALIDATE);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			return new String[] { "" };
		}
		
		return r.readEntity(String[].class);
	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinitionFromMEC(String controllerUrl) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEC,
						 									 ServiceConfiguration.SVC_MEC_MED);

		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_URL, controllerUrl);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			throw new IllegalStateException("Fetching MED from MEC failed.");
		}
		
		return r.readEntity(MeasurementEnvironmentDefinition.class);
	}

	@Override
	public MeasurementEnvironmentDefinition getBlankMEDefinition() {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_SET,
															 ServiceConfiguration.SVC_MED_SET_BLANK);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.info("Creating blank MED failed.");
			return null;
		}

		return r.readEntity(MeasurementEnvironmentDefinition.class);
	}

	private void setNewMEDefinition(MeasurementEnvironmentDefinition definition) {
		LOGGER.debug("Set a new environment definition.");
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_SET);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		
		wt.request(MediaType.APPLICATION_JSON).post(Entity.entity(definition, MediaType.APPLICATION_JSON));
	}

	@Override
	public MeasurementEnvironmentDefinition getCurrentMEDefinition() {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
						 									 ServiceConfiguration.SVC_MED_CURRENT);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();

		LOGGER.debug("getCurrentMEDefinition");
		
		return r.readEntity(MeasurementEnvironmentDefinition.class);
	}

	@Override
	public boolean addNamespace(String path) {
		requiredLoggedIn();

		LOGGER.debug("getCurrentMEDefinition");
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
						 									 ServiceConfiguration.SVC_MED_NAMESPACE,
						 									 ServiceConfiguration.SVC_MED_NAMESPACE_ADD);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, path);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean removeNamespace(String path) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_NAMESPACE,
															 ServiceConfiguration.SVC_MED_NAMESPACE_REMOVE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, path);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).delete();

		if (r.getStatus() == Status.CONFLICT.getStatusCode()) {
			LOGGER.debug(r.readEntity(String.class));
			return false;
		}

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean renameNamespace(String namespacePath, String newName) {
		requiredLoggedIn();
		
		LOGGER.debug("rpc: renameNamespace: {}", namespacePath);

		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_NAMESPACE,
															 ServiceConfiguration.SVC_MED_NAMESPACE_RENAME);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, namespacePath);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE_NEW, newName);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));

		if (r.getStatus() == Status.CONFLICT.getStatusCode()) {
			LOGGER.debug(r.readEntity(String.class));
			return false;
		}

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean addParameter(String path, String name, String type, ParameterRole role) {
		requiredLoggedIn();
		
		LOGGER.debug("rpc: addParameter: {} to '{}'", name, path);

		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
				 											 ServiceConfiguration.SVC_MED_PARAM,
				 											 ServiceConfiguration.SVC_MED_PARAM_ADD);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, path);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_NAME, name);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_TYP, type);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(role, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() == Status.CONFLICT.getStatusCode()) {
			LOGGER.debug(r.readEntity(String.class));
		}
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean removeParameter(String path, String name) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_PARAM,
															 ServiceConfiguration.SVC_MED_PARAM_REMOVE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, path);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_NAME, name);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).delete();

		if (r.getStatus() == Status.CONFLICT.getStatusCode()) {
			LOGGER.debug(r.readEntity(String.class));
			return false;
		}

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean updateParameter(String path, String oldName, String newName, String type, ParameterRole role) {
		requiredLoggedIn();

		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MED,
															 ServiceConfiguration.SVC_MED_PARAM,
															 ServiceConfiguration.SVC_MED_PARAM_UPDATE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_NAMESPACE, path);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_NAME, oldName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_NAME_NEW, newName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MED_PARAM_TYP, type);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(role, MediaType.APPLICATION_JSON));

		if (r.getStatus() == Status.CONFLICT.getStatusCode()) {
			LOGGER.debug(r.readEntity(String.class));
			return false;
		}

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean isPortReachable(String host, int port) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEC,
															 ServiceConfiguration.SVC_MEC_PORTREACHABLE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_HOST, host);
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_PORT, port);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() == Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
			LOGGER.debug("Failed to connect to host and port combination.");
			return false;
		}
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public List<String> getController(MEControllerProtocol protocol, String host, int port) {
		requiredLoggedIn();
		
		LOGGER.warn("This method is not supported, as only Socket protocol is supported.");
		
		return null;
	}
}
