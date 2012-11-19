package org.sopeco.gwt.widgets.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TreeItem extends FlowPanel implements ClickHandler {

	private static final String CSS_CONTENT_CLASS = "content";
	private static final String CSS_TREE_ITEM_CLASS = "sopeco-TreeItem";
	private static final String IMG_COLLAPSE = "images/tree_minus.png";
	private static final String IMG_EXPAND = "images/tree_plus.png";
	private static final String IMG_NO_CHILDREN = "images/tree_minus_inactive.png";
	private static final String CSS_TRANSPARENT = "transparent-50";

	private static final String PATH_SEPERATOR = "/";

	private String text;
	private Image image;
	private Element textElement;
	private FlowPanel contentWrapper;
	private boolean expanded;

	/** The children items, which are on this item. */
	private List<TreeItem> childrenItems;
	/** The parent item. */
	private TreeItem parentItem;

	public TreeItem(String pText, boolean noContent) {
		text = pText;

		initialize(noContent);
	}

	public TreeItem(String pText) {
		this(pText, false);
	}

	protected void initialize(boolean noContent) {
		if (!noContent) {
			addStyleName(CSS_TREE_ITEM_CLASS);
		}

		childrenItems = new ArrayList<TreeItem>();
		expanded = true;

		contentWrapper = new FlowPanel();
		contentWrapper.addStyleName(CSS_CONTENT_CLASS);

		image = new Image(IMG_NO_CHILDREN);
		image.addStyleName(CSS_TRANSPARENT);
		image.addClickHandler(this);

		textElement = DOM.createSpan();
		textElement.setInnerHTML(text);

		if (!noContent) {
			contentWrapper.add(image);
			contentWrapper.getElement().appendChild(textElement);
		}

		add(contentWrapper);
	}

	/**
	 * 
	 */
	protected FlowPanel getContentWrapper() {
		return contentWrapper;
	}

	/**
	 * 
	 */
	public List<TreeItem> getChildrenItems() {
		return new ArrayList<TreeItem>(childrenItems);
	}

	/**
	 * Removes the icon to collapse this node.
	 */
	protected void removeIcon() {
		image.removeFromParent();
	}

	/**
	 * Adding a new child item to this item.
	 * 
	 * @param item
	 */
	public void addItem(TreeItem item) {
		item.setParentItem(this);
		childrenItems.add(item);
		add(item);

		updateImage();
	}

	/**
	 * Updates the style and the url of the image.
	 */
	private void updateImage() {
		if (childrenItems.isEmpty()) {
			image.setUrl(IMG_NO_CHILDREN);
			image.addStyleName(CSS_TRANSPARENT);
			image.getElement().getStyle().setCursor(Cursor.DEFAULT);
		} else {
			image.getElement().getStyle().setCursor(Cursor.POINTER);
			image.removeStyleName(CSS_TRANSPARENT);
			if (expanded) {
				image.setUrl(IMG_COLLAPSE);
			} else {
				image.setUrl(IMG_EXPAND);
			}
		}
	}

	/**
	 * Sets the parent item.
	 */
	protected void setParentItem(TreeItem item) {
		parentItem = item;
	}

	/**
	 * Returns the parent of this treeItem.
	 * 
	 * @return
	 */
	protected TreeItem getparentItem() {
		return parentItem;
	}

	/**
	 * Returns the current text.
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!childrenItems.isEmpty()) {
			if (expanded) {
				expanded = false;
				setChildrenVisibility(false);
			} else {
				expanded = true;
				setChildrenVisibility(true);
			}
			updateImage();
		}
	}

	/**
	 * Hides or show the child items.
	 * 
	 * @param visible
	 */
	private void setChildrenVisibility(boolean visible) {
		for (TreeItem item : childrenItems) {
			item.setVisible(visible);
		}
	}

	/**
	 * Returns the full name.
	 * 
	 * @return
	 */
	public String getPath() {
		return getPath(PATH_SEPERATOR);
	}

	/**
	 * Returns the full name.
	 * 
	 * @return
	 */
	public String getPath(String seperator) {
		if (parentItem != null) {
			return parentItem.getPath() + seperator + getText();
		}
		return getText();
	}
}
