package org.sopeco.webui.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalRuler extends Widget {

	private static HorizontalRulerUiBinder uiBinder = GWT.create(HorizontalRulerUiBinder.class);

	interface HorizontalRulerUiBinder extends UiBinder<Element, HorizontalRuler> {
	}

	@UiField
	HRElement element;

	public HorizontalRuler() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public void setColor(String color) {
		element.getStyle().setBorderColor(color);
	}

	public void setStyle(BorderStyle style) {
		element.getStyle().setBorderStyle(style);
	}

	public void setLineWidth(int width) {
		element.getStyle().setBorderWidth(width, Unit.PX);
	}
}
