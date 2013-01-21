package org.sopeco.frontend.server.chartconnector;

import org.sopeco.engine.registry.ISoPeCoExtension;

public interface IChartConnectionExtension extends ISoPeCoExtension<IChartConnection> {
	/**
	 * Creates a new analysis strategy provided by the extension.
	 * 
	 * @return Returns an analysis strategy
	 */
	IChartConnection createExtensionArtifact();
}
