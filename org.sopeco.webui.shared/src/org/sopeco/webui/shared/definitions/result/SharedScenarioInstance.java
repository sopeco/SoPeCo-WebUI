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
package org.sopeco.webui.shared.definitions.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SharedScenarioInstance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String scenarioName, controllerUrl;
	private List<SharedExperimentSeries> experimentSeries;

	public SharedScenarioInstance() {
		experimentSeries = new ArrayList<SharedExperimentSeries>();
	}

	public void addExperimentSeries(SharedExperimentSeries series) {
		experimentSeries.add(series);
		series.setParentInstance(this);
	}

	/**
	 * @return the controllerUrl
	 */
	public String getControllerUrl() {
		return controllerUrl;
	}

	/**
	 * @return the experimentSeries
	 */
	public List<SharedExperimentSeries> getExperimentSeries() {
		return experimentSeries;
	}

	/**
	 * @return the scenarioName
	 */
	public String getScenarioName() {
		return scenarioName;
	}

	/**
	 * @param controllerUrl
	 *            the controllerUrl to set
	 */
	public void setControllerUrl(String controllerUrl) {
		this.controllerUrl = controllerUrl;
	}

	/**
	 * @param scenarioName
	 *            the scenarioName to set
	 */
	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
}
