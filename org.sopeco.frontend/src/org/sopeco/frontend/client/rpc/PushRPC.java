package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.definitions.PushPackage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pushRPC")
public interface PushRPC extends RemoteService {

	public static enum Type {
		ERROR, IDLE, MESSAGE
	}
	
	PushPackage push ();
}
