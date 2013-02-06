package org.sopeco.frontend.client.rpc;

import java.util.List;
import java.util.Map;

import org.sopeco.frontend.shared.helper.MEControllerProtocol;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("getRPC")
public interface GetRPC extends RemoteService {

	Map<String, String[]> getConnectedSocketController();

	List<String> getControllerFromMEC(MEControllerProtocol protocol, String host, int port);
}
