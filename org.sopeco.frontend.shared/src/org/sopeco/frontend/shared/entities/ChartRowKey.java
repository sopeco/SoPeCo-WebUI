package org.sopeco.frontend.shared.entities;

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
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()){
			builder.append(e.getValue());
			builder.append("-");
		}
		return builder.toString();
	}
	
	public Double getKeyValue(ChartParameter p){
		return inputParameters.get(p);
	}
	
	public void set(ChartParameter key, Double value){
		inputParameters.put(key, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof ChartRowKey))
			return false;
		ChartRowKey key = (ChartRowKey) obj;
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()){
			if (e.getValue() != key.getKeyValue(e.getKey()))
				return false;
		}
		return true;
	}

	@Override
	public int compareTo(ChartRowKey key) {
		if (key == null)
			return -1;
		for (Entry<ChartParameter, Double> e : inputParameters.entrySet()){
			if (key.getKeyValue(e.getKey()) != null && e.getValue() != null){
				int d = (int) Math.signum(e.getValue()-key.getKeyValue(e.getKey()));
				if (d != 0)
					return d;
			} else if (e.getValue() != null) {
				return -1;
			} else if (key.getKeyValue(e.getKey()) != null){
				return 1;
			}
		}
		return 0;
	}

	
}
