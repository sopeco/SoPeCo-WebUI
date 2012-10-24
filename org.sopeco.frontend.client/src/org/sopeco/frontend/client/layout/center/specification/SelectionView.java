package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.widget.tree.Tree;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectionView extends ScrollPanel {

	private static final String SELECTION_PANEL_WRAPPER_ID = "selectionViewPanelWrapper";
	private static final String SELECTION_PANEL_ID = "selectionViewPanel";
	private FlowPanel wrapper;
	private Tree tree;

	public SelectionView() {
		initialize();
	}

	/**
	 * initialize the view.
	 */
	private void initialize() {
		wrapper = new FlowPanel();
		
		wrapper.getElement().setId(SELECTION_PANEL_ID);
		getElement().setId(SELECTION_PANEL_WRAPPER_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("envParameter"));

		wrapper.getElement().appendChild(headline);

		wrapper.add(getTree());

		add(wrapper);
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		if (tree == null) {
			tree = new Tree();
		}

		return tree;
	}

	/**
	 * @param newTree
	 *            the tree to set
	 */
	public void setTree(Tree newTree) {
		tree = newTree;
		wrapper.add(tree);
	}

}
