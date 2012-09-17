package org.sopeco.frontend.server.rpc;

import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.shared.definitions.PushPackage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PushRPCImpl extends RemoteServiceServlet implements PushRPC {

	private static final long serialVersionUID = 1L;
	private static PushPackage send;
	private static final Object notify = new Object();
	private static final int TIMEOUT = 30000;

	public PushPackage push() {

		try {
			send = new PushPackage(Type.IDLE);
			
			synchronized (notify) {
				notify.wait(TIMEOUT);
			}
			return send;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a pushPackage to the frontend
	 * @param integer
	 */
	public static void push(PushPackage pushPackage) {
		send = pushPackage;

		synchronized (notify) {
			notify.notifyAll();
		}
	}

	public static void pushMessage ( String message ) {
		PushPackage pushPackage = new PushPackage(Type.MESSAGE);
		pushPackage.setPiggyback(message);
		push(pushPackage);
	}
}
