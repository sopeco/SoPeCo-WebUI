/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
