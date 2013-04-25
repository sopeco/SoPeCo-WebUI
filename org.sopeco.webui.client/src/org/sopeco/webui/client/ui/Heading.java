package org.sopeco.webui.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class Heading extends Widget {

	private static HeadingUiBinder uiBinder = GWT.create(HeadingUiBinder.class);

	interface HeadingUiBinder extends UiBinder<Element, Heading> {
	}

	@UiField
	HeadingElement element;

	public Heading() {
		setElement(uiBinder.createAndBindUi(this));
	}

	public void setTitle (String title) {
		element.setInnerHTML(title);
	}
}
