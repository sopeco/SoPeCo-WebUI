package org.sopeco.frontend.server.rpc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.engine.measurementenvironment.socket.SocketAppWrapper;
import org.sopeco.engine.measurementenvironment.socket.SocketManager;
import org.sopeco.frontend.client.rpc.GetRPC;
import org.sopeco.frontend.server.helper.ServerCheck;
import org.sopeco.frontend.shared.helper.MEControllerProtocol;

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
			String address = app.getSocket().getInetAddress().getHostAddress();
			map.put(address, app.getAvailableController());
		}
		return map;
	}

	@Override
	public List<String> getControllerFromMEC(MEControllerProtocol protocol, String host, int port) {
		if (protocol == MEControllerProtocol.SOCKET) {
			SocketAppWrapper app = SocketManager.getSocketApp(host);
			if (app == null) {
				return null;
			} else {
				return Arrays.asList(app.getAvailableController());
			}
		} else {
			if (ServerCheck.isPortReachable(host, port)) {
				return ServerCheck.getController(protocol, host, port);
			} else {
				return null;
			}
		}
	}
}
