package org.sopeco.webui.shared.entities;

import java.io.Serializable;
import java.util.List;

public class VisualizationBundle implements Serializable {

	List<Visualization> visualizations;
	private int totalNumberOfVisualizations;

	public List<Visualization> getVisualizations() {
		return visualizations;
	}

	public void setVisualizations(List<Visualization> visualizations) {
		this.visualizations = visualizations;
	}

	public int getTotalNumberOfVisualizations() {
		return totalNumberOfVisualizations;
	}

	public void setTotalNumberOfVisualizations(int totalNumberOfVisualizations) {
		this.totalNumberOfVisualizations = totalNumberOfVisualizations;
	}

}
