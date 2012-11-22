package org.sopeco.frontend.client.widget.grid;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EditGridItem implements ValueChangeHandler<String> {

	private String value;
	private EditableText editText;
	private ParameterDefinition parameter;

	private EditGridHandler handler;

	public EditGridItem(ConstantValueAssignment cva) {
		this(cva.getParameter(), cva.getValue());
	}

	public EditGridItem(ParameterDefinition pParameter, String pValue) {
		parameter = pParameter;
		this.value = pValue;

		editText = new EditableText(value);
		editText.addValueChangeHandler(this);

		if (parameter.getType().toLowerCase().equals("integer")) {
			editText.setValidPattern(EditableText.PATTERN_INTEGER);
		} else if (parameter.getType().toLowerCase().equals("double")) {
			editText.setValidPattern(EditableText.PATTERN_DOUBLE);
		} else if (parameter.getType().toLowerCase().equals("boolean")) {
			editText.setValidPattern(EditableText.PATTERN_BOOLEAN);
		}
	}

	/**
	 * Returns the namespace path.
	 * 
	 * @return
	 */
	public String getNamespace() {
		return parameter.getNamespace().getFullName();
	}

	/**
	 * Returns namespace + name.
	 * 
	 * @return
	 */
	public String getFullName() {
		return parameter.getFullName();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return parameter.getName();
	}

	public String getType() {
		return parameter.getType();
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

	public void setHandler(EditGridHandler pHandler) {
		this.handler = pHandler;
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

}