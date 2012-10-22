package org.sopeco.frontend.client.rpc;

import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ExtensionRPCAsync {
	void getExtensions(AsyncCallback<ExtensionContainer> callback);
}
