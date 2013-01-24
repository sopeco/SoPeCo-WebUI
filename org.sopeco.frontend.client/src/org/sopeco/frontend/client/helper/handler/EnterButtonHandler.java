package org.sopeco.frontend.client.helper.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnterButtonHandler implements KeyPressHandler {

	private Button target;

	public EnterButtonHandler(Button pTarget) {
		target = pTarget;
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER) {
			target.fireEvent(new ClickEvent() {
			});
		}
	}

}
