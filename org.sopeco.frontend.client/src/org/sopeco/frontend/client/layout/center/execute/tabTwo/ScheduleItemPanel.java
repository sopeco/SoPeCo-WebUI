package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.Date;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.gwt.widgets.ClearDiv;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class ScheduleItemPanel extends FlowPanel implements ClickHandler, AsyncCallback<Boolean> {

	private static final Logger LOGGER = Logger.getLogger(ScheduleItemPanel.class.getName());

	private FrontendScheduledExperiment experiment;
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

	private static final String[] DAY_STRINGS = new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	public ScheduleItemPanel(FrontendScheduledExperiment pExperiment) {
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
		imgRemove.addClickHandler(this);

		htmlLabelExecute = new HTML(R.get("Experiments") + ":");
		htmlLabelStartTime = new HTML(R.get("StartTime") + ":");
		htmlLabelRepeat = new HTML(R.get("Repeat") + ":");
		htmlLabelAdded = new HTML(R.get("Added") + ":");
		htmlLabelLastExec = new HTML(R.get("LastExecution") + ":");
		htmlLabelNextExec = new HTML(R.get("NextExecution") + ":");

		anchorPerformNow = new Anchor(R.get("PerfomrNow"));

		htmlStartTime = new HTML(dtf.format(new Date(experiment.getStartTime())));
		htmlAdded = new HTML(dtf.format(new Date(experiment.getAddTime())));
		htmlNextExec = new HTML(dtf.format(new Date(experiment.getNextExecutionTime())));

		String lastExec = "";
		if (experiment.getLastExecutionTime() == -1) {
			lastExec = R.get("NeverExecuted");
		} else {
			lastExec = dtf.format(new Date(experiment.getLastExecutionTime()));
		}
		htmlLastExec = new HTML(lastExec);

		String repeatText = "";
		if (experiment.isRepeating()) {
			repeatText = getDay(experiment.getRepeatDays()) + " " + experiment.getRepeatHours() + " "
					+ experiment.getRepeatMinutes();
		} else {
			repeatText = R.get("UniqueExecution");
		}
		htmlRepeat = new HTML(repeatText);

		addObjects();
		style();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == imgRemove) {
			Confirmation.confirm("Do you want to delete the scheduled experiment really? ", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					RPC.getExecuteRPC().removeScheduledExperiment(experiment.getId(), ScheduleItemPanel.this);
				}
			});
		} else if (event.getSource() == imgPause) {
			RPC.getExecuteRPC().setScheduledExperimentEnabled(experiment.getId(), false, new AsyncCallback<Boolean>() {
				@Override
				public void onSuccess(Boolean result) {
				}

				@Override
				public void onFailure(Throwable caught) {
				}
			});
		}
	}

	@Override
	public void onSuccess(Boolean result) {
		if (result) {
			removeFromParent();
		} else {
			LOGGER.severe("Can't delete experiment");
			Message.error("Can't delete experiment");
		}
	}

	@Override
	public void onFailure(Throwable caught) {
		LOGGER.severe(caught.getLocalizedMessage());
		Message.error(caught.getMessage());
	}

	public String getDay(String days) {
		String ret = "";
		for (String s : days.split(",")) {
			int i = Integer.parseInt(s.trim()) - 1;
			if (!ret.isEmpty()) {
				ret += ",";
			}
			ret += DAY_STRINGS[i];
		}
		return ret;
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

	public FrontendScheduledExperiment getExperiment() {
		return experiment;
	}

}
