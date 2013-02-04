package org.sopeco.frontend.server.rpc;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;
import org.sopeco.frontend.client.rpc.GetRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class GetRPCImpl extends SuperRemoteServlet implements GetRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, String[]> getConnectedSocketController() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (SocketAppWrapper app : SocketManager.getAllSocketApps()) {
			String address = app.getSocket().getInetAddress().getHostAddress() + ":" + app.getSocket().getPort();
			map.put(address, app.getAvailableController());
		}
		return map;
	}

}
