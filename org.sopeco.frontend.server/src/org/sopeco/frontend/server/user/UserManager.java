package org.sopeco.frontend.server.user;

import java.util.ArrayList;
import java.util.Collection;
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
	public static void setUser(String sessionId, User user) {
		LOGGER.debug("stored new user under the id '{}'", sessionId);
		userMap.put(sessionId, user);
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
				userMap.put(sessionId, newUser);
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
			if (u.getCurrentDatabaseId().equals(databaseId)) {
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
