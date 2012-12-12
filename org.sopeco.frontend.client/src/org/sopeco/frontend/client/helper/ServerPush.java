package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.Notification;
import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.client.rpc.PushRPCAsync;
import org.sopeco.frontend.shared.push.PushPackage;
import org.sopeco.frontend.shared.push.StatusPackage;
import org.sopeco.frontend.shared.push.StatusPackage.EventType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ServerPush implements AsyncCallback<PushPackage> {

	private static ServerPush push;
	private static PushRPCAsync pushRPC = GWT.create(PushRPC.class);
	private boolean waiting = false;

	public static ServerPush get() {
		if (push == null) {
			push = new ServerPush();
		}
		return push;
	}

	private ServerPush() {
	}

	@Override
	public void onFailure(Throwable caught) {
		waiting = false;
		Message.error("serverpush failed");
	}

	@Override
	public void onSuccess(PushPackage result) {
		waiting = false;
		execute(result);
		startRequest();
	}

	public void startRequest() {
		synchronized (pushRPC) {
			if (waiting) {
				return;
			} else {
				waiting = true;
			}
		}
		pushRPC.push(this);
	}

	private void execute(PushPackage pushPackage) {
		if (pushPackage.getType() == Type.CONTROLLER_STATUS) {
			execStatusPackage((StatusPackage) pushPackage);
		}
		// switch (pushPackage.getType()) {
		// case IDLE:
		// // Notification.show("Idle");
		// break;
		// case ERROR:
		// Notification.show("Error");
		// break;
		// case MESSAGE:
		// String message = (String) pushPackage.getPiggyback();
		// Notification.show(message);
		// break;
		// case NEW_MEC_AVAILABLE:
		// // EnvironmentView envPanel = (EnvironmentView) MainLayoutPanel.get()
		// // .getCenterController(CenterType.Environment).getView();
		// // envPanel.addMEControllerUrl(pushPackage.getPiggyback());
		// break;
		// case NEW_ENV_DEFINITION:
		// EnvironmentView envPanel2 = (EnvironmentView) MainLayoutPanel.get()
		// .getCenterController(CenterType.Environment).getView();
		//
		// envPanel2.getEnvironmentDefinitonTreePanel().generateTree(true);
		// break;
		// default:
		// Message.warning("unknown parameter");
		// }
	}

	private void execStatusPackage(StatusPackage statusPackage) {
		GWT.log(statusPackage.getEventType() + "");
		if (statusPackage.getEventType() == EventType.MEASUREMENT_FINISHED) {
			Notification.show("Measurement Finished");
		}
	}
}
