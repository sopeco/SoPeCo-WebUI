package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class Headline extends SimplePanel {

	private static final int DEFAULT_TYPE = 3;

	public Headline() {
		this("", DEFAULT_TYPE);
	}
	
	public Headline(String text) {
		this(text, DEFAULT_TYPE);
	}

	public Headline(String text, int type) {
		super(Document.get().createElement("h" + type));

		getElement().setInnerHTML(text);
	}

	/**
	 * Sets the text of the headline element.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		getElement().setInnerHTML(text);
	}
}
