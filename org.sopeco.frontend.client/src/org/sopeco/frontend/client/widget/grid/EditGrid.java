package org.sopeco.frontend.client.widget.grid;

import org.sopeco.frontend.client.R;

import com.google.gwt.user.client.ui.Grid;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EditGrid extends Grid {
	private static final String ASSIGNMENT_ITEM_TABLE = "assignmentItemTable";
	private static final String ASSIGNMENT_ITEM_TABLE_HEADER_CSS = "headerRow";

	public EditGrid(int rows, int columns) {
		super(rows, columns);
		init();
	}

	private void init() {
		addStyleName(ASSIGNMENT_ITEM_TABLE);
		getRowFormatter().addStyleName(0, ASSIGNMENT_ITEM_TABLE_HEADER_CSS);
		getColumnFormatter().setWidth(0, "1px");
		getColumnFormatter().setWidth(1, "1px");
		getColumnFormatter().setWidth(2, "1px");

		setText(0, 0, R.get("Namespace"));
		setText(0, 1, R.get("Parameter"));
		setText(0, 2, R.get("Type"));
		setText(0, 3, R.get("Value"));
	}

	public void addItem(int row, EditGridItem item) {
		setText(row, 0, item.getNamespace());
		setText(row, 1, item.getName());
		setText(row, 2, item.getType());
		setWidget(row, 3, item.getEditText());

		getCellFormatter().addStyleName(row, 0, "namespace");
		getCellFormatter().addStyleName(row, 1, "name");
		getCellFormatter().addStyleName(row, 2, "type");
	}
}
