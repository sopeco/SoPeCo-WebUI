package org.sopeco.frontend.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * TreeItem of the FrontendTree.
 * 
 * @author Marius Oehler
 * 
 */
public class NamespaceTreeItem extends FlowPanel implements ClickHandler {

	private static final String IMG_COLLAPSE = "images/tree_minus.png";
	private static final String IMG_EXPAND = "images/tree_plus.png";

	private String currentText;
	private HTML htmlText;
	private FlowPanel linePanel;
	private List<NamespaceTreeItem> children;
	private boolean expanded;
	private Image img;
	private NamespaceTreeItem parent;

	public NamespaceTreeItem(String html) {
		initialize();

		currentText = html;

		refresh();
	}

	/**
	 * Adding a new child item to this item.
	 * 
	 * @param item
	 */
	public void addItem(NamespaceTreeItem item) {
		item.setParentItem(this);
		children.add(item);

		refresh();
	}

	/**
	 * Return the parent item.
	 */
	public NamespaceTreeItem getParentItem() {
		return parent;
	}

	@Override
	public void onClick(ClickEvent event) {
		GWT.log("test");
		if (expanded) {
			setExpanded(false);
		} else {
			setExpanded(true);
		}
	}

	/**
	 * Removes the given item from the children list.
	 * 
	 * @param item
	 */
	public void removeItem(NamespaceTreeItem item) {
		item.setParentItem(null);
		children.remove(item);

		refresh();
	}

	/**
	 * Expand or collapse this item.
	 * 
	 * @param expand
	 */
	public void setExpanded(boolean expand) {
		expanded = expand;

		refresh();
	}

	/**
	 * Set the html text of this item.
	 * 
	 * @param html
	 */
	public void setHTML(String html) {
		currentText = html;
		htmlText.setHTML(currentText);
	}

	private void initialize() {
		htmlText = new HTML();
		expanded = true;
		children = new ArrayList<NamespaceTreeItem>();

		linePanel = new FlowPanel();
		linePanel.addStyleName("linePanel");

		img = new Image();
		img.addClickHandler(this);
		img.addStyleName("toggleIcon");

		addStyleName("treeItem");
	}

	private void refresh() {
		clear();

		updateImage();

		htmlText.setText(currentText);

		linePanel.clear();
		if (children.size() > 0) {
			linePanel.add(img);
		}
		linePanel.add(htmlText);

		// Adding the Elements
		add(linePanel);

		if (expanded) {
			for (NamespaceTreeItem item : children) {
				add(item);
			}
		}
	}

	private void setParentItem(NamespaceTreeItem item) {
		parent = item;
	}

	private void updateImage() {
		if (expanded) {
			img.setUrl(IMG_COLLAPSE);
		} else {
			img.setUrl(IMG_EXPAND);
		}
	}
}
