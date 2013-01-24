package org.sopeco.gwt.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 *
 */
public class HorizontalLine extends SimplePanel {

	public HorizontalLine() {
		super(Document.get().createElement("div"));
		addStyleName("horizontalLine");
	}
}
