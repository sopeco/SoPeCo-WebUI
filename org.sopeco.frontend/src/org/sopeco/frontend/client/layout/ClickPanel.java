package org.sopeco.frontend.client.layout;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * FocusPanel without focus feature. Only a wrapper for eg. Horizontalpanel to
 * attach a clickhandler.
 * 
 * @author Marius Oehler
 * 
 */
public class ClickPanel extends FocusPanel {
	public ClickPanel() {
		addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setFocus(false);
			}
		});
	}
}
