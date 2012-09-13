package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.client.rpc.PushRPCAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ServerPush {

	private static boolean waiting = false;
	private static PushRPCAsync pushRPC = GWT.create(PushRPC.class);

	private ServerPush() {
	}

	
	public static void start() {
		synchronized (pushRPC) {
			if (waiting)
				return;
			else
				waiting = true;
		}
		
		pushRPC.push(getPushCallback());
	}


	private static AsyncCallback<Integer> getPushCallback() {
		AsyncCallback<Integer> returnCall = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				waiting = false;
				Message.error("serverpush failed");
			}

			@Override
			public void onSuccess(Integer result) {
				waiting = false;
				
				execute(result);

				start();
			}
		};

		return returnCall;
	}

	/**
	 * 
	 * @param x
	 */
	private static void execute(int x) {
		switch (x) {
		case PushRPC.TEST:
			Message.warning("Test");
			break;
		case PushRPC.ERROR:
			Message.warning("Error");
			break;
		default:
			Message.warning("unknown parameter");
		}
	}
}
