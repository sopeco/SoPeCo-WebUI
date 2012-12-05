package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.Date;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.shared.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.helper.ScheduleExpression;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduledItem extends FlexTable implements ClickHandler {

	private static final String IMG_REMOVE = "images/trash_white.png";
	private static final String[] DAYS = new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
	private static final String ITEM_CSS = "scheduleItem";
	private static final String IMG_REPEAT = "images/repeat.png";

	private HTML htmlLabel, htmlSpecification, htmlExperiment, htmlStart, htmlRepeat, htmlAdded, htmlNextExecution;
	private ScheduledExperiment experiment;
	private Image imgRepeat, imgRemove;

	private ScheduleTab parent;

	public ScheduledItem(ScheduledExperiment pExperiment) {
		experiment = pExperiment;
		init();
	}

	private void init() {
		addStyleName(ITEM_CSS);

		DateTimeFormat format = DateTimeFormat.getFormat("hh:mm aa - dd.MM.yyyy");

		String repeatString;
		if (experiment.isRepeating()) {
			repeatString = makeDayString() + " ";
			repeatString += experiment.getRepeatHours() + " ";
			repeatString += experiment.getRepeatMinutes() + " ";
		} else {
			repeatString = "once";
		}

		String startTime = format.format(new Date(experiment.getStartTime()));
		if (experiment.isRepeating() && experiment.getStartTime() < System.currentTimeMillis()) {
			htmlStart = new HTML("repeating");
			htmlStart.setTitle("StartTime: " + startTime);
		} else {
			htmlStart = new HTML(startTime);
		}

		htmlLabel = new HTML(experiment.getLabel());
		htmlSpecification = new HTML("mySpecification");
		htmlExperiment = new HTML("myExperimentSeries");
		htmlRepeat = new HTML(repeatString);
		htmlAdded = new HTML(format.format(new Date(experiment.getAddedTime())));
		htmlNextExecution = new HTML(format.format(new Date(experiment.getNextExecutionTime())));

		imgRemove = new Image(IMG_REMOVE);
		imgRemove.getElement().getStyle().setCursor(Cursor.POINTER);
		imgRemove.addClickHandler(this);

		if (experiment.isRepeating()) {
			imgRepeat = new Image(IMG_REPEAT);
			imgRepeat.setSize("21px", "14px");
			imgRepeat.getElement().getStyle().setVerticalAlign(VerticalAlign.SUB);
			setWidget(0, 1, imgRepeat);
		}

		setWidget(0, 0, htmlLabel);
		setWidget(0, 2, imgRemove);

		setWidget(1, 0, new HTML(R.get("Specification") + ":"));
		setWidget(2, 0, new HTML(R.get("ExperimentSeries") + ":"));

		setWidget(1, 1, htmlSpecification);
		setWidget(2, 1, htmlExperiment);

		setWidget(1, 2, new HTML(R.get("Start") + ":"));
		setWidget(2, 2, new HTML(R.get("Repeat") + ":"));

		setWidget(1, 3, htmlStart);
		setWidget(2, 3, htmlRepeat);

		setWidget(1, 4, new HTML(R.get("Added") + ":"));
		setWidget(2, 4, new HTML(R.get("NextExecution") + ":"));

		setWidget(1, 5, htmlAdded);
		setWidget(2, 5, htmlNextExecution);

		String width = "1px";
		getColumnFormatter().setWidth(0, width);
		getColumnFormatter().setWidth(2, width);
		getColumnFormatter().setWidth(4, width);

		width = "33%";
		getColumnFormatter().setWidth(1, width);
		getColumnFormatter().setWidth(3, width);
		getColumnFormatter().setWidth(5, width);

		// getFlexCellFormatter().setColSpan(2, 3, 3);

		getFlexCellFormatter().setColSpan(0, 2, 4);
		getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	private String makeDayString() {
		String ret = "";
		for (String val : experiment.getRepeatDays().split(",")) {
			if (!ret.isEmpty()) {
				ret += ",";
			}
			ret += DAYS[Integer.parseInt(val) - 1];
		}
		return ret;
	}

	public Image getImgRemove() {
		return imgRemove;
	}

	public ScheduledExperiment getExperiment() {
		return experiment;
	}

	public void setParent(ScheduleTab pParent) {
		parent = pParent;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (parent != null) {
			//parent.removeExperiment(this);
		}
	}
}
