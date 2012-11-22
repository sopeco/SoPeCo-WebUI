package org.sopeco.gwt.widgets;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExtendedTextBox extends TextBox implements FocusHandler,
		BlurHandler, ValueChangeHandler<String> {

	private static final String DEFAULT_COLOR = "#AAAAAA";

	private String defaultValue = null;
	private boolean allowEmptyText;

	public ExtendedTextBox(String pDefaultValue, boolean pAllowEmptyText) {
		super();

		defaultValue = pDefaultValue;
		allowEmptyText = pAllowEmptyText;

		addBlurHandler(this);
		addValueChangeHandler(this);
		addFocusHandler(this);

		setDefaultValue();
	}

	public ExtendedTextBox() {
		super();
	}

	/**
	 * Set the value of the textBox to the default value and the color to the
	 * default color.
	 */
	private void setDefaultValue() {
		setText(defaultValue);
		getElement().getStyle().setColor(DEFAULT_COLOR);
	}

	@Override
	public void onBlur(BlurEvent event) {
		check();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		check();
	}

	@Override
	public void setText(String text) {
		super.setText(text);
		if (defaultValue == null || !text.equals(defaultValue)) {
			getElement().getStyle().clearColor();
		}
	}

	/**
	 * Checks checks whether the vauel is the default value and reset textbox if
	 * neccesarry.
	 */
	private void check() {
		if (defaultValue == null) {
			return;
		}
		if (getText().equals(defaultValue)
				|| (!allowEmptyText && getText().isEmpty())) {
			setDefaultValue();
		}
	}

	@Override
	public void onFocus(FocusEvent event) {
		if (defaultValue == null) {
			return;
		}

		getElement().getStyle().clearColor();
		if (getText().equals(defaultValue)) {
			setText("");
		}
	}
}
