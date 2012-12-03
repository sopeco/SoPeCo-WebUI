package org.sopeco.frontend.client.layout.center.execute.tabOne;

import java.util.Date;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduleConfigTable extends FlowPanel implements FocusHandler, ValueChangeHandler<Date> {
	private static final String CONFIG_TABLE_CLASS = "scheduleConfigTable";

	private static final String TIME_PATTERN = "((\\d)|((0|1)\\d)|(2[0-3])):((\\d)|([0-5]\\d))";

	private HTML htmlStartTime, htmlRepeat, htmlFirstRepeat;
	private EditableText editStartTime, editStartDate;
	private CheckBox cbRepeat;
	private FlexTable table;
	private DatePicker datePicker = new DatePicker();
	private PopupPanel popDatePicker;
	private HorizontalPanel panelRepeat;
	private ExecuteTab parent;

	public ScheduleConfigTable(ExecuteTab pParent) {
		parent = pParent;
		init();
	}

	private void init() {
		addStyleName(CONFIG_TABLE_CLASS);

		htmlStartTime = new HTML("Start Time/Date:");
		htmlRepeat = new HTML(R.get("Repeat") + ":");
		htmlFirstRepeat = new HTML();
		setFirstRepeatText("-");

		Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60);
		editStartTime = new EditableText(DateTimeFormat.getFormat("H").format(date) + ":00");
		editStartDate = new EditableText(DateTimeFormat.getFormat("dd.MM.yyyy").format(date));

		cbRepeat = new CheckBox();

		datePicker = new DatePicker();
		popDatePicker = new PopupPanel(true);

		panelRepeat = new HorizontalPanel();

		table = new FlexTable();

		htmlFirstRepeat.setVisible(false);

		panelRepeat.addStyleName("tableRepeat");
		panelRepeat.add(cbRepeat);
		panelRepeat.add(htmlFirstRepeat);

		datePicker.addValueChangeHandler(this);
		popDatePicker.add(datePicker);

		editStartTime.setValidPattern(TIME_PATTERN);
		editStartTime.addStyleName("textCenter");
		editStartDate.addStyleName("textCenter");
		editStartDate.addFocusHandler(this);
		editStartDate.setEditable(false);

		table.setWidget(0, 0, htmlStartTime);
		table.setWidget(0, 1, editStartTime);
		table.setWidget(0, 2, editStartDate);
		table.setWidget(1, 0, htmlRepeat);
		table.setWidget(1, 1, panelRepeat);

		table.getFlexCellFormatter().setColSpan(1, 1, 3);
		table.getCellFormatter().setWidth(0, 0, "130px");
		table.getCellFormatter().setWidth(0, 1, "75px");
		table.getCellFormatter().setWidth(0, 2, "100px");

		add(table);

	}

	@Override
	public void onFocus(FocusEvent event) {
		if (event.getSource() == editStartDate) {
			int left = editStartDate.getAbsoluteLeft();
			int top = editStartDate.getAbsoluteTop() + editStartDate.getOffsetHeight();

			popDatePicker.show();
			popDatePicker.setPopupPosition(left, top);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Date> event) {
		DateTimeFormat format = DateTimeFormat.getFormat("dd.MM.y");
		editStartDate.setValue(format.format(event.getValue()));
		popDatePicker.hide();
	}

	public CheckBox getCbRepeat() {
		return cbRepeat;
	}

	public HTML getHtmlFirstRepeat() {
		return htmlFirstRepeat;
	}

	public void setFirstRepeatText(String text) {
		htmlFirstRepeat.setText(R.get("FirstRepeatAfterStart") + ": " + text);
	}

	public EditableText getEditStartTime() {
		return editStartTime;
	}

	public EditableText getEditStartDate() {
		return editStartDate;
	}
	
	
}
