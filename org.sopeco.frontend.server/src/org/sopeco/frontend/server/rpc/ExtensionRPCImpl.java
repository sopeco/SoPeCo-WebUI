package org.sopeco.frontend.server.rpc;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.ITerminationConditionExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.frontend.client.rpc.ExtensionRPC;
import org.sopeco.frontend.shared.helper.Extension;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

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
	public Extension getExtension(ExtensionTypes extensionType) {
		// Map<String, String> temp = new HashMap<String, String>();
		// Extension ext = new Extension();
		// ext.getExtensionMap().put("Test", temp);
		//
		// return ext;
		switch (extensionType) {
		case TerminationCondition:
			return createExtension(ITerminationConditionExtension.class);
		case ExplorationStrategy:
			return createExtension(IExplorationStrategyExtension.class);
		default:
			throw new RuntimeException("Unknown ExtensionType..");
		}
	}

	/**
	 * Loads the Extensions of the given Class in a new Extension object which
	 * is send to the frontend.
	 * 
	 * @param c
	 *            the extension class
	 * @param <E>
	 *            type of the extension to be retrieved
	 * @return
	 */
	<E extends ISoPeCoExtension<?>> Extension createExtension(Class<E> c) {
		Extension extension = new Extension();

		for (E ext : ExtensionRegistry.getSingleton().getExtensions(c).getList()) {
			Map<String, String> copiedMap = new HashMap<String, String>();
			copiedMap.putAll(ext.getConfigParameters());
			
			extension.getExtensionMap().put(ext.getName(), copiedMap);
		}

		return extension;
	}
}
