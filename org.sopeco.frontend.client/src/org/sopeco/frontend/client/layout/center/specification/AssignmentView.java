package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
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

	private Grid itemTable;
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

		itemTable = new Grid(1, 4);
		itemTable.addStyleName(ASSIGNMENT_ITEM_TABLE);
		itemTable.getRowFormatter().addStyleName(0, ASSIGNMENT_ITEM_TABLE_HEADER_CSS);
		itemTable.getColumnFormatter().setWidth(0, "1px");
		itemTable.getColumnFormatter().setWidth(1, "1px");
		itemTable.getColumnFormatter().setWidth(2, "1px");

		itemTable.setText(0, 0, R.get("Namespace"));
		itemTable.setText(0, 1, R.get("Parameter"));
		itemTable.setText(0, 2, R.get("Type"));
		itemTable.setText(0, 3, R.get("Value"));

		htmlNoAssignments = new HTML(R.get("noInitAssignments"));
		htmlNoAssignments.setVisible(false);
		htmlNoAssignments.addStyleName(NO_ASSIGNMENTS_CSS_CLASS);

		add(headline);
		add(itemTable);
		add(htmlNoAssignments);
	}

	public ScrollPanel getInScrollPanel() {
		ScrollPanel panel = new ScrollPanel(this);
		panel.getElement().setId(ASSIGNMENT_PANEL_WRAPPER_ID);
		return panel;
	}

	public Grid getItemTable() {
		return itemTable;
	}

	public void setAssignmentItem(int row, AssignmentItem item) {
		itemTable.setText(row, 0, item.getNamespace());
		itemTable.setText(row, 1, item.getName());
		itemTable.setText(row, 2, item.getType());
		itemTable.setWidget(row, 3, item.getEditText());

		itemTable.getCellFormatter().addStyleName(row, 0, "namespace");
		itemTable.getCellFormatter().addStyleName(row, 1, "name");
		itemTable.getCellFormatter().addStyleName(row, 2, "type");
	}

	public HTML getHtmlNoAssignments() {
		return htmlNoAssignments;
	}

}
