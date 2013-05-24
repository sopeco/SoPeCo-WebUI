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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ChartRowKey implements Comparable<ChartRowKey>, Serializable {
	private Map<ChartParameter, Double> inputParameters;

	public ChartRowKey() {
		this.inputParameters = new HashMap<ChartParameter, Double>();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()) {
			builder.append(e.getValue());
			builder.append("-");
		}
		return builder.toString();
	}

	public Double getKeyValue(ChartParameter p) {
		return inputParameters.get(p);
	}

	public void set(ChartParameter key, Double value) {
		inputParameters.put(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ChartRowKey))
			return false;
		ChartRowKey key = (ChartRowKey) obj;
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()) {
			if (e.getValue() != key.getKeyValue(e.getKey()))
				return false;
		}
		return true;
	}

	@Override
	public int compareTo(ChartRowKey key) {
		if (key == null)
			return -1;
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()) {
			if (key.getKeyValue(e.getKey()) != null && e.getValue() != null) {
				int d = (int) Math.signum(e.getValue() - key.getKeyValue(e.getKey()));
				if (d != 0)
					return d;
			} else if (e.getValue() != null) {
				return -1;
			} else if (key.getKeyValue(e.getKey()) != null) {
				return 1;
			}
		}
		return 0;
	}

}
