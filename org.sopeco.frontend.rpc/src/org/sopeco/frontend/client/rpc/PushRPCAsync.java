package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.definitions.PushPackage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PushRPCAsync {

	void push(AsyncCallback<PushPackage> callback);

}
