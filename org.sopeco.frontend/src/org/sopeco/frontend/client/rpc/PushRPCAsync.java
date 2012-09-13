package org.sopeco.frontend.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PushRPCAsync {

	void push(AsyncCallback<Integer> callback);

}
