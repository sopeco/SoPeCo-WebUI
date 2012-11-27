package org.sopeco.gwt.widgets;

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
	private HTML htmlText;
	private WrappedTextBox tbValue;
	private String value;
	private String validPattern = "";

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
	 * Sets the value and fires the valuechange event, if fireEvent is true.
	 * 
	 * @param newValue
	 * @param fireEvent
	 */
	public void setValue(String newValue, boolean fireEvent) {
		value = newValue;
		htmlText.setText(value);
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
		return text.matches(validPattern);
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
}
