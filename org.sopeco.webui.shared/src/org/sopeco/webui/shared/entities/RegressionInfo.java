package org.sopeco.webui.shared.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RegressionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2349780880471480125L;
	private Map<Double, List<Double>> data;

	public Map<Double, List<Double>> getData() {
		return data;
	}

	public void setData(Map<Double, List<Double>> data) {
		this.data = data;
	}

}
