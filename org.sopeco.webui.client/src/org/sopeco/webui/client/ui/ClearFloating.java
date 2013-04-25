package org.sopeco.webui.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ClearFloating extends Composite {

	private static ClearFloatingUiBinder uiBinder = GWT.create(ClearFloatingUiBinder.class);

	interface ClearFloatingUiBinder extends UiBinder<Widget, ClearFloating> {
	}

	public ClearFloating() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
