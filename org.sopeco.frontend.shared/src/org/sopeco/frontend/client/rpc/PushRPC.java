package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.push.PushPackage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 *
 */
@RemoteServiceRelativePath("pushRPC")
public interface PushRPC extends RemoteService {

	public static enum Type {
		ERROR, IDLE, MESSAGE, NEW_MEC_AVAILABLE, NEW_ENV_DEFINITION,
		
		GET_MEC_TOKEN,
		CONTROLLER_STATUS,
		SCHEDULED_EXPERIMENT_CHANGED,
		PUSH_SCHEDULED_EXPERIMENT,
		PUSH_CURRENT_CONTROLLER_EXPERIMENT
	}

	PushPackage push();
}
