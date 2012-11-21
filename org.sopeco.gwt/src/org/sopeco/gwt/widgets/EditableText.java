package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EditableText extends FocusPanel implements ClickHandler,
		BlurHandler, HasValueChangeHandlers<String>, FocusHandler {

	private static final String STYLE_CLASS = "sopeco-EditableText";
	private HTML htmlText;
	private WrappedTextBox tbValue;
	private String value;

	public EditableText(String text) {
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

		htmlText.addClickHandler(this);
		tbValue.getTextbox().addBlurHandler(this);

		add(htmlText);
	}

	@Override
	public void onClick(ClickEvent event) {
		edit();
	}

	private void edit() {
		tbValue.getTextbox().setValue(value);
		htmlText.removeFromParent();
		add(tbValue);
		tbValue.getTextbox().setFocus(true);
	}

	@Override
	public void onBlur(BlurEvent event) {
		if (!value.equals(tbValue.getTextbox().getText())) {
			value = tbValue.getTextbox().getText();
			ValueChangeEvent.fire(this, value);
		}

		if (value.isEmpty()) {
			htmlText.setHTML("&nbsp;");
		} else {
			htmlText.setText(value);
		}
		tbValue.removeFromParent();
		add(htmlText);
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
}
