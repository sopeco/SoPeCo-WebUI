package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.Notification;
import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.client.rpc.PushRPCAsync;
import org.sopeco.frontend.shared.push.CurrentControllerExperimentPackage;
import org.sopeco.frontend.shared.push.ScheduledExperimentsPackage;
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
		startRequest();
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
			// } else if (pushPackage.getType() ==
			// Type.SCHEDULED_EXPERIMENT_CHANGED) {
			// MainLayoutPanel.get().getExecuteController().getTabControllerTwo().loadScheduledExperiments();
			// Notification.show("Experiment started..");
		} else if (pushPackage.getType() == Type.PUSH_SCHEDULED_EXPERIMENT) {
			ScheduledExperimentsPackage inPackage = (ScheduledExperimentsPackage) pushPackage;
			MainLayoutPanel.get().getExecuteController().getTabControllerTwo()
					.setScheduledExperiments(inPackage.getAttachement());
		} else if (pushPackage.getType() == Type.PUSH_CURRENT_CONTROLLER_EXPERIMENT) {
			CurrentControllerExperimentPackage ccePackage = (CurrentControllerExperimentPackage) pushPackage;
			MainLayoutPanel.get().getExecuteController().getTabControllerThree()
					.setCurrentControllerExperiment(ccePackage.getCurrentControllerExperiment());
		}
	}

	private void execStatusPackage(StatusPackage statusPackage) {
		GWT.log(statusPackage.getEventType() + "");
		if (statusPackage.getEventType() == EventType.MEASUREMENT_FINISHED) {
			Notification.show("Measurement Finished");
		}
	}
}
