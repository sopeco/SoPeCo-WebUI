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
package org.sopeco.webui.shared.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ChartData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<ChartRowKey, List<Double>> data;
	private ChartParameter inputParameter;

	public ChartData() {
		data = new HashMap<ChartRowKey, List<Double>>();
	}

	public List<ChartRowKey> getxAxis() {
		return new ArrayList<ChartRowKey>(data.keySet());
	}

	public List<List<Double>> getDatarows() {
		return new ArrayList<List<Double>>(data.values());
	}

	public void addDatarows(String name, Double[] data) {
		this.addDatarows(name, data);
	}

	public void setData(Map<ChartRowKey, List<Double>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("\n");
		for (Entry<ChartRowKey, List<Double>> d : data.entrySet()) {
			builder.append(d.getKey());
			builder.append(": ");
			for (Double doub : d.getValue()) {
				builder.append(doub);
				builder.append(", ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	public ChartParameter getInputParameter() {
		return inputParameter;
	}

	public void setInputParameter(ChartParameter inputParameter) {
		this.inputParameter = inputParameter;
	}

}
