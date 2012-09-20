package org.sopeco.frontend.client.layout.popups;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Handler for the OK Button of the {@link TextInput} Dialog.
 * 
 * @author Marius Oehler
 * 
 */
public interface TextInputOkHandler {

	void onInput(ClickEvent event, String input);

}
