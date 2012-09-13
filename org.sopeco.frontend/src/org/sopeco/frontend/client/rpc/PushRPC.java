package org.sopeco.frontend.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("pushRPC")
public interface PushRPC extends RemoteService {

	public final static int ERROR = 0;
	public final static int TEST = 1;
	
	int push ();
}
