package org.sopeco.frontend.server.rpc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rmi.RmiMEConnector;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.shared.definitions.PushPackage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerRPCImpl extends RemoteServiceServlet implements MEControllerRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> controllerList = new ArrayList<String>();

	@Override
	public List<String> getMEControllerList() {
		// List<String> returnList = new ArrayList<String>();

		return controllerList;
	}

	int i = 0;

	@Override
	public int checkControllerStatus(String url) {
		boolean f = true;
		for (String s : controllerList) {
			if (s.equals(url)) {
				f = false;
			}
		}
		if (f) {
			controllerList.add(url);

			PushPackage push = new PushPackage(Type.NEW_MEC_AVAILABLE);
			push.setPiggyback(url);

			PushRPCImpl.push(push);
		}

		
		
//		IMeasurementEnvironmentController meController = RmiMEConnector.connectToMEController(new URI("rmi://"));
//		
//		meController.getMEDefinition();
		
		
		
		
		i++;
		try {
			Thread.sleep(750);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (i % 3 == 0)
			return -1;

		if (i % 3 == 1)
			return STATUS_ONLINE;

		return MEControllerRPC.STATUS_OFFLINE;
	}

}
