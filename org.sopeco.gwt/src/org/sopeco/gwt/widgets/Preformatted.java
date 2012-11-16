package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class Preformatted extends SimplePanel {
	public Preformatted() {
		this("");
	}

	public Preformatted(String text) {
		super(Document.get().createElement("pre"));

		getElement().setInnerHTML(text);
	}

	/**
	 * Sets the text of the paragraph element.
	 * 
	 * @param text
	 */
	public void setHTML(String text) {
		getElement().setInnerHTML(text);
	}
}
