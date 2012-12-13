package org.sopeco.frontend.client.layout.center.execute.tabOne;

import java.util.Date;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.shared.helper.ScheduleExpression;
import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.dom.client.Style.Overflow;
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
	private HTML htmlController, htmlLabel, htmlExecution, htmlRdioOnReady, htmlRdioSchedule;
	private Button btnExecute;
	private EditableText editController, editLabel;
	private RadioButton rdioOnReady, rdioSchedule;
	private HorizontalPanel hPanelExecution;

	private ScheduleConfigTable scheduleConfTable;
	private RepeatPanel repeatTable;
	private SelectionPanel selectionPanel;

	public ExecuteTab() {
		init();
	}

	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);

		configTable = new FlexTable();
		configTable.addStyleName(CONFIG_TABLE_CLASS);

		htmlController = new HTML(R.get("Controller") + ":");
		htmlLabel = new HTML(R.get("Label") + ":");
		htmlExecution = new HTML(R.get("Execution") + ":");
		htmlRdioOnReady = new HTML(R.get("execOnReady"));
		htmlRdioSchedule = new HTML(R.get("execSchedule"));

		editLabel = new EditableText("My Scheduling");
		editController = new EditableText(Manager.get().getControllerUrl());
		editController.setEditable(false);
		btnExecute = new Button(R.get("StoreExperiment"));

		rdioOnReady = new RadioButton("execution");
		rdioSchedule = new RadioButton("execution");
		rdioSchedule.setEnabled(false);

		hPanelExecution = new HorizontalPanel();
		hPanelExecution.add(rdioOnReady);
		hPanelExecution.add(htmlRdioOnReady);
		hPanelExecution.add(rdioSchedule);
		hPanelExecution.add(htmlRdioSchedule);

		scheduleConfTable = new ScheduleConfigTable(this);
		repeatTable = new RepeatPanel(this);
		selectionPanel = new SelectionPanel();

		// #
		scheduleConfTable.setVisible(false);
		repeatTable.setVisible(false);

		scheduleConfTable.getCbRepeat().addValueChangeHandler(this);

		htmlRdioOnReady.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		htmlRdioSchedule.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		rdioOnReady.setValue(true);

		rdioOnReady.addValueChangeHandler(this);
		rdioSchedule.addValueChangeHandler(this);

		// #
		configTable.setWidget(0, 0, htmlLabel);
		configTable.setWidget(0, 1, editLabel);
		configTable.setWidget(1, 0, htmlController);
		configTable.setWidget(1, 1, editController);
		configTable.setWidget(2, 2, btnExecute);
		configTable.setWidget(2, 0, htmlExecution);
		configTable.setWidget(2, 1, hPanelExecution);

		configTable.getColumnFormatter().setWidth(0, "130px");

		configTable.getFlexCellFormatter().setColSpan(0, 1, 2);
		configTable.getFlexCellFormatter().setColSpan(1, 1, 2);
		configTable.getCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		add(new Headline("Execution Settings"));
		add(configTable);
		add(scheduleConfTable);
		add(repeatTable);
		add(selectionPanel);
	}

	public void updateNextRepeat() {
		if (repeatTable.getScheduleDays().isEmpty()) {
			scheduleConfTable.setFirstRepeatText("-");
			return;
		}
		long nextRepeat = ScheduleExpression.nextValidDate(repeatTable.getScheduleDays(),
				repeatTable.getScheduleHours(), repeatTable.getScheduleMinutes());

		scheduleConfTable.setFirstRepeatText(new Date(nextRepeat).toString());
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getSource() == rdioOnReady || event.getSource() == rdioSchedule) {
			scheduleConfTable.setVisible(rdioSchedule.getValue());
			repeatTable.setVisible(rdioSchedule.getValue() && scheduleConfTable.getCbRepeat().getValue());
		} else if (event.getSource() == scheduleConfTable.getCbRepeat()) {
			repeatTable.setVisible(scheduleConfTable.getCbRepeat().getValue());
			scheduleConfTable.getHtmlFirstRepeat().setVisible(scheduleConfTable.getCbRepeat().getValue());
		}
	}

	public boolean isExecutingImmediately() {
		return rdioOnReady.getValue();
	}

	public void generateTree() {
		selectionPanel.generateTree();
	}

	public ScheduleConfigTable getScheduleConfTable() {
		return scheduleConfTable;
	}

	public RepeatPanel getRepeatTable() {
		return repeatTable;
	}

	public SelectionPanel getSelectionPanel() {
		return selectionPanel;
	}

	public Button getBtnExecute() {
		return btnExecute;
	}

	public EditableText getEditController() {
		return editController;
	}

	public EditableText getEditLabel() {
		return editLabel;
	}

}
