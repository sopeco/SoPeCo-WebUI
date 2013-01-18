package org.sopeco.frontend.client.layout.center.visualization.types;

import org.sopeco.frontend.client.R;

import com.google.gwt.user.client.ui.Image;

public enum VisualizationTypes {
	RPLOT(R.get("rplot"),new Image("images/r_logo_big.jpg"),2), LINE_CHART(R.get("linechart"),null, 2);
	
	private String name;
	private Image image;
	private int maxColumnNr;
	
	private VisualizationTypes(String name, Image image, int maxColumnNr) {
		this.name = name;
		this.image = image;
		this.maxColumnNr = maxColumnNr;
	}

	public String getName() {
		return name;
	}

	public Image getImage() {
		return image;
	}
	
	public int getMaxColumnNr(){
		return maxColumnNr;
	}
}
