package org.sopeco.frontend.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendTree extends FlowPanel {

	private FrontendTreeItem rootItem;

	public FrontendTree() {
		addStyleName("frontendTree");
	}

	public void setRoot(FrontendTreeItem item) {
		rootItem = item;

		add(rootItem);

		addClearElement();
	}

	private void addClearElement() {
		Element clearDiv = DOM.createDiv();
		clearDiv.addClassName("clear");
		getElement().appendChild(clearDiv);
	}
}
