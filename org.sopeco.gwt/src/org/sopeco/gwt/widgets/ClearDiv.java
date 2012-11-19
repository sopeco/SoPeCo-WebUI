package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ClearDiv extends SimplePanel {

	public ClearDiv() {
		super(Document.get().createElement("div"));
		getElement().getStyle().setProperty("clear", "both");
	}
}
