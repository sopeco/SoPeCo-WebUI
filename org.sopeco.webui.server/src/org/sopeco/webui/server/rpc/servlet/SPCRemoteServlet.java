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
package org.sopeco.webui.server.rpc.servlet;

import javax.servlet.http.HttpSession;

import org.sopeco.webui.server.security.Security;
import org.sopeco.webui.server.user.TokenManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This class is used to handle the session of the current thread
 * and:<br />
 * - deliver the token to the session ID<br />
 * - checks if the current session ID is logged into the service
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class SPCRemoteServlet extends RemoteServiceServlet {

	/**
	 * Returns the current session id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the session ID corresponding to the current thread.
	 * 
	 * @return the session ID
	 */
	protected String getSessionId() {
		return getThreadLocalRequest().getSession().getId();
	}

	/**
	 * Returns the {@link HttpSession} correspding to the current thread.
	 * @return the {@link HttpSession}
	 */
	protected HttpSession getSession() {
		return getThreadLocalRequest().getSession();
	}
	
	/**
	 * Returns the token corresponding to the current session.
	 * 
	 * @return the token
	 */
	protected String getToken() {
		return TokenManager.instance().getToken(getSessionId());
	}

	/**
	 * Checks if the current session ID has a valid token (and therefore
	 * is logged in).
	 */
	protected void requiredLoggedIn() {
		Security.requiredLoggedIn(getSessionId());
	}

}
