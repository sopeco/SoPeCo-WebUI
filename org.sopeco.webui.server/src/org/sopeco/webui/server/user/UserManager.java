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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UserManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

	private UserManager() {
	}

	private static HashMap<String, User> userMap = new HashMap<String, User>();

	/**
	 * Stores the given user in the userMap under the given key.
	 * 
	 * @param sessionId
	 * @param user
	 */
	public static void setUser(User user) {
		LOGGER.debug("stored new user under the id '{}'", user.getSessionId());
		userMap.put(user.getSessionId(), user);
	}

	/**
	 * Returns the user, which has the given session id.
	 * 
	 * @param sessionId
	 * @return user
	 */
	public static User getUser(String sessionId) {
		synchronized (userMap) {
			if (!userMap.containsKey(sessionId)) {
				User newUser = new User(sessionId);
				setUser(newUser);
			}
		}

		return userMap.get(sessionId);
	}

	/**
	 * Returns whether a user with the given session id exists in the userMap.
	 * 
	 * @param sessionId
	 * @return user with the given session exists
	 */
	public static boolean existSession(String sessionId) {
		synchronized (userMap) {
			return userMap.containsKey(sessionId);
		}
	}

	/**
	 * Returns a List with all users which have the given databaseId.
	 * 
	 * @param databaseId
	 * @return
	 */
	public static List<User> getAllUserOnDatabase(String databaseId) {
		List<User> userList = new ArrayList<User>();

		for (User u : userMap.values()) {
			if (u.getCurrentAccountId().equals(databaseId)) {
				userList.add(u);
			}
		}

		return userList;
	}

	public static HashMap<String, User> getAllUsers() {
		return new HashMap<String, User>(userMap);
	}

	public static void removeUser(User u) {
		userMap.remove(u.getSessionId());
		u.kill();
	}
}
