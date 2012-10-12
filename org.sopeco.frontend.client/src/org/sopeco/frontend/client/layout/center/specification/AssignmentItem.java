package org.sopeco.frontend.client.layout.center.specification;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentItem extends FlowPanel {

	private static final String ITEM_CSS_CLASS = "assignmentItem";

	private String namespace, name, type;
	private HTML htmlAssignment;
	private TextBox textboxValue;

	public AssignmentItem(String pNamespace, String pName, String pType) {
		this.namespace = pNamespace;
		this.name = pName;
		this.type = pType;

		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		addStyleName(ITEM_CSS_CLASS);

		String fullText = "<span class=\"namespace\">" + namespace + "</span><span class=\"name\">" + name
				+ "</span> : " + "<span class=\"type\">" + type + "</span>";
		htmlAssignment = new HTML(fullText);

		textboxValue = new TextBox();

		Element clearDiv = DOM.createDiv();
		clearDiv.addClassName("clear");

		add(htmlAssignment);
		add(textboxValue);

		getElement().appendChild(clearDiv);
	}
}
