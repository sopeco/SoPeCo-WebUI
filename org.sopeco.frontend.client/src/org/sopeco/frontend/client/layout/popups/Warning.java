package org.sopeco.frontend.client.layout.popups;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Warning {

	/**
	 * Shows a warnig bar at the top of the screen.
	 * 
	 * @param warning
	 */
	public static void showWarning(String warning) {
		DOM.getElementById("topWarning").setInnerHTML(warning);
		DOM.getElementById("topWarning").getStyle().setDisplay(Display.BLOCK);
	}

	/**
	 * Hides the warning bar.
	 */
	public static void hideWarning() {
		DOM.getElementById("topWarning").getStyle().setDisplay(Display.NONE);
	}

	private Warning() {
	}

}
