package org.sopeco.frontend.client.layout.center.execute.tabOne;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
@SuppressWarnings("rawtypes")
public class RepeatPanel extends SimplePanel implements ValueChangeHandler {
	private static final String CSS_CLASS = "repeatPanel";
	private static final String REPEAT_CLASS = "repeatTable";

	private static final String SCHEDULE_REGEX = "((\\*)|(\\d+)|(\\d+/\\d+)|(\\d+\\-\\d+))(,((\\*)|(\\d+)|(\\d+/\\d+)|(\\d+\\-\\d+)))*";

	private FlexTable table;
	private HTML htmlWeekdays, htmlTimes;
	private FlowPanel panelWeekdays;
	private EditableText editHours, editMinutes;
	private HorizontalPanel hPanelTimes;
	private ExecuteTab parent;

	private List<CheckBoxWidget> checkboxWidgetList;

	public RepeatPanel(ExecuteTab pParent) {
		parent = pParent;
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		addStyleName(CSS_CLASS);

		checkboxWidgetList = new ArrayList<CheckBoxWidget>();

		table = new FlexTable();
		table.addStyleName(REPEAT_CLASS);

		htmlTimes = new HTML("Hours / Minutes:");
		htmlWeekdays = new HTML(R.get("Weekdays") + ":");
		editHours = new EditableText("0");
		editMinutes = new EditableText("0");

		editHours.setValidPattern(SCHEDULE_REGEX);
		editMinutes.setValidPattern(SCHEDULE_REGEX);

		hPanelTimes = new HorizontalPanel();
		panelWeekdays = new FlowPanel();

		editHours.getElement().getStyle().setMarginRight(1, Unit.EM);

		hPanelTimes.add(editHours);
		hPanelTimes.add(editMinutes);

		String[] days = new String[] { R.get("Sunday"), R.get("Monday"), R.get("Tuesday"), R.get("Wednesday"),
				R.get("Thursday"), R.get("Friday"), R.get("Saturday") };

		int c = 1;
		for (String d : days) {
			CheckBoxWidget cbWidget = new CheckBoxWidget(d, "" + c++);
			panelWeekdays.add(cbWidget);
			checkboxWidgetList.add(cbWidget);
			cbWidget.getCheckBox().addValueChangeHandler(this);
		}

		editHours.addValueChangeHandler(this);
		editMinutes.addValueChangeHandler(this);

		hPanelTimes.setCellWidth(editHours, "130px");
		hPanelTimes.setCellWidth(editMinutes, "130px");

		table.setWidget(0, 0, htmlWeekdays);
		table.setWidget(0, 1, panelWeekdays);
		table.setWidget(1, 0, htmlTimes);
		table.setWidget(1, 1, hPanelTimes);

		table.getCellFormatter().setWidth(0, 0, "130px");

		add(table);
	}

	public String getScheduleHours() {
		return editHours.getValue();
	}

	public String getScheduleMinutes() {
		return editMinutes.getValue();
	}

	public String getScheduleDays() {
		String days = "";
		for (CheckBoxWidget cbw : checkboxWidgetList) {
			if (cbw.isChecked()) {
				if (!days.isEmpty()) {
					days += ",";
				}
				days += cbw.getValue();
			}
		}
		return days;
	}

	@Override
	public void onValueChange(ValueChangeEvent event) {
	}
}

/**
 * 
 *
 */
class CheckBoxWidget extends HorizontalPanel implements ValueChangeHandler<Boolean> {

	private static final String NOT_CHECKED = "notChecked";
	private CheckBox checkBox;
	private HTML htmlText;
	private String value;

	public CheckBoxWidget(String text, String pValue) {
		addStyleName("checkBoxWidget");
		addStyleName(NOT_CHECKED);

		value = pValue;

		checkBox = new CheckBox();
		htmlText = new HTML(text);

		checkBox.addValueChangeHandler(this);

		add(checkBox);
		add(htmlText);
	}

	public boolean isChecked() {
		return checkBox.getValue();
	}

	public String getValue() {
		return value;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public HTML getHtmlText() {
		return htmlText;
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getValue()) {
			removeStyleName(NOT_CHECKED);
		} else {
			addStyleName(NOT_CHECKED);
		}
	}
}
