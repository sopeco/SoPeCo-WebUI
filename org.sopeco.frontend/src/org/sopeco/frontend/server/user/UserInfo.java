package org.sopeco.frontend.server.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.server.rpc.DatabaseManagerRPCImpl;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class UserInfo {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInfo.class);

	private UserInfo() {
	}

	/**
	 * Which SessionId is connected to which database.
	 */
	private static HashMap<String, String> sessionToDatabase = new HashMap<String, String>();

	/**
	 * Set the database, which is related to this session.
	 * 
	 * @param sessionId
	 * @param databaseId
	 */
	public static void setSessionToDb(String sessionId, String databaseId) {
		LOGGER.debug("set database '{}' to session '{}'", databaseId, sessionId);
		sessionToDatabase.put(sessionId, databaseId);
	}

	/**
	 * Returns the database to which the session is connected.
	 * 
	 * @param sessionId
	 * @return
	 */
	public static String getDatabaseOfSession(String sessionId) {
		return sessionToDatabase.get(sessionId);
	}

	/**
	 * Returns all session ids, which are connected to the given database.
	 * 
	 * @param databaseName
	 * @return
	 */
	public static List<String> getSessionsOnDatabase(String databaseName) {
		List<String> retList = new ArrayList<String>();

		for (String sId : sessionToDatabase.keySet()) {
			if (databaseName.equals(sessionToDatabase.get(sId))) {
				retList.add(sId);
			}
		}

		return retList;
	}
}
