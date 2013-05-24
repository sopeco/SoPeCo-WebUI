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
package org.sopeco.webui.server.gcharts;

import org.sopeco.engine.registry.ISoPeCoExtension;
import org.sopeco.webui.server.chartconnector.IChartCreator;
import org.sopeco.webui.shared.entities.ChartData;
import org.sopeco.webui.shared.entities.ChartOptions;
import org.sopeco.webui.shared.entities.ChartParameter;
import org.sopeco.webui.shared.entities.Visualization;
import org.sopeco.webui.shared.entities.Visualization.Type;

public class GCharts implements IChartCreator {

	public GCharts() {
	}

	@Override
	public ISoPeCoExtension<?> getProvider() {
		return null;
	}

	@Override
	public synchronized Visualization createVisualization(String experimentName, ChartData data,
			ChartParameter inputParameter, ChartParameter outputParameter, ChartOptions options) {
		Visualization visualization = new Visualization();
		visualization.setLink("");
		visualization.setName(experimentName);
		visualization.setData(data);
		visualization.setType(Type.GCHART);
		visualization.setInputParameter(inputParameter);
		visualization.setOutputParameter(outputParameter);
		visualization.setOptions(options);
		return visualization;
	}
}
