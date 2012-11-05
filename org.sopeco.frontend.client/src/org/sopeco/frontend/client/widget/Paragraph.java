package org.sopeco.frontend.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class Paragraph extends SimplePanel {

	public Paragraph() {
		this("");
	}

	public Paragraph(String text) {
		super(Document.get().createElement("p"));

		getElement().setInnerHTML(text);
	}

	/**
	 * Sets the text of the paragraph element.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		getElement().setInnerHTML(text);
	}
}
