package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.definitions.PushPackage;

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
		ERROR, IDLE, MESSAGE, NEW_MEC_AVAILABLE
	}

	PushPackage push();
}
