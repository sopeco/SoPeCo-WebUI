package org.sopeco.frontend.server.rpc;

import org.sopeco.frontend.client.rpc.SystemDetailsRPC;
import org.sopeco.persistence.config.PersistenceConfiguration;

import java.util.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SystemDetailsRPCImpl extends RemoteServiceServlet implements SystemDetailsRPC {

	private static final long serialVersionUID = 1L;

	@Override
	public HashMap<String, String> getMetaDatabaseDetails() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		PersistenceConfiguration pConf = PersistenceConfiguration.getSingleton();
		
		map.put("host", pConf.getMetaDataHost());
		map.put("port", pConf.getMetaDataPort());
		
		return map;
	}

}
