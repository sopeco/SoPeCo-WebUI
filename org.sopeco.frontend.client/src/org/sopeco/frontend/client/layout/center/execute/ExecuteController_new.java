package org.sopeco.frontend.client.layout.center.execute;

import java.util.Date;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.helper.ScheduleExpression;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController_new implements ICenterController, ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(ExecuteController_new.class.getName());

	private ExecuteTabPanel view;

	// private boolean running = false;

	public ExecuteController_new() {
		FrontEndResources.loadExecuteViewCSS();
		init();
	}

	private void init() {
		view = new ExecuteTabPanel();

		view.getTabExecute().getBtnExecute().addClickHandler(this);
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void onSwitchTo() {
		view.getTabExecute().generateTree();
		refreshScheduleTab();
	}

	public void refreshScheduleTab() {
		view.getTabSchedule().refreshList();
		int scheduleCount = Manager.get().getCurrentScenarioDetails().getScheduledExperimentsList().size();
		view.getTabBar().setTabText(1, R.get("ScheduledExperiments") + " [" + scheduleCount + "]");
	}

	private boolean isRepeating() {
		return view.getTabExecute().getScheduleConfTable().getCbRepeat().getValue()
				&& !view.getTabExecute().getRepeatTable().getScheduleDays().isEmpty();
	}

	private long getStartTime() {
		if (view.getTabExecute().isExecutingImmediately()) {
			return System.currentTimeMillis();
		} else {
			String[] splitTime = view.getTabExecute().getScheduleConfTable().getEditStartTime().getValue().split(":");
			String[] splitDate = view.getTabExecute().getScheduleConfTable().getEditStartDate().getValue().split("\\.");

			Date date = new Date();
			date.setHours(Integer.parseInt(splitTime[0]));
			date.setMinutes(Integer.parseInt(splitTime[1]));
			date.setSeconds(0);
			date.setDate(Integer.parseInt(splitDate[0]));
			date.setMonth(Integer.parseInt(splitDate[1]) - 1);
			date.setYear(Integer.parseInt(splitDate[2]) - 1900);

			return date.getTime();
		}
	}

	@Override
	public void reset() {
	}

	@Override
	public void onClick(ClickEvent event) {
		scheduleExperiment();
	}

	private void scheduleExperiment() {
		ScheduledExperiment scheduledExperiment = new ScheduledExperiment();
		scheduledExperiment.setAddedTime(System.currentTimeMillis());
		scheduledExperiment.setLabel(view.getTabExecute().getEditLabel().getValue());
		scheduledExperiment.setStartTime(getStartTime());
		scheduledExperiment.setControllerUrl(Manager.get().getControllerUrl());
		scheduledExperiment.setScenarioDefinition(null);

		if (isRepeating()) {
			scheduledExperiment.setRepeating(true);
			scheduledExperiment.setRepeatDays(view.getTabExecute().getRepeatTable().getScheduleDays());
			scheduledExperiment.setRepeatHours(view.getTabExecute().getRepeatTable().getScheduleHours());
			scheduledExperiment.setRepeatMinutes(view.getTabExecute().getRepeatTable().getScheduleMinutes());
		} else {
			scheduledExperiment.setRepeating(false);
		}

		if (view.getTabExecute().isExecutingImmediately()) {
			LOGGER.fine("Execute NOW");
			view.selectTab(2);
		} else {
			if (scheduledExperiment.getStartTime() > System.currentTimeMillis()) {
				scheduledExperiment.setNextExecutionTime(scheduledExperiment.getStartTime());
			} else {
				long nextRepetition = ScheduleExpression.nextValidDate(scheduledExperiment.getStartTime(),
						scheduledExperiment.getRepeatDays(), scheduledExperiment.getRepeatHours(),
						scheduledExperiment.getRepeatMinutes());
				scheduledExperiment.setNextExecutionTime(nextRepetition);
			}

			Manager.get().getCurrentScenarioDetails().getScheduledExperimentsList().add(scheduledExperiment);
			Manager.get().storeAccountDetails();
			view.selectTab(1);
		}

		refreshScheduleTab();
	}
	// private void addHandler() {
	// view.getBtnStartExperiment().addClickHandler(this);
	// }
	//
	// @Override
	// public void onClick(ClickEvent event) {

	// if (event.getSource() == view.getBtnStartExperiment()) {
	// String url = Manager.get().getControllerUrl();
	//
	// if (running) {
	// // RPC.getExecuteRPC().stop(url, new AsyncCallback<Void>() {
	// // @Override
	// // public void onSuccess(Void result) {
	// // // TODO Auto-generated method stub
	// //
	// // }
	// //
	// // @Override
	// // public void onFailure(Throwable caught) {
	// // Message.error(caught.getMessage());
	// // }
	// // });
	// } else {
	// RPC.getExecuteRPC().execute(url, new AsyncCallback<Void>() {
	//
	// @Override
	// public void onSuccess(Void result) {
	// view.getBtnStartExperiment().setText(R.get("stop"));
	// view.getBtnStartExperiment().setEnabled(false);
	// view.getHtmlStatus().setText("Status: running");
	//
	// running = true;
	//
	// pollingCheckStatus();
	// }

	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// }
	// });
	// }
	// }
	// }

	// private void pollingCheckStatus() {
	// String url = Manager.get().getControllerUrl();
	//
	// if (url.isEmpty()) {
	// return;
	// }
	//
	// RPC.getExecuteRPC().isRunning(url, new AsyncCallback<Long>() {
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// }
	//
	// @Override
	// public void onSuccess(Long result) {
	// if (result != -1) {
	// long temp = (System.currentTimeMillis() - result) / 1000;
	//
	// String since = "[for ";
	// if (temp > 60) {
	// temp /= 60;
	// if (temp > 60) {
	// since += (temp / 60) + "h " + (temp % 60) + " min]";
	// } else {
	// since += temp + " minutes]";
	// }
	// } else {
	// since += (temp) + " seconds]";
	// }
	//
	// view.getBtnStartExperiment().setText(R.get("stop"));
	// view.getBtnStartExperiment().setEnabled(false);
	// view.getHtmlStatus().setText("Status: running " + since);
	//
	// running = true;
	//
	// Timer t = new Timer() {
	// @Override
	// public void run() {
	// // if (repeate) {
	// pollingCheckStatus();
	// // }
	// }
	// };
	//
	// t.schedule(2500);
	// } else {
	// view.getBtnStartExperiment().setText(R.get("startExperiment"));
	// view.getHtmlStatus().setText("Status: -");
	// view.getBtnStartExperiment().setEnabled(true);
	//
	// running = false;
	// }
	// }
	// });
	// }
}
