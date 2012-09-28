package org.sopeco.frontend.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * TreeItem of the FrontendTree.
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendTreeItem extends FlowPanel implements ClickHandler {

	private static final String IMG_COLLAPSE = "images/tree_minus.png";
	private static final String IMG_EXPAND = "images/tree_plus.png";

	private String currentText;
	protected HTML htmlText;
	protected FlowPanel linePanel;
	protected List<FrontendTreeItem> children;
	private boolean expanded, focusable;
	protected Image img;
	protected FrontendTreeItem parentItem;
	private FocusPanel lineFocusPanel;
	protected Element clearLine;

	public FrontendTreeItem(String html) {
		initialize();

		currentText = html;

		refresh();
	}

	/**
	 * Adding a new child item to this item.
	 * 
	 * @param item
	 */
	public void addItem(FrontendTreeItem item) {
		item.setParentItem(this);
		children.add(item);

		refresh();
	}

	/**
	 * Return the parent item.
	 */
	public FrontendTreeItem getParentItem() {
		return parentItem;
	}

	@Override
	public void onClick(ClickEvent event) {
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
	public void removeItem(FrontendTreeItem item) {
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
	 * Is the element focusable.
	 * 
	 * @param focus
	 */
	public void setFocusable(boolean focus) {
		focusable = focus;
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

	protected void initialize() {
		htmlText = new HTML();
		expanded = true;
		focusable = false;
		children = new ArrayList<FrontendTreeItem>();
		lineFocusPanel = new FocusPanel();

		clearLine = DOM.createDiv();
		clearLine.addClassName("clear");

		linePanel = new FlowPanel();
		linePanel.addStyleName("linePanel");

		img = new Image();
		img.addClickHandler(this);
		img.addStyleName("toggleIcon");

		addStyleName("treeItem");
	}

	protected void refresh() {
		clear();

		updateImage();

		htmlText.setText(currentText);

		refreshLinePanel();

		addElementsToPanel();

	}

	protected void refreshLinePanel() {
		linePanel.clear();
		if (children.size() > 0) {
			linePanel.add(img);
		}
		linePanel.add(htmlText);
		linePanel.getElement().appendChild(clearLine);
	}

	protected void addElementsToPanel() {
		lineFocusPanel.clear();
		if (focusable) {
			lineFocusPanel.add(linePanel);
			add(lineFocusPanel);
		} else {
			add(linePanel);
		}

		if (expanded) {
			for (FrontendTreeItem item : children) {
				item.refresh();
				add(item);
			}
		}
	}

	private void setParentItem(FrontendTreeItem item) {
		parentItem = item;
	}

	private void updateImage() {
		if (expanded) {
			img.setUrl(IMG_COLLAPSE);
		} else {
			img.setUrl(IMG_EXPAND);
		}
	}

	public String getPath() {
		if (parentItem != null) {
			return parentItem.getPath() + "/" + currentText;
		}
		return currentText;
	}

	/**
	 * Removes this item from the parent element.
	 */
	public void remove() {
		parentItem.removeItem(this);
		parentItem = null;
	}

	/**
	 * Removes the given item from the children list.
	 * 
	 * @param item
	 *            child, which will be removed
	 */
	public void removeChild(FrontendTreeItem item) {
		children.remove(item);

		refresh();
	}
}
