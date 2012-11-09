package org.sopeco.frontend.server.rpc;

import java.util.HashMap;

import org.sopeco.frontend.client.rpc.SystemDetailsRPC;
import org.sopeco.persistence.config.PersistenceConfiguration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SystemDetailsRPCImpl extends SuperRemoteServlet implements SystemDetailsRPC {

	private static final long serialVersionUID = 1L;

	@Override
	public HashMap<String, String> getMetaDatabaseDetails() {
		HashMap<String, String> map = new HashMap<String, String>();

		PersistenceConfiguration pConf = PersistenceConfiguration.getSessionSingleton(getThreadLocalRequest()
				.getSession().getId());

		map.put("host", pConf.getMetaDataHost());
		map.put("port", pConf.getMetaDataPort());

		return map;
	}

}
