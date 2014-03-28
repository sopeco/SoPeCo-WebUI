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
package org.sopeco.webui.server.rpc;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.shared.helper.MEControllerProtocol;
import org.sopeco.webui.shared.rpc.GetRPC;

/**
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class GetRPCImpl extends SPCRemoteServlet implements GetRPC {

	private static final long serialVersionUID = 1L;

	/**
	 * Always returns null.
	 * 
	 * @deprecated Method is not used in current build and not implemented in RESTful
	 * service interface for SoPeCo.
	 */
	@Override
	@Deprecated
	public Map<String, String[]> getConnectedSocketController() {
		return null;
	}

	/**
	 * This method is just stuffed with a call to get connected Socket controllers. Actually
	 * this method does not support REST or RMI.
	 * 
	 * @param protocol the protocol
	 * @param host		the host, it's actually the ID of the mec (because only sockets are supported)
	 */
	@Override
	public List<String> getControllerFromMEC(MEControllerProtocol protocol, String host, int port) {
		requiredLoggedIn();
		
		// TODO: remove suppoprt for old protocols!
		if (protocol != MEControllerProtocol.SOCKET) {
			return null;
		}
		
		String mec_id = host; // just to make clear, that host MUST be the mec_id here
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_MEC,
							 								 ServiceConfiguration.SVC_MEC_LIST);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		wt = wt.queryParam(ServiceConfiguration.SVCP_MEC_ID, mec_id);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
				
			return null;
		}
		
		return r.readEntity(new GenericType<List<String>>() { });
	}
}
