package org.sopeco.frontend.client.layout.center.visualization.wizard;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ListBox;

public class ExtensionPanel extends ListBox {

	public ExtensionPanel() {
		this.getElement().getStyle().setBorderWidth(0, Unit.EM);
	}
	
	public void setExtensions(List<String> extensions){
		for (String ext : extensions){
			this.addItem(ext);
		}
	}
	
	public String getExtension(){
		return this.getValue(this.getSelectedIndex());
	}
	
	
}
