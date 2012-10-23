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
public class ExperimentAssignmentView extends FlowPanel {
	private static final String EXP_ASSIGNMENT_PANEL_ID = "expAssignmentsPanel";

	public ExperimentAssignmentView() {
		initialize();
	}

	/**
	 * Inits all objects.
	 */
	private void initialize() {
		getElement().setId(EXP_ASSIGNMENT_PANEL_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("expAssignsments"));
		getElement().appendChild(headline);
	}
}
