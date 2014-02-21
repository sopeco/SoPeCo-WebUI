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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;
import org.sopeco.webui.server.helper.ServerCheck;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.shared.helper.MEControllerProtocol;
import org.sopeco.webui.shared.rpc.GetRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class GetRPCImpl extends SPCRemoteServlet implements GetRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @deprecated Method is not used in current build and not implemented in RESTful
	 * service interface for SoPeCo.
	 */
	@Override
	@Deprecated
	public Map<String, String[]> getConnectedSocketController() {
		requiredLoggedIn();
		
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (SocketAppWrapper app : SocketManager.getAllSocketApps()) {
			String address = app.getSocket().getInetAddress().getHostAddress();
			map.put(address, app.getAvailableController());
		}
		return map;
	}

	/**
	 * RESTful service at /mec/list
	 */
	@Override
	public List<String> getControllerFromMEC(MEControllerProtocol protocol, String host, int port) {
		requiredLoggedIn();
		
		if (protocol == MEControllerProtocol.SOCKET) {
			SocketAppWrapper app = SocketManager.getSocketApp(host);
			if (app == null) {
				return null;
			} else {
				return Arrays.asList(app.getAvailableController());
			}
		} else {
			if (ServerCheck.isPortReachable(host, port)) {
				return ServerCheck.getController(protocol, host, port);
			} else {
				return null;
			}
		}
	}
}
