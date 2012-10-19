package org.sopeco.frontend.server.rpc;

import java.util.List;

import org.sopeco.engine.experimentseries.ITerminationConditionExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.frontend.client.rpc.ExtensionRPC;
import org.sopeco.frontend.shared.helper.Extension;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExtensionRPCImpl extends RemoteServiceServlet implements
		ExtensionRPC {

	private static final long serialVersionUID = 1L;

	@Override
	public Extension getExtension() {

		List<ITerminationConditionExtension> terminationList = ExtensionRegistry.getSingleton()
				.getExtensions(ITerminationConditionExtension.class).getList();
		
		return null;
	}

}
