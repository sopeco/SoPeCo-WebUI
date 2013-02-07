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

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EditableText extends FocusPanel implements ClickHandler,
		BlurHandler, HasValueChangeHandlers<String>, FocusHandler, KeyUpHandler {

	/** regular expression. \\d* */
	public static final String PATTERN_INTEGER = "\\d*";
	/** regular expression. \\d*(\\.\\d+)? */
	public static final String PATTERN_DOUBLE = "\\d*(\\.\\d+)?";
	/** regular expression. true|false */
	public static final String PATTERN_BOOLEAN = "true|false";

	private static final String STYLE_CLASS = "sopeco-EditableText";
	private static final String DEFAULT_CLASS = "sopeco-EditableText-Default";
	private HTML htmlText;
	private WrappedTextBox tbValue;
	private String value;
	private String validPattern = "";
	private String defaultValue;
	private boolean editable = true;
	private boolean isPassword = false;

	public EditableText(String text) {
		WidgetResources.resc.editableTextCss().ensureInjected();

		addStyleName(STYLE_CLASS);
		addFocusHandler(this);

		value = text;
		htmlText = new HTML();
		if (value.isEmpty()) {
			htmlText.setHTML("&nbsp;");
		} else {
			htmlText.setText(value);
		}

		htmlText.getElement().getStyle().setCursor(Cursor.POINTER);

		tbValue = new WrappedTextBox();
		tbValue.getTextbox().addKeyUpHandler(this);
		tbValue.getTextbox().addBlurHandler(this);

		htmlText.addClickHandler(this);

		sinkEvents(Event.ONPASTE);

		add(htmlText);
	}

	/**
	 * Sets the value.
	 * 
	 * @param newValue
	 */
	public void setValue(String newValue) {
		setValue(newValue, false);
	}

	/**
	 * Sets whether the EditableText is edtiable.
	 */
	public void setEditable(boolean pEditable) {
		editable = pEditable;
	}

	/**
	 * Sets the defualt String. If this String is entered, the DEFAULT_CSS class
	 * is added to the element.
	 * 
	 * @param pDefaultValue
	 */
	public void setDefaultValue(String pDefaultValue) {
		this.defaultValue = pDefaultValue;
		checkDefaultValue();
	}

	/**
	 * Sets the value and fires the valuechange event, if fireEvent is true.
	 * 
	 * @param newValue
	 * @param fireEvent
	 */
	public void setValue(String newValue, boolean fireEvent) {
		value = newValue;
		setHtmlText(value);
		tbValue.getTextbox().setText(value);
		if (fireEvent) {
			ValueChangeEvent.fire(this, value);
		}
	}

	public void setValidPattern(String pValidPattern) {
		this.validPattern = pValidPattern;
	}

	@Override
	public void onClick(ClickEvent event) {
		edit();
	}

	private void edit() {
		if (!editable) {
			// setFocus(false);
			return;
		}
		tbValue.getTextbox().setValue(value);
		htmlText.removeFromParent();
		add(tbValue);
		tbValue.getTextbox().setFocus(true);
		checkTextboxContent(tbValue.getTextbox());
	}

	@Override
	public void onBlur(BlurEvent event) {
		if (!value.equals(tbValue.getTextbox().getText())) {
			if (isValid(tbValue.getTextbox().getText())) {
				value = tbValue.getTextbox().getText();
				ValueChangeEvent.fire(this, value);
			} else {
				tbValue.getTextbox().setText(value);
			}
		}

		checkDefaultValue();

		if (value.isEmpty()) {
			htmlText.setHTML("&nbsp;");
		} else {
			setHtmlText(value);
		}
		tbValue.removeFromParent();
		add(htmlText);
	}

	private void setHtmlText(String text) {
		if (isPassword) {
			String hidden = "";
			for (int i = 0; i < text.length(); i++) {
				hidden += "*";
			}
			htmlText.setText(hidden);
		} else {
			htmlText.setText(value);
		}
	}

	private void checkDefaultValue() {
		if (defaultValue != null && value.equals(defaultValue)) {
			addStyleName(DEFAULT_CLASS);
		} else {
			removeStyleName(DEFAULT_CLASS);
		}
	}

	@Override
	public void onFocus(FocusEvent event) {
		edit();
	}

	public String getValue() {
		return value;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		checkTextboxContent((TextBox) event.getSource());
	}

	private void checkTextboxContent(TextBox source) {
		String newText = source.getText();
		if (!isValid(newText)) {
			source.getElement().getStyle().setColor("red");
		} else {
			source.getElement().getStyle().setColor("black");
		}
	}

	private boolean isValid(String text) {
		if (validPattern.isEmpty()) {
			return true;
		}
		RegExp regExp = RegExp.compile(validPattern);
		MatchResult matcher = regExp.exec(text);
		return (matcher != null);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONPASTE:
			GWT.log("paste");
			break;
		default:
			break;
		}
	}

	public void setAsPassword() {
		isPassword = true;
		tbValue.setAsPasswordTextbox();
		if (!value.isEmpty()) {
			setHtmlText(value);
		}
	}
}
