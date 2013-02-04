package org.sopeco.frontend.client.rpc;

import java.util.Map;

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

}
