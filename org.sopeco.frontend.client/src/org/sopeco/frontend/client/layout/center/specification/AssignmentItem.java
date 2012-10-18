package org.sopeco.frontend.client.layout.center.specification;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentItem extends FlowPanel implements HasBlurHandlers, BlurHandler {

	/**
	 * The max lenght of the displayed namespace. If the namespace is longer
	 * than this, it will be shortened by trimPath(String).
	 */
	private static final int PATH_MAX_LENGTH = 40;

	private String namespace, name, type, value;
	private TextBox textboxValue;

	private HTML htmlNamespace, htmlName, htmlType;
	private FlowPanel nestedValueTextBox;

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
	 * Initialize the necessary objects.
	 */
	private void initialize() {
		textboxValue = new TextBox();
		textboxValue.setText(value);
		textboxValue.addBlurHandler(this);

		htmlNamespace = new HTML(trimPath(namespace));
		htmlName = new HTML(name);
		htmlType = new HTML(type);
		nestedValueTextBox = new FlowPanel();

		htmlNamespace.setTitle(namespace);
		nestedValueTextBox.add(textboxValue);
	}

	/**
	 * @return the nestedValueTextBox
	 */
	public FlowPanel getNestedValueTextBox() {
		return nestedValueTextBox;
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
	 * Returns the fullname in a shortened version.
	 * 
	 * @return
	 */
	public String getShortenedNamespace() {
		return trimPath(namespace);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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

	/**
	 * @return the htmlNamespace
	 */
	public HTML getHtmlNamespace() {
		return htmlNamespace;
	}

	/**
	 * @return the htmlName
	 */
	public HTML getHtmlName() {
		return htmlName;
	}

	/**
	 * @return the htmlType
	 */
	public HTML getHtmlType() {
		return htmlType;
	}

}
