package org.sopeco.frontend.client.rpc;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("systemDetailsRPC")
public interface SystemDetailsRPC extends RemoteService {

	HashMap<String, String> getMetaDatabaseDetails ();
	
}
