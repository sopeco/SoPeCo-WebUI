package org.sopeco.frontend.client.layout.center.experiment.assignment;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentView extends FlowPanel {
	private static final String EXP_PREPERATION_PANEL_ID = "expPreperationPanel";

	private Element headline;

	public AssignmentView(String headlineText) {
		initialize(headlineText);
	}

	/**
	 * Inits all objects.
	 */
	private void initialize(String headlineText) {
		getElement().setId(EXP_PREPERATION_PANEL_ID);

		headline = DOM.createElement("h3");
		headline.setInnerHTML(headlineText);
		getElement().appendChild(headline);
	}

}
