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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.shared.rpc.MSpecificationRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MSpecificationRPCImpl extends SPCRemoteServlet implements MSpecificationRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(MSpecificationRPCImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> getAllSpecificationNames() {
		requiredLoggedIn();
		
		LOGGER.debug("Try to fetch all measurement specification names from SPC SL.");
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEASUREMENT,
					     									 ServiceConfiguration.SVC_MEASUREMENT_LIST);
		
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		List<String> list = r.readEntity(new GenericType<List<String>>() { });

		System.out.println("+++++++++++++++++++++++++++++++++++++++++1");
		System.out.println("size: " + list.size());
		
		for (String s : list) {
			System.out.println("s: " + s);
		}
		
		return list;
	}

	@Override
	public List<MeasurementSpecification> getAllSpecifications() {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEASUREMENT,
															 ServiceConfiguration.SVC_MEASUREMENT_LISTSPECS);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		List<MeasurementSpecification> list = r.readEntity(new GenericType<List<MeasurementSpecification>>() { });
		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++1");
		System.out.println("size: " + list.size());
		
		for (MeasurementSpecification s : list) {
			System.out.println("s: " + s.getName());
		}
		
		return list;
	}

	@Override
	public boolean setWorkingSpecification(String specificationName) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEASUREMENT,
						 									 ServiceConfiguration.SVC_MEASUREMENT_SWITCH);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_SPECNAME, specificationName);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean createSpecification(String name) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEASUREMENT,
						 									 ServiceConfiguration.SVC_MEASUREMENT_CREATE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_SPECNAME, name);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).post(Entity.entity(Null.class, MediaType.APPLICATION_JSON));

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean renameWorkingSpecification(String newName) {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEASUREMENT,
						 									 ServiceConfiguration.SVC_MEASUREMENT_RENAME);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEASUREMENT_SPECNAME, newName);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));

		return r.getStatus() == Status.OK.getStatusCode();
	}
}
