package org.sopeco.frontend.client.widget.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 *
 */
public class Tree extends FlowPanel {

	private static final String CSS_TREE_CLASS = "sopeco-Tree";
	private TreeItem rootItem;

	public Tree() {
		addStyleName(CSS_TREE_CLASS);
	}

	public void setRoot(TreeItem item) {
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
