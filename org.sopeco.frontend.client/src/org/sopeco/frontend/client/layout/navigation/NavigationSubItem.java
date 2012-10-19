package org.sopeco.frontend.client.layout.navigation;

import org.sopeco.frontend.client.layout.center.CenterType;

import com.google.gwt.user.client.ui.HTML;

/**
 * The class of the navigation items for the experiments.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationSubItem extends NavigationItem {

	private static final String ADD_IMAGE = "<img src=\"images/add_blue.png\">";
	private String experimentName;

	public NavigationSubItem(String name, CenterType type) {
		super(type, name);
		experimentName = name;

		removeStyleName("tlEntry");
		addStyleName("slEntry");

		HTML marked = new HTML();
		marked.addStyleName("marker");
		marked.addStyleName("second");
		getContentPanel().add(marked);
	}

	/**
	 * Returns the name of this item.
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * Adds an add iamge to the label on the navi item.
	 */
	public void addAddImage() {
		getLabel().setHTML(ADD_IMAGE + getLabel().getText());
	}
}
