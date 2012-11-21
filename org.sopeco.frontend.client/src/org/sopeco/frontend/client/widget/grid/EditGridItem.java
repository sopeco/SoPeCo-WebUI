package org.sopeco.frontend.client.widget.grid;

import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @author Marius Oehler
 *
 */
public class EditGridItem implements ValueChangeHandler<String> {

	private String namespace, name, type, value;
	private EditableText editText;

	private EditGridHandler handler;

	public EditGridItem(String pNamespace, String pName, String pType) {
		this(pNamespace, pName, pType, "");
	}

	public EditGridItem(String pNamespace, String pName, String pType, String pValue) {
		this.namespace = pNamespace;
		this.name = pName;
		this.type = pType;
		this.value = pValue;

		editText = new EditableText(value);
		editText.addValueChangeHandler(this);
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public EditableText getEditText() {
		return editText;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		value = event.getValue();
		if (handler != null) {
			handler.onValueChange(this);
		}
	}

	public void setController(EditGridHandler pHandler) {
		this.handler = pHandler;
	}

}