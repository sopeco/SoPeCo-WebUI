package org.sopeco.frontend.shared.entities;

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
	
	public void setData(Map<ChartRowKey, List<Double>> data){
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("\n");
		for (Entry<ChartRowKey, List<Double>> d : data.entrySet()){
			builder.append(d.getKey());
			builder.append(": ");
			for (Double doub : d.getValue()){
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
