package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentView extends FlowPanel {

	private static final String EXP_PREPERATION_PANEL_ID = "expPreperationPanel";

	private Element headline;

	public PreperationAssignmentView() {
		initialize();
	}

	/**
	 * Inits all objects.
	 */
	private void initialize() {
		getElement().setId(EXP_PREPERATION_PANEL_ID);

		headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("prepAssignments"));
		getElement().appendChild(headline);
	}
}