package org.sopeco.frontend.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 *
 */
public interface MEControllerRPCAsync {

	void checkControllerStatus(String url, AsyncCallback<Integer> callback);

	void getMEControllerList(AsyncCallback<List<String>> callback);

}
