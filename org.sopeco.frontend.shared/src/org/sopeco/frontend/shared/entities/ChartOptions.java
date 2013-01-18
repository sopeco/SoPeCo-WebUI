package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

public class ChartOptions implements Serializable {
	private ChartType type = ChartType.LINECHART;
	
	public ChartOptions(){
		
	}

	public ChartType getType() {
		return type;
	}

	public void setType(ChartType type) {
		this.type = type;
	}

	public static enum ChartType {
		LINECHART, BARCHART, PIECHART;
	}
}
