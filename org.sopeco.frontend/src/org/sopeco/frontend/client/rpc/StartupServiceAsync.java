package org.sopeco.frontend.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StartupServiceAsync {

	void start(AsyncCallback<Boolean> callback);

}
