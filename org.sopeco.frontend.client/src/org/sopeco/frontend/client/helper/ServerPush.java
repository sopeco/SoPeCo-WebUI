package org.sopeco.frontend.client.helper;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.PushRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.client.rpc.PushRPCAsync;
import org.sopeco.frontend.shared.entities.RunningControllerStatus;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.frontend.shared.push.PushListPackage;
import org.sopeco.frontend.shared.push.PushObjectPackage;
import org.sopeco.frontend.shared.push.PushPackage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ServerPush implements AsyncCallback<PushPackage> {

	private static final Logger LOGGER = Logger.getLogger(ServerPush.class.getName());
	
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
		LOGGER.severe("ServerPush failed..");
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
		if (pushPackage.getType() == Type.PUSH_SCHEDULED_EXPERIMENT) {
			PushListPackage inPackage = (PushListPackage) pushPackage;
			List<FrontendScheduledExperiment> list = inPackage.getAttachment(FrontendScheduledExperiment.class);
			MainLayoutPanel.get().getExecuteController().getTabControllerTwo().setScheduledExperiments(list);
		} else if (pushPackage.getType() == Type.PUSH_CURRENT_CONTROLLER_EXPERIMENT) {
			PushObjectPackage inPackage = (PushObjectPackage) pushPackage;
			RunningControllerStatus ccExperiment = inPackage.getAttachment(RunningControllerStatus.class);
			MainLayoutPanel.get().getExecuteController().getTabControllerThree()
					.setCurrentControllerExperiment(ccExperiment);
		} else if (pushPackage.getType() == Type.PUSH_CURRENT_CONTROLLER_QUEUE) {
			PushListPackage inPackage = (PushListPackage) pushPackage;
			List<FrontendScheduledExperiment> list = inPackage.getAttachment(FrontendScheduledExperiment.class);
			MainLayoutPanel.get().getExecuteController().getTabControllerThree().setControllerQueue(list);
		}
	}

}
