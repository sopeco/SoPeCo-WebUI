package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.experiment.assignment.items.AssignmentItem;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentView extends FlowPanel {
	private static final String EXP_PREPERATION_PANEL_ID = "expPreperationPanel";
	private static final String ASSIGNMENT_ITEM_TABLE = "assignmentItemTable";
	private static final String ASSIGNMENT_ITEM_TABLE_HEADER_CSS = "headerRow";

	private Headline headline;
	private FlexTable grid;

	public AssignmentView(String headlineText) {
		initialize(headlineText);
	}

	/**
	 * Inits all objects.
	 */
	private void initialize(String headlineText) {
		getElement().setId(EXP_PREPERATION_PANEL_ID);

		headline = new Headline(headlineText);
		headline.getElement().getStyle().setMarginBottom(1, Unit.EM);

		initGrid();

		add(headline);
		add(grid);
	}

	private void initGrid() {
		grid = new FlexTable();

		grid.addStyleName(ASSIGNMENT_ITEM_TABLE);
		addTableHeader();
	}

	public FlexTable getGrid() {
		return grid;
	}

	public void addTableHeader() {
		grid.getColumnFormatter().setWidth(0, "1px");
		grid.getColumnFormatter().setWidth(1, "1px");
		grid.getColumnFormatter().setWidth(2, "1px");

		grid.setText(0, 0, R.get("Namespace"));
		grid.setText(0, 1, R.get("Parameter"));
		grid.setText(0, 2, R.get("Type"));
		grid.setText(0, 3, R.get("Variation"));

		grid.getRowFormatter().addStyleName(0, ASSIGNMENT_ITEM_TABLE_HEADER_CSS);
	}

	public void addItem(int row, AssignmentItem item) {
		grid.setText(row, 0, item.getAssignment().getParameter().getNamespace().getFullName());
		grid.setText(row, 1, item.getAssignment().getParameter().getName());
		grid.setText(row, 2, item.getAssignment().getParameter().getType());
		grid.setWidget(row, 3, item.getCombobox());

		grid.setWidget(row + 1, 0, item.getParameterWrapper());

		grid.getCellFormatter().addStyleName(row, 0, "namespace");
		grid.getCellFormatter().addStyleName(row, 1, "name");
		grid.getCellFormatter().addStyleName(row, 2, "type");
		grid.getCellFormatter().addStyleName(row + 1, 0, "value");

		grid.getFlexCellFormatter().setColSpan(row + 1, 0, 4);
		grid.getRowFormatter().addStyleName(row + 1, "valueRow");
	}
}
