package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.widget.grid.EditGrid;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
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
	private static final String ASSIGNMENT_ITEM_TABLE_HEADER_CSS = "headerRow";
	private static final String NO_ASSIGNMENTS_CSS_CLASS = "noAssignments";

	private EditGrid grid;
	private HTML htmlNoAssignments;

	public AssignmentView() {
		initialize();
	}

	/**
	 * Creates the necessary items.
	 */
	private void initialize() {
		getElement().setId(ASSIGNMENT_PANEL_ID);

		Headline headline = new Headline(R.get("initAssignments"));

		grid = new EditGrid(1, 4);

		htmlNoAssignments = new HTML(R.get("noInitAssignments"));
		htmlNoAssignments.setVisible(false);
		htmlNoAssignments.addStyleName(NO_ASSIGNMENTS_CSS_CLASS);

		add(headline);
		add(grid);
		add(htmlNoAssignments);
	}

	public ScrollPanel getInScrollPanel() {
		ScrollPanel panel = new ScrollPanel(this);
		panel.getElement().setId(ASSIGNMENT_PANEL_WRAPPER_ID);
		return panel;
	}

	public EditGrid getGrid() {
		return grid;
	}

	public HTML getHtmlNoAssignments() {
		return htmlNoAssignments;
	}

}
