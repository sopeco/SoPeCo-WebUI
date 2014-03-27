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
package org.sopeco.webui.server.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.webui.server.rest.ClientFactory;

/**
 * Maps session ID to token.<br />
 * The class is a Singleton.
 * 
 * @author Peter Merkert
 */
public final class UserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

	/**
	 * The Map to map a Session ID to a User.
	 */
	private Map<String, User> sessionID2UserMap = new HashMap<String, User>();
	
	private static UserManager singleton;

	/**
	 * Private constructor for singleton.
	 */
	private UserManager() {
	}
	
	/**
	 * Singleton constructor.
	 * 
	 * @return the instance of {@link UserManager}
	 */
	public static UserManager instance() {
		if (singleton == null) {
			singleton = new UserManager();
		}
		return singleton;
	}

	/**
	 * Returns whether a (not expired) user with the given session id exists in
	 * the userMap.
	 * 
	 * @param sessionId	the session ID
	 * @return 			true, if a token to the given session ID exists
	 */
	public boolean existToken(String sessionId) {
		return sessionID2UserMap.get(sessionId) != null;
	}
	
	/**
	 * Returns whether a token to the given session ID exsists and
	 * if the token is still valid in the RESTful service.
	 * 
	 * @param sessionId	the session ID
	 * @return 			true, if a token to the given session ID
	 * 					exists and is valid
	 */
	public boolean hasValidToken(String sessionId) {
		String token = getToken(sessionId);
		
		if (token == null) {
			return false;
		}

		LOGGER.debug("Checking token '{}' on SPC SL for validity.", token, sessionId);
			
		// now check if the token is still valid at the service interface
		WebTarget wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
														     ServiceConfiguration.SVC_ACCOUNT_CHECK,
															 ServiceConfiguration.SVC_ACCOUNT_TOKEN);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, token);
		
		Response r = wr.request(MediaType.APPLICATION_JSON).get();
		
		return r.getStatus() == Status.OK.getStatusCode();
	}
	
	/**
	 * Returns whether a (not expired) user with the given session id exists in
	 * the userMap.
	 * 
	 * @param sessionId	the session ID
	 * @param token		the users token
	 * @param accountId	the account ID related to the user
	 * @return 			true, if a token to the given session ID exists
	 */
	public boolean registerToken(String sessionId, String token, long accountId) {
		LOGGER.debug("New token '{}' registered to session id '{}'.", token, sessionId);
		
		User u = new User(token, accountId);
		
		synchronized (sessionID2UserMap) {	
			return sessionID2UserMap.put(sessionId, u) != null;
		}
	}

	/**
	 * Returns the token, which has the given session id. If there is no token
	 * with the given session key, it returns null.
	 * 
	 * @param sessionId	the session ID
	 * @return 			token (<code>null</code> possible)
	 */
	public String getToken(String sessionId) {
		
		User u = sessionID2UserMap.get(sessionId);
		
		if (u != null) {
			return u.getToken();
		}
		
		return "";
	}

	/**
	 * Removes the given token out of the map. As (session id, token) are both sides unique values
	 * the map is iterated on the session ids.<br />
	 * The time estimation is O(n)! <br />
	 * The parallel mapping in to the account ID is removed, too.
	 * 
	 * @param token	the token to delete
	 */
	public void deleteToken(String token) {
		
		for (String sessionId : sessionID2UserMap.keySet()) {
			
			if (token.equals(getToken(sessionId))) {
				sessionID2UserMap.remove(sessionId);
				// break can be done, because the token is unique (at least it should be)
				break;
			}
			
		}
		
	}
	
	/**
	 * Returns the account ID corresponding to this token.
	 * 
	 * @param token	the token of the current user
	 * @return		the account ID, this user is related to
	 */
	public long getAccountID(String token) {
		
		User u =  UserManager.instance().getUser(token);
		
		if (u != null) {
			return u.getAccountID();
		}
		
		return -1;
	}
	
	/**
	 * Returns the account ID corresponding to this token.
	 * 
	 * @param token	the token of the current user
	 * @return		the account ID, this user is related to
	 */
	public User getUser(String token) {
		
		for (User u : sessionID2UserMap.values()) {
			
			if (u.getToken().equals(token)) {
				return u;
			}
			
		}
	
		return null;
	}

	/**
	 * Returns a list with all tokens currently registered.
	 * 
	 * @return list with all tokens
	 */
	/*public List<String> getAllToken() {
		
		List<String> tokenList = new ArrayList<String>();
		for (String token : tokenMap.values()) {
			tokenList.add(token);
		}
		
		return tokenList;
		
	}*/

}
