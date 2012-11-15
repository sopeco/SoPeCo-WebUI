package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentView extends FlowPanel {

	private static final String ASSIGNMENT_PANEL_ID = "assignmentListPanel";
	private static final String ASSIGNMENT_PANEL_WRAPPER_ID = "assignmentListPanelWrapper";

	private static final String ASSIGNMENT_ITEM_TABLE = "assignmentItemTable";

	private FlowPanel assignmentItemTable;

	public AssignmentView() {
		initialize();
	}

	/**
	 * Creates the necessary items.
	 */
	private void initialize() {
		getElement().setId(ASSIGNMENT_PANEL_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("initAssignments"));

		getElement().appendChild(headline);

		assignmentItemTable = new FlowPanel();
		assignmentItemTable.getElement().setId(ASSIGNMENT_ITEM_TABLE);

		add(assignmentItemTable);
	}

	public ScrollPanel getInScrollPanel() {
		ScrollPanel panel = new ScrollPanel(this);
		panel.getElement().setId(ASSIGNMENT_PANEL_WRAPPER_ID);
		return panel;
	}

	public void addAssignmentitem(AssignmentItem item) {
		add(item);
	}

	public void removeAssignmentitem(AssignmentItem item) {
		remove(item);
	}
}
