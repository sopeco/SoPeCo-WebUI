package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.client.rpc.PushRPCAsync;
import org.sopeco.frontend.shared.definitions.PushPackage;

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

	private static AsyncCallback<PushPackage> getPushCallback() {
		AsyncCallback<PushPackage> returnCall = new AsyncCallback<PushPackage>() {

			@Override
			public void onFailure(Throwable caught) {
				waiting = false;
				Message.error("serverpush failed");
			}

			@Override
			public void onSuccess(PushPackage result) {
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
	private static void execute(PushPackage pushPackage) {
//		switch (pushPackage.getType()) {
//		case IDLE:
//			// Notification.show("Idle");
//			break;
//		case ERROR:
//			Notification.show("Error");
//			break;
//		case MESSAGE:
//			String message = (String) pushPackage.getPiggyback();
//			Notification.show(message);
//			break;
//		case NEW_MEC_AVAILABLE:
////			EnvironmentView envPanel = (EnvironmentView) MainLayoutPanel.get()
////					.getCenterController(CenterType.Environment).getView();
////			envPanel.addMEControllerUrl(pushPackage.getPiggyback());
//			break;
//		case NEW_ENV_DEFINITION:
//			EnvironmentView envPanel2 = (EnvironmentView) MainLayoutPanel.get()
//					.getCenterController(CenterType.Environment).getView();
//
//			envPanel2.getEnvironmentDefinitonTreePanel().generateTree(true);
//			break;
//		default:
//			Message.warning("unknown parameter");
//		}
	}
}
