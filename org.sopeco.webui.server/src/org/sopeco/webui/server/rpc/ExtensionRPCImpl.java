/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.server.rpc;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.engine.analysis.IPredictionFunctionStrategyExtension;
import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.experimentseries.IParameterVariationExtension;
import org.sopeco.engine.processing.IProcessingStrategyExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.webui.client.rpc.ExtensionRPC;
import org.sopeco.webui.shared.helper.ExtensionContainer;
import org.sopeco.webui.shared.helper.ExtensionTypes;

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
			case ANALYSIS:
				container.getMap().put(ExtensionTypes.ANALYSIS,
						createExtension(IPredictionFunctionStrategyExtension.class));
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
