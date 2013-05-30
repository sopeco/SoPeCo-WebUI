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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.server.user.User;
import org.sopeco.webui.server.user.UserManager;
import org.sopeco.webui.shared.push.PushPackage;
import org.sopeco.webui.shared.rpc.PushRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PushRPCImpl extends SPCRemoteServlet implements PushRPC {

	private static final long serialVersionUID = 1L;
	private static final int TIMEOUT = 30000;

	private static Map<String, List<PushPackage>> packageListMap = new HashMap<String, List<PushPackage>>();

	public List<PushPackage> push() {
		try {
			initList(getSessionId());

			List<PushPackage> returnList = new ArrayList<PushPackage>();
			synchronized (packageListMap.get(getSessionId())) {
				List<PushPackage> list = packageListMap.get(getSessionId());
				if (list.isEmpty()) {
					list.wait(TIMEOUT);
				}
				if (!list.isEmpty()) {
					returnList.addAll(list);
					list.clear();
				}
			}
			return returnList;
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

	public static void pushToAllOnController(String controllerUrl, PushPackage pushPackage) {
		for (User user : UserManager.instance().getAllUsers() ) {
			try {
				String cUrl = user.getAccountDetails().getControllerUrl();
				if (cUrl != null && cUrl.equals(controllerUrl)) {
					PushRPCImpl.push(user.getSessionId(), pushPackage);
				}
			} catch (NullPointerException x) {
				// TODO
				System.out.println("TODO");
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
