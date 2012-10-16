package org.sopeco.frontend.client.layout.center.specification;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
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
class AssignmentItem extends FlowPanel implements HasBlurHandlers, BlurHandler {

	private static final String ITEM_CSS_CLASS = "assignmentItem";
	private static int PATH_MAX_LENGTH = 40;

	private String namespace, name, type, value;
	private HTML htmlAssignment;
	private TextBox textboxValue;

	public AssignmentItem(String pNamespace, String pName, String pType) {
		this(pNamespace, pName, pType, "");
	}

	public AssignmentItem(String pNamespace, String pName, String pType, String pValue) {
		this.namespace = pNamespace;
		this.name = pName;
		this.type = pType;
		this.value = pValue;

		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		addStyleName(ITEM_CSS_CLASS);

		String fullText = "<span class=\"namespace\">" + trimPath(namespace) + "</span><span class=\"name\">" + name
				+ "</span> : " + "<span class=\"type\">" + type + "</span>";
		htmlAssignment = new HTML(fullText);

		textboxValue = new TextBox();
		textboxValue.setText(value);
		textboxValue.addBlurHandler(this);

		Element clearDiv = DOM.createDiv();
		clearDiv.addClassName("clear");

		add(htmlAssignment);
		add(textboxValue);

		getElement().appendChild(clearDiv);
	}

	/**
	 * Shortened the given path.
	 * 
	 * @return
	 */
	private String trimPath(String path) {
		if (path.length() > PATH_MAX_LENGTH) {
			String[] splitted = path.split("\\.");
			path = "";
			for (int i = splitted.length - 1; i >= 0; i--) {
				if ((path + splitted[i]).length() < PATH_MAX_LENGTH) {
					path = splitted[i] + "." + path;
				} else {
					path = splitted[i].substring(0, 1) + "." + path;
				}
			}
		}
		return path;
	}

	/**
	 * Returns the namespace path.
	 * 
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace attribute.
	 * 
	 * @param newNamespace
	 */
	public void setNamespace(String newNamespace) {
		namespace = newNamespace;
	}

	/**
	 * Returns namespace + name.
	 * 
	 * @return
	 */
	public String getFullName() {
		return namespace + name;
	}

	/**
	 * @return the textboxValue
	 */
	public TextBox getTextboxValue() {
		return textboxValue;
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addHandler(handler, BlurEvent.getType());
	}
	
	@Override
	public void onBlur(BlurEvent event) {
		DomEvent.fireNativeEvent(Document.get().createBlurEvent(), this);
	}

}
