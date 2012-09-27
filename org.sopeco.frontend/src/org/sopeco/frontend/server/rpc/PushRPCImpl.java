package org.sopeco.frontend.server.rpc;

import java.util.HashMap;

import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.server.user.UserInfo;
import org.sopeco.frontend.shared.definitions.PushPackage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushRPCImpl extends RemoteServiceServlet implements PushRPC {

	private static final long serialVersionUID = 1L;
	private static final int TIMEOUT = 30000;

	private static HashMap<String, Object> waitingMap = new HashMap<String, Object>();
	private static HashMap<String, PushPackage> packageMap = new HashMap<String, PushPackage>();

	public PushPackage push() {
		String sId = getThreadLocalRequest().getSession().getId();

		try {
			packageMap.put(sId, new PushPackage(Type.IDLE));
			waitingMap.put(sId, new Object());

			synchronized (waitingMap.get(sId)) {
				waitingMap.get(sId).wait(TIMEOUT);
			}
			return packageMap.get(sId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a pushPackage to the frontend (to all connected clients).
	 * 
	 * @param pushPackage
	 *            object, which will be send
	 */
	public static void pushToAll(PushPackage pushPackage) {
		for (String sId : waitingMap.keySet()) {
			synchronized (waitingMap.get(sId)) {
				packageMap.put(sId, pushPackage);
				waitingMap.get(sId).notify();
			}
		}
	}

	/**
	 * Sends the package to all users, which are connected to the given
	 * database.
	 * 
	 * @param pushPackage
	 */
	public static void pushToCODB(String databaseName, PushPackage pushPackage) {
		for (String sId : UserInfo.getSessionsOnDatabase(databaseName)) {
			// for (String sId : waitingMap.keySet()) {

			synchronized (waitingMap.get(sId)) {
				packageMap.put(sId, pushPackage);
				waitingMap.get(sId).notify();
			}
		}
	}

	/**
	 * Sends a pushPackage to the frontend (to the client with the given session
	 * id).
	 * 
	 * @param sessionId
	 *            session of the client
	 * @param pushPackage
	 *            object, which will be send
	 */
	public static void push(String sessionId, PushPackage pushPackage) {
		synchronized (waitingMap.get(sessionId)) {
			packageMap.put(sessionId, pushPackage);
			waitingMap.get(sessionId).notify();
		}
	}

	/**
	 * Sending Pushpackage with the Type MESSAGE to the client.
	 * 
	 * @param message
	 */
	public static void pushMessage(String sessionId, String message) {
		PushPackage pushPackage = new PushPackage(Type.MESSAGE);
		pushPackage.setPiggyback(message);
		push(sessionId, pushPackage);
	}
}
