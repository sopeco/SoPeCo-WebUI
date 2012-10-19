package org.sopeco.frontend.client.helper.handler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Restricts input to numbers..
 * 
 * @author Marius Oehler
 * 
 */
public final class NumbersOnlyHandler implements KeyPressHandler, ChangeHandler {

	/** Singleton Object */
	private static NumbersOnlyHandler handler;

	/**
	 * Adding a NumbersOnlyHandler to the given TextBox.
	 * 
	 * @param textbox
	 */
	public static void setOn(TextBox textbox) {
		if (handler == null) {
			handler = new NumbersOnlyHandler();
		}

		textbox.addKeyPressHandler(handler);
		textbox.addChangeHandler(handler);
	}

	private NumbersOnlyHandler() {
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (!Character.isDigit(event.getCharCode())) {
			((TextBox) event.getSource()).cancelKey();
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		String text = ((TextBox) event.getSource()).getText();

		text = text.replaceAll("[^\\d]", "");

		((TextBox) event.getSource()).setText(text);
	}

}
