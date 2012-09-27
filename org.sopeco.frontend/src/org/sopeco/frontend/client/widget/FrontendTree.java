package org.sopeco.frontend.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendTree extends FlowPanel {

	private NamespaceTreeItem rootItem;

	public FrontendTree() {
		addStyleName("frontendTree");
	}

	public void setRoot(NamespaceTreeItem item) {
		rootItem = item;
		
		add(rootItem);
	}

}
