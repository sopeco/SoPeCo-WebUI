package org.sopeco.frontend.client.helper.handler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Restricts input to numbers and letters.
 * 
 * @author Marius Oehler
 * 
 */
public class NoSpecialCharsHandler implements KeyPressHandler, ChangeHandler {

	@Override
	public void onKeyPress(KeyPressEvent event) {
		/*
		 * KeyCodes: 46: ENTF, 37-40: LEFT UP DOWN RIGHT, 8: BACKSPACE
		 */
		if (!Character.isLetterOrDigit(event.getCharCode()) && event.getCharCode() != '_'
				&& event.getNativeEvent().getKeyCode() != 8 && event.getNativeEvent().getKeyCode() != 46
				&& (event.getNativeEvent().getKeyCode() < 37 || event.getNativeEvent().getKeyCode() > 40)) {
			((TextBox) event.getSource()).cancelKey();
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		String text = ((TextBox) event.getSource()).getText();

		text = text.replaceAll("[^\\w]", "");

		((TextBox) event.getSource()).setText(text);
	}

}