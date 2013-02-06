package org.sopeco.frontend.client.rpc;

import java.util.List;
import java.util.Map;

import org.sopeco.frontend.shared.helper.MEControllerProtocol;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface GetRPCAsync {

	void getConnectedSocketController(AsyncCallback<Map<String, String[]>> callback);

	void getControllerFromMEC(MEControllerProtocol protocol, String host, int port, AsyncCallback<List<String>> callback);
}
