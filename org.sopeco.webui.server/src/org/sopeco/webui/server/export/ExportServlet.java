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
package org.sopeco.webui.server.export;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.security.Security;
import org.sopeco.webui.server.user.TokenManager;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String SCNEARIO_EXPORT = "scenario";

	private HttpSession session;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Security.requiredLoggedIn(req);

		session = req.getSession();

		if (req.getParameter(SCNEARIO_EXPORT) != null) {
			sendScenarioAsXML(resp, session.getId());
		} else {
			resp.sendError(204);
		}
	}

	/**
	 * 
	 * @param resp
	 * @throws IOException
	 */
	private void sendScenarioAsXML(HttpServletResponse resp, String sessionId) throws IOException {
		

		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_SCENARIO,
				     										 ServiceConfiguration.SVC_SCENARIO_CURRENT);

		wt = wt.queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, TokenManager.instance().getToken(sessionId));
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		ScenarioDefinition sd = r.readEntity(ScenarioDefinition.class);
		
		if (sd == null) {
			resp.sendError(204);
			return;
		}
		
		wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_SCENARIO,
				     										 ServiceConfiguration.SVC_SCENARIO_XML);

		wt = wt.queryParam(ServiceConfiguration.SVCP_SCENARIO_TOKEN, TokenManager.instance().getToken(sessionId));
		
		r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			resp.sendError(204);
			return;
		}
		
		String definitionXML = r.readEntity(String.class);

		String fileName = "scenario-" + sd.getScenarioName() + ".xml";

		sendXML(resp, definitionXML, fileName);

	}

	/**
	 * 
	 * @param resp
	 * @param xmlContent
	 * @throws IOException
	 */
	private void sendXML(HttpServletResponse resp, String xmlContent, String fileName) throws IOException {

		resp.setContentType("text/xml");
		resp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		resp.setContentLength((int) xmlContent.length());

		resp.getWriter().write(xmlContent);
		resp.flushBuffer();
	}
}
