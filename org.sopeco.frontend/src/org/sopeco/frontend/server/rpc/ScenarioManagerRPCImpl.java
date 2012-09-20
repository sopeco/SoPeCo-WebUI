package org.sopeco.frontend.server.rpc;

import org.sopeco.frontend.client.rpc.ScenarioManagerRPC;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of the ScenarioManagerRPC. The Frontend is getting all
 * scenario inforamtion from these calls.
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioManagerRPCImpl extends RemoteServiceServlet implements ScenarioManagerRPC {

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getScenarioNames() {
		String[] retValues = new String[]{"test 1", "test 2"};
		return retValues;
	}

}
