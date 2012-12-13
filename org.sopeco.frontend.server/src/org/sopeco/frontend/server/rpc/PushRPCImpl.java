package org.sopeco.frontend.server.rpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.shared.push.PushPackage;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushRPCImpl extends SuperRemoteServlet implements PushRPC {

	private static final long serialVersionUID = 1L;
	private static final int TIMEOUT = 5000;

	private static HashMap<String, List<PushPackage>> packageListMap = new HashMap<String, List<PushPackage>>();

	public PushPackage push() {
		try {
			initList(getSessionId());

			PushPackage sendingPackage = new PushPackage(Type.IDLE);
			synchronized (packageListMap.get(getSessionId())) {
				if (packageListMap.get(getSessionId()).isEmpty()) {
					packageListMap.get(getSessionId()).wait(TIMEOUT);
				}
				if (!packageListMap.get(getSessionId()).isEmpty()) {
					sendingPackage = packageListMap.get(getSessionId()).get(0);
					packageListMap.get(getSessionId()).remove(0);
				}
			}
			return sendingPackage;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initList(String sessionId) {
		if (!packageListMap.containsKey(sessionId)) {
			packageListMap.put(sessionId, new ArrayList<PushPackage>());
		}
	}

	/**
	 * Sends a pushPackage to the frontend (to all connected clients).
	 * 
	 * @param pushPackage
	 *            object, which will be send
	 */
	public static void pushToAll(PushPackage pushPackage) {
		for (List<PushPackage> packageList : packageListMap.values()) {
			synchronized (packageList) {
				packageList.add(pushPackage);
				packageList.notify();
			}
		}
	}

	public static void push(String sessionId, PushPackage pushPackage) {
		if (packageListMap.containsKey(sessionId)) {
			List<PushPackage> packageList = packageListMap.get(sessionId);
			synchronized (packageList) {
				packageList.add(pushPackage);
				packageList.notify();
			}
		}
	}

	/**
	 * Sends the package to all users, which are connected to the given
	 * database.
	 * 
	 * @param pushPackage
	 */
	// public static void pushToCODB(String databaseId, PushPackage pushPackage)
	// {
	// for (User u : UserManager.getAllUserOnDatabase(databaseId)) {
	//
	// synchronized (waitingMap.get(u.getSessionId())) {
	// packageListMap.put(u.getSessionId(), pushPackage);
	// waitingMap.get(u.getSessionId()).notify();
	// }
	// }
	// }

	/**
	 * Sends a pushPackage to the frontend (to the client with the given session
	 * id).
	 * 
	 * @param sessionId
	 *            session of the client
	 * @param pushPackage
	 *            object, which will be send
	 */
	// public static void push(String sessionId, PushPackage pushPackage) {
	// synchronized (waitingMap.get(sessionId)) {
	// packageListMap.put(sessionId, pushPackage);
	// waitingMap.get(sessionId).notify();
	// }
	// }

	/**
	 * Sending Pushpackage with the Type MESSAGE to the client.
	 * 
	 * @param message
	 */
	// public static void pushMessage(String sessionId, String message) {
	// PushPackage pushPackage = new PushPackage(Type.MESSAGE);
	// pushPackage.setPiggyback(message);
	// push(sessionId, pushPackage);
	// }
}
