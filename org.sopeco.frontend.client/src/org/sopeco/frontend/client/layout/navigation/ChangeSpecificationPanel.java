package org.sopeco.frontend.client.layout.navigation;

import java.util.HashMap;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The panel, where you can change the current specification.
 * 
 * @author Marius Oehler
 * 
 */
public class ChangeSpecificationPanel extends VerticalPanel {

	private static final String CHANGE_SPECIFICATION_PANEL_ID = "changeSpecPanel";
	private HashMap<String, HTML> itemMap;

	public ChangeSpecificationPanel() {
		getElement().setId(CHANGE_SPECIFICATION_PANEL_ID);

		itemMap = new HashMap<String, HTML>();

		setVisible(false);
	}

	/**
	 * The map, where all existing items are stored.
	 * 
	 * @return the HashMap
	 */
	public HashMap<String, HTML> getItemMap() {
		return itemMap;
	}

	/**
	 * Adds a new item to this panel and to the HashMap. The created Element
	 * will be returned.
	 * 
	 * @param text
	 *            the text of the item
	 * @return the created element
	 */
	public HTML addItem(String text) {
		HTML newItem = new HTML(text);
		add(newItem);

		itemMap.put(text, newItem);

		return newItem;
	}
}
