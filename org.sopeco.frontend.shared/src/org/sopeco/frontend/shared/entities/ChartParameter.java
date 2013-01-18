package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

public class ChartParameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4409439863166448986L;
	
	public static final int INPUT = 0;
	public static final int OBSERVATION = 1;
	private String parameterName;
	private int type = INPUT;	

	public ChartParameter() {
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
