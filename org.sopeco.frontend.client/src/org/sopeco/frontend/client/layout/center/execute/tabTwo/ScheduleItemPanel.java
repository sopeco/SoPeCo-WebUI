package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.Date;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.shared.entities.RawScheduledExperiment;
import org.sopeco.gwt.widgets.ClearDiv;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduleItemPanel extends FlowPanel {

	private RawScheduledExperiment experiment;
	private HTML htmlLabel;
	private Image imgPause;
	private Image imgRemove;
	private FlowPanel headPanel;
	private FlexTable contentTable;
	private HTML htmlLabelExecute;
	private HTML htmlLabelStartTime;
	private HTML htmlLabelRepeat;
	private HTML htmlLabelNextExec;
	private HTML htmlLabelLastExec;
	private HTML htmlLabelAdded;
	private HTML htmlNextExec;
	private HTML htmlLastExec;
	private HTML htmlAdded;
	private HTML htmlRepeat;
	private HTML htmlStartTime;
	private Anchor anchorPerformNow;

	public ScheduleItemPanel(RawScheduledExperiment pExperiment) {
		experiment = pExperiment;
		init();
	}

	private void init() {
		DateTimeFormat dtf = DateTimeFormat.getFormat("hh:mm aa / dd.MM.yyyy");

		headPanel = new FlowPanel();

		contentTable = new FlexTable();

		htmlLabel = new HTML(R.get("Label"));
		imgPause = new Image("images/control_pause.png");
		imgRemove = new Image("images/trash.png");

		htmlLabelExecute = new HTML(R.get("Experiments") + ":");
		htmlLabelStartTime = new HTML(R.get("StartTime") + ":");
		htmlLabelRepeat = new HTML(R.get("Repeat") + ":");
		htmlLabelAdded = new HTML(R.get("Added") + ":");
		htmlLabelLastExec = new HTML(R.get("LastExecution") + ":");
		htmlLabelNextExec = new HTML(R.get("NextExecution") + ":");

		anchorPerformNow = new Anchor(R.get("PerfomrNow"));

		htmlStartTime = new HTML(dtf.format(new Date(experiment.getStartTime())));
		htmlRepeat = new HTML("");
		htmlAdded = new HTML(dtf.format(new Date(experiment.getLabel())));
		htmlLastExec = new HTML("");
		htmlNextExec = new HTML(dtf.format(new Date(experiment.getLabel())));

		addObjects();
		style();
	}

	/**
	 * 
	 */
	private void style() {
		addStyleName("scheduleItemPanel");
		headPanel.addStyleName("headPanel");
	}

	/**
	 * 
	 */
	private void addObjects() {
		headPanel.add(htmlLabel);
		headPanel.add(imgPause);
		headPanel.add(imgRemove);
		headPanel.add(anchorPerformNow);
		headPanel.add(new ClearDiv());

		contentTable.setWidget(0, 0, htmlLabelExecute);

		contentTable.setWidget(0, 2, htmlLabelStartTime);
		contentTable.setWidget(1, 2, htmlLabelAdded);
		contentTable.setWidget(2, 2, htmlLabelRepeat);

		contentTable.setWidget(0, 3, htmlStartTime);
		contentTable.setWidget(1, 3, htmlAdded);
		contentTable.setWidget(2, 3, htmlRepeat);

		contentTable.setWidget(0, 4, htmlLabelLastExec);
		contentTable.setWidget(1, 4, htmlLabelNextExec);

		contentTable.setWidget(0, 5, htmlLastExec);
		contentTable.setWidget(1, 5, htmlNextExec);

		add(headPanel);
		add(contentTable);
	}

	public RawScheduledExperiment getExperiment() {
		return experiment;
	}

}
