package org.sopeco.frontend.client.rpc;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface GetRPCAsync {

	void getConnectedSocketController(AsyncCallback<Map<String, String[]>> callback);

}
