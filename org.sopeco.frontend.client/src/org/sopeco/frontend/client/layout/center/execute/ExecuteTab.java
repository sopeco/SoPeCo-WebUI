package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteTab extends FlowPanel implements ValueChangeHandler<Boolean> {

	private static final String CONFIG_TABLE_CLASS = "executeConfigTable";

	private FlexTable configTable;
	private HTML htmlController, htmlStatus, htmlControllerStatus, htmlExecution, htmlRdioOnReady, htmlRdioSchedule;
	private Button btnExecute;
	private EditableText editText;
	private RadioButton rdioOnReady, rdioSchedule;
	private HorizontalPanel hPanelExecution;

	private ScheduleConfigTable scheduleConfTable;
	private ExecuteSelectionPanel selectionPanel;

	public ExecuteTab() {
		init();
	}

	private void init() {
		configTable = new FlexTable();
		configTable.addStyleName(CONFIG_TABLE_CLASS);

		htmlController = new HTML(R.get("Controller") + ":");
		htmlStatus = new HTML(R.get("Status") + ":");
		htmlControllerStatus = new HTML(R.get("controllerStatus"));
		htmlExecution = new HTML(R.get("Execution") + ":");
		htmlRdioOnReady = new HTML(R.get("execOnReady"));
		htmlRdioSchedule = new HTML(R.get("execSchedule"));

		editText = new EditableText("controllerurl");
		btnExecute = new Button(R.get("Execute"));

		rdioOnReady = new RadioButton("execution");
		rdioSchedule = new RadioButton("execution");

		hPanelExecution = new HorizontalPanel();
		hPanelExecution.add(rdioOnReady);
		hPanelExecution.add(htmlRdioOnReady);
		hPanelExecution.add(rdioSchedule);
		hPanelExecution.add(htmlRdioSchedule);

		scheduleConfTable = new ScheduleConfigTable();

		selectionPanel = new ExecuteSelectionPanel();

		// #
		scheduleConfTable.setVisible(false);

		htmlRdioOnReady.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		htmlRdioSchedule.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		rdioOnReady.setValue(true);

		rdioOnReady.addValueChangeHandler(this);
		rdioSchedule.addValueChangeHandler(this);

		// #
		configTable.setWidget(0, 0, htmlController);
		configTable.setWidget(0, 1, editText);
		configTable.setWidget(1, 0, htmlStatus);
		configTable.setWidget(1, 1, htmlControllerStatus);
		configTable.setWidget(2, 2, btnExecute);
		configTable.setWidget(2, 0, htmlExecution);
		configTable.setWidget(2, 1, hPanelExecution);

		configTable.getColumnFormatter().setWidth(0, "1px");

		configTable.getFlexCellFormatter().setColSpan(0, 1, 2);
		configTable.getFlexCellFormatter().setColSpan(1, 1, 2);
		configTable.getCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		add(configTable);
		add(scheduleConfTable);
		add(selectionPanel);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getSource() == rdioOnReady || event.getSource() == rdioSchedule) {
			scheduleConfTable.setVisible(rdioSchedule.getValue());
		}
	}
}
