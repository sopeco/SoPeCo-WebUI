package org.sopeco.frontend.client.layout.center.execute;

import java.util.Date;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.RawScheduledExperiment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController implements ICenterController, ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(ExecuteController.class.getName());

	private ExecuteTabPanelOld view;

	public ExecuteController() {
		FrontEndResources.loadExecuteViewCSS();
		init();
	}

	private void init() {
		view = new ExecuteTabPanelOld();
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

	@Override
	public void reset() {
	}

	@Override
	public void onClick(ClickEvent event) {
		scheduleExperiment();
	}

	public void refreshScheduleTab() {
		view.getTabSchedule().refreshList();
//		int scheduleCount = Manager.get().getCurrentScenarioDetails().getScheduledExperimentsList().size();
//		view.getTabBar().setTabText(1, R.get("ScheduledExperiments") + " [" + scheduleCount + "]");
	}

	private boolean isRepeating() {
		return view.getTabExecute().getScheduleConfTable().getCbRepeat().getValue()
				&& !view.getTabExecute().getRepeatTable().getScheduleDays().isEmpty();
	}

	/**
	 * Returns the time, when the experiment will be executed. If it's executed
	 * immediately, this method returns the current time.
	 * 
	 * @return stating timestamp
	 */
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

	private void scheduleExperiment() {
		RawScheduledExperiment scheduledExperiment = new RawScheduledExperiment();
		scheduledExperiment.setAccount(Manager.get().getAccountDetails().getAccountName());
		scheduledExperiment.setLabel(view.getTabExecute().getEditLabel().getValue());
		scheduledExperiment.setStartTime(getStartTime());
		scheduledExperiment.setControllerUrl(Manager.get().getControllerUrl());
		scheduledExperiment.setScenarioDefinition(ScenarioManager.get().getCurrentScenarioDefinition());

		if (isRepeating()) {
			scheduledExperiment.setRepeating(true);
			scheduledExperiment.setRepeatDays(view.getTabExecute().getRepeatTable().getScheduleDays());
			scheduledExperiment.setRepeatHours(view.getTabExecute().getRepeatTable().getScheduleHours());
			scheduledExperiment.setRepeatMinutes(view.getTabExecute().getRepeatTable().getScheduleMinutes());
		} else {
			scheduledExperiment.setRepeating(false);
		}

		RPC.getExecuteRPC().scheduleExperiment(scheduledExperiment, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}
		});

		if (view.getTabExecute().isExecutingImmediately()) {
			LOGGER.fine("Execute NOW");
			// view.selectTab(2);
		} else {
			// view.selectTab(1);
		}

		refreshScheduleTab();
	}
}
