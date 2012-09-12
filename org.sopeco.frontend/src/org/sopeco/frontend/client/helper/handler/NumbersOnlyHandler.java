package org.sopeco.frontend.client.helper.handler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

public class NumbersOnlyHandler implements KeyPressHandler, ChangeHandler {

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if ( !Character.isDigit(event.getCharCode()) )
			((TextBox)event.getSource()).cancelKey();
	}

	@Override
	public void onChange(ChangeEvent event) {
		String text = ((TextBox)event.getSource()).getText();
		
		text = text.replaceAll("[^\\d]", "");

		((TextBox)event.getSource()).setText(text);
	}

}
