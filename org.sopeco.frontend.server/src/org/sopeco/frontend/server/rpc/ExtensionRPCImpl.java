package org.sopeco.frontend.server.rpc;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.processing.IProcessingStrategyExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.frontend.client.rpc.ExtensionRPC;
import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.frontend.shared.helper.ExtensionTypes;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExtensionRPCImpl extends SuperRemoteServlet implements ExtensionRPC {

	private static final long serialVersionUID = 1L;

	@Override
	public ExtensionContainer getExtensions() {
		ExtensionContainer container = new ExtensionContainer();

		for (ExtensionTypes type : ExtensionTypes.values()) {
			switch (type) {
			case EXPLORATIONSTRATEGY:
				container.getMap().put(ExtensionTypes.EXPLORATIONSTRATEGY,
						createExtension(IExplorationStrategyExtension.class));
				break;
			// case TERMINATIONCONDITION:
			// container.getMap().put(ExtensionTypes.TERMINATIONCONDITION,
			// createExtension(ITerminationConditionExtension.class));
			// break;
			case CONSTANTASSIGNMENT:
				container.getMap().put(ExtensionTypes.CONSTANTASSIGNMENT,
						createExtension(IConstantAssignmentExtension.class));
				break;
			case PARAMETERVARIATION:
				container.getMap().put(ExtensionTypes.PARAMETERVARIATION,
						createExtension(IParameterVariationExtension.class));
				break;
			case PROCESSINGSTRATEGY:
				container.getMap().put(ExtensionTypes.PROCESSINGSTRATEGY,
						createExtension(IProcessingStrategyExtension.class));
				break;
			}
		}

		return container;
	}

	/**
	 * Returns a map with all existing extensions of the given class. Key:
	 * Extension Name - Value: Config-Map.
	 * 
	 * @param c
	 *            the extension class
	 * @param <E>
	 *            type of the extension to be retrieved
	 * @return
	 */
	<E extends ISoPeCoExtension<?>> Map<String, Map<String, String>> createExtension(Class<E> c) {
		Map<String, Map<String, String>> extensions = new HashMap<String, Map<String, String>>();

		for (E ext : ExtensionRegistry.getSingleton().getExtensions(c).getList()) {
			Map<String, String> copiedMap = new HashMap<String, String>();
			copiedMap.putAll(ext.getConfigParameters());

			extensions.put(ext.getName(), copiedMap);
		}

		return extensions;
	}
}
