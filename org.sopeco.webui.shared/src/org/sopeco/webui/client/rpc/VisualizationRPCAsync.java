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
package org.sopeco.webui.client.rpc;

import java.util.List;
import java.util.Map;

import org.sopeco.webui.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.webui.shared.entities.ChartOptions;
import org.sopeco.webui.shared.entities.ChartParameter;
import org.sopeco.webui.shared.entities.Visualization;

import com.google.gwt.user.client.rpc.AsyncCallback;

/** This class provides the different Remote Procedure Calls (RPCs) for creating, modifying and deleting visualizations/charts.
 * <br/> - {@link #createVisualization}
 * <br/> - {@link #deleteVisualization}
 * <br/> - {@link #getVisualizations}
 * <br/> - {@link #applySplineInterpolation}
 * 
 * @author Benjamin Ebling
 *
 */
public interface VisualizationRPCAsync {
	
	/** Creates a new chart with the specified parameters (use {@link #getChartParameter} to get them for your selection).
	 * 
	 * @param experimentRun The experiment which the chart is created for.
	 * @param inputParameter The parameter which is used as x-axis.
	 * @param outputParameter The parameter which is used as y-axis.
	 * @param options The options describing the chart more in detail.
	 * @param extension This extension is used to create the chart (default is: "Google Charts").
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void createVisualization(SharedExperimentRuns experimentRun, ChartParameter inputParameter, ChartParameter outputParameter, ChartOptions options, String extension, AsyncCallback<Visualization> callback);
	
	/** A RPC that returns an array of {@link #ChartParameter} of either the type input or observation.
	 * <br/>Select one of each type for chart creation( {@link #createVisualization} ).
	 * 
	 * @param experiementRun The experiment for which to return the parameter.
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void getChartParameter(SharedExperimentRuns experiementRun, AsyncCallback<ChartParameter[]> callback);
	
	/** Returns the visualizations from <i>start</i>(included) to <i>start+length</i>(excluded).
	 * <br/>Use it when you just want to load some of the visualizations. For example if you use a list with paging,
	 * 
	 * @param start
	 * @param length
	 * @param callback
	 */
	void getVisualizations(int start, int length, AsyncCallback<List<Visualization>> callback);
	
	/** Deletes the visualization.
	 * 
	 * @param visualization The visualization to be deleted.
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void deleteVisualization(Visualization visualization, AsyncCallback<Void> callback);
	
	/** Returns all extensions that can be selected for the chart creation( {@link #createChart} ).
	 * 
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void getExtensions(AsyncCallback<List<String>> callback);
	
	/** Applies a spline interpolation to the given values. The keys are used as x-values the valueset is used as y-values.
	 * 
	 * @param values The values on which the spline interpolation should be applied.
	 * @param min The smallest x-value of the returned values.
	 * @param max The biggest x-value of the returned values.
	 * @param step The number of values that should be returned (Cannot be smaller than 2).
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void applySplineInterpolation(Map<Double, List<Double>> values, double min, double max, double step, AsyncCallback<Map<Double, List<Double>>> callback);
	
	/** Applies a simple regression to the given values. The keys are used as x-values the valueset is used as y-values.
	 * 
	 * @param values The values on which the regression should be applied.
	 * @param callback The asynchronous callback which is needed for all RPCs.
	 */
	void applySimpleRegression(Map<Double, List<Double>> values, AsyncCallback<Map<Double, List<Double>>> callback);
}
