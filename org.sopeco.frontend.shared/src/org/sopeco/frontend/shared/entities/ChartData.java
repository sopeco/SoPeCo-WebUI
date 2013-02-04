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
	
	private Map<Double, List<Double>> data;
	private List<String> dataSetNames = new ArrayList<String>();
	
	public ChartData() {
		data = new HashMap<Double, List<Double>>();
	}

	public List<Double> getxAxis() {
		return new ArrayList<Double>(data.keySet());
	}

	public List<List<Double>> getDatarows() {
		return new ArrayList<List<Double>>(data.values());
	}

	public void addDatarows(String name, Double[] data) {
		this.addDatarows(name, data);
	}
	
	public void setData(Map<Double, List<Double>> data){
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getName());
		builder.append("\n");
		builder.append("names: ");
		for (String s : dataSetNames){
			builder.append(s);
		}
		builder.append("\n");
		for (Entry<Double, List<Double>> d : data.entrySet()){
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

	public List<String> getDataSetNames() {
		return dataSetNames;
	}

	public void setDataSetNames(List<String> dataSetNames) {
		this.dataSetNames = dataSetNames;
	}

	
}
