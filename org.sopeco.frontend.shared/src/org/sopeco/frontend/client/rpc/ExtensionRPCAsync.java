package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.helper.Extension;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ExtensionRPCAsync {
	void getExtension(AsyncCallback<Extension> callback);
}
