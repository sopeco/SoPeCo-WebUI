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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

	private static UserManager singleton;

	public static UserManager instance() {
		if (singleton == null) {
			singleton = new UserManager();
		}
		return singleton;
	}

	private Map<String, User> userMap = new HashMap<String, User>();

	private UserManager() {
	}

	/**
	 * Returns whether a (not expired) user with the given session id exists in
	 * the userMap.
	 * 
	 * @param sessionId
	 * @return user with the given session exists
	 */
	public boolean existUser(String sessionId) {
		synchronized (userMap) {
			return getUser(sessionId) != null;
		}
	}

	/**
	 * Returns a List with all users which are connected to the given account.
	 * 
	 * @param databaseId
	 * @return
	 */
	public List<User> getAllUserOnAccount(long accountId) {
		List<User> userList = new ArrayList<User>();
		for (User u : userMap.values()) {
			if (u.isExpired()) {
				destroyUser(u);
			} else if (u.getCurrentAccount().getId() == accountId) {
				userList.add(u);
			}
		}
		return userList;
	}

	/**
	 * Returns a list with all valid users.
	 * 
	 * @return
	 */
	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<User>();
		for (User u : userMap.values()) {
			if (u.isExpired()) {
				destroyUser(u);
			} else {
				userList.add(u);
			}
		}
		return userList;
	}

	/**
	 * Returns the user, which has the given session id. If there is no user
	 * with the given session key, it returns null.
	 * 
	 * @param sessionId
	 * @return user
	 */
	public User getUser(String sessionId) {
		synchronized (userMap) {
			User user = userMap.get(sessionId);
			if (user != null && user.isExpired()) {
				destroyUser(user);
				user = null;
			}
			return user;
		}
	}

	public User registerUser(String sessionId) {
		LOGGER.debug("Store new user with the session id '{}'", sessionId);
		User newUser = new User(sessionId);
		userMap.put(sessionId, newUser);
		return newUser;
	}

	/**
	 * Destroys the given user.
	 * 
	 * @param user
	 *            to destroy
	 */
	public void destroyUser(User u) {
		LOGGER.debug("Destroy user with the session id '{}'", u.getSessionId());

		userMap.remove(u.getSessionId());

		if (u.getCurrentPersistenceProvider() != null) {
			u.getCurrentPersistenceProvider().closeProvider();
			u.setCurrentPersistenceProvider(null);
		}
	}
}
