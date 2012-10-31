package org.sopeco.frontend.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Headline extends FocusWidget {

	private static final int DEFAULT_TYPE = 3;

	private Headline(Element element) {
		super(element);
	}

	public static Headline create(String text, int type) {
		Element headlineElement = Document.get().createElement("h" + type);
		headlineElement.setInnerHTML(text);
		headlineElement.getStyle().setProperty("outline", "none");
		
		return new Headline(headlineElement);
	}

	public static Headline create(String text) {
		return create(text, DEFAULT_TYPE);
	}
}
