package org.sopeco.frontend.client.widget;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SmallTableLabel extends HTML {
	private static final double FONT_SIZE = 0.85D;
	private static final double TOP_MARGIN = 6D;

	public SmallTableLabel(String text) {
		super(text);
		getElement().getStyle().setColor("#666666");
		getElement().getStyle().setFontSize(FONT_SIZE, Unit.EM);
		getElement().getStyle().setMargin(0, Unit.PX);
		getElement().getStyle().setMarginTop(TOP_MARGIN, Unit.PX);
	}
}
