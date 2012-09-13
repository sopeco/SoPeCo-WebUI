package org.sopeco.frontend.server.rpc;

import org.sopeco.frontend.client.rpc.PushRPC;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PushRPCImpl extends RemoteServiceServlet implements PushRPC {

	private static final long serialVersionUID = 1L;

	public int push() {
		
		try {
			Thread.sleep(5000);
			
			return (int)(Math.random()*2);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//return PushRPC.ERROR;
	}

}
