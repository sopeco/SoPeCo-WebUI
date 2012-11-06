package org.sopeco.frontend.shared.definitions.result;

import java.io.Serializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SharedExperimentRuns implements Serializable {
	/**	 */
	private static final long serialVersionUID = 1L;

	private SharedExperimentSeries parentSeries;

	private long timestamp;

	private String label;

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @return the parentSeries
	 */
	public SharedExperimentSeries getParentSeries() {
		return parentSeries;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param parentSeries
	 *            the parentSeries to set
	 */
	public void setParentSeries(SharedExperimentSeries parentSeries) {
		this.parentSeries = parentSeries;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
