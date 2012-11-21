package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class WrappedTextBox extends SimplePanel {

	private static final String CSS_CLASS = "sopeco-WrappedTextBox";
	private TextBox textbox;

	public WrappedTextBox() {
		addStyleName(CSS_CLASS);
		addStyleName("gwt-TextBox");
		textbox = new TextBox();
		textbox.getElement().getStyle().setMargin(0, Unit.PX);
		textbox.getElement().getStyle().setPadding(0, Unit.PX);
		textbox.getElement().getStyle().setBorderWidth(0, Unit.PX);
		textbox.getElement().getStyle().setProperty("outline", "0");
		textbox.getElement().getStyle().setProperty("minWidth", "80px");
		textbox.setWidth("100%");
		getElement().getStyle().setBackgroundColor("white");
		add(textbox);
	}

	public TextBox getTextbox() {
		return textbox;
	}
}
