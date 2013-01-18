package org.sopeco.frontend.client.layout.center.visualization.types;

import com.google.gwt.user.client.ui.Widget;

public interface VisualizationType {
	
	public Widget getWidget();
	
	public String getName();
	
	public boolean isLoading();
}
