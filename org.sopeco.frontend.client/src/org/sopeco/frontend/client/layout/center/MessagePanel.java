package org.sopeco.frontend.client.layout.center;

import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;

import com.google.gwt.dom.client.Style.Unit;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MessagePanel extends CenterPanel {
	private static MessagePanel panel;

	private Headline headline;
	private Paragraph paragraph;
	
	private MessagePanel() {
		getElement().getStyle().setPaddingLeft(1, Unit.EM);
		getElement().getStyle().setPaddingRight(1, Unit.EM);

		headline = new Headline();
		paragraph = new Paragraph();
		
		add(headline);
		add(paragraph);
	}

	public static MessagePanel createMessagePanel(String headline, String text) {
		if (panel == null) {
			panel = new MessagePanel();
		}

		panel.headline.setText(headline);
		panel.paragraph.setText(text);
		
		return panel;
	}
}
