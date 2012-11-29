package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduleConfigTable extends SimplePanel {
	private static final String CONFIG_TABLE_CLASS = "scheduleConfigTable";

	private HTML htmlStartTime, htmlStartDate, htmlRepeat;
	private EditableText editStartTime, editStartDate;
	private CheckBox cbRepeat;
	private FlexTable table;

	public ScheduleConfigTable() {
		init();
	}

	private void init() {
		addStyleName(CONFIG_TABLE_CLASS);

		htmlStartTime = new HTML(R.get("StartTime") + ":");
		htmlStartDate = new HTML(R.get("StartDate") + ":");
		htmlRepeat = new HTML(R.get("Repeat") + ":");

		editStartTime = new EditableText("12:15");
		editStartDate = new EditableText("29.11.2012");

		cbRepeat = new CheckBox();

		table = new FlexTable();

		table.setWidget(0, 0, htmlStartTime);
		table.setWidget(0, 1, editStartTime);
		table.setWidget(0, 2, htmlStartDate);
		table.setWidget(0, 3, editStartDate);
		table.setWidget(1, 0, htmlRepeat);
		table.setWidget(1, 1, cbRepeat);

		table.getFlexCellFormatter().setColSpan(1, 1, 3);
		table.getCellFormatter().setWidth(0, 0, "100px");
		table.getCellFormatter().setWidth(0, 1, "100px");
		table.getCellFormatter().setWidth(0, 2, "100px");
		table.getCellFormatter().setWidth(0, 3, "100px");

		add(table);
	}
}
