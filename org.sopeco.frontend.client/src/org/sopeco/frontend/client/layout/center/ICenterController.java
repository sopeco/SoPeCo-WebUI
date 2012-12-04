package org.sopeco.frontend.client.layout.center;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ICenterController {

	Widget getView();

	void reset();

	/**
	 * Is called, when the view is displayed because of the
	 * {@link ViewSwitch#switchTo(CenterType)}.
	 */
	void onSwitchTo();
}
