package org.sopeco.frontend.client.layout.navigation;

import java.util.HashMap;

import org.sopeco.frontend.client.R;

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
	private static final String ADD_SPECIFICATION_IMAGE = "<img src=\"images/add_blue.png\" />";
	private static final String ADD_SPECIFIACTION_ID = "addSpecification";

	private HashMap<String, HTML> itemMap;
	private HTML addSpecificationHTML;

	public ChangeSpecificationPanel() {
		getElement().setId(CHANGE_SPECIFICATION_PANEL_ID);

		itemMap = new HashMap<String, HTML>();

		addSpecificationHTML = new HTML(ADD_SPECIFICATION_IMAGE + R.get("addSpecification"));
		addSpecificationHTML.getElement().setId(ADD_SPECIFIACTION_ID);
		add(addSpecificationHTML);

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
	 * Returns the "add Spec" HTML Element.
	 * 
	 * @return HTML
	 */
	public HTML getAddSpecificationHTML() {
		return addSpecificationHTML;
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
		remove(addSpecificationHTML);

		HTML newItem = new HTML(text);
		add(newItem);
		add(addSpecificationHTML);

		itemMap.put(text, newItem);
		return newItem;
	}
}
