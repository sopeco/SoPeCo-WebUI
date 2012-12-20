package org.sopeco.frontend.client.layout.center.execute.tabOne;

import java.util.Date;
import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerOne extends TabController implements ClickHandler {

	private static final Logger LOGGER = Logger.getLogger(TabControllerOne.class.getName());

	private ExecuteTab view;

	/**
	 * Constructor.
	 * 
	 * @param parentController
	 */
	public TabControllerOne(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	/**
	 * Initializes the view.
	 */
	private void initialize() {
		view = new ExecuteTab();
		view.getBtnExecute().addClickHandler(this);
	}

	@Override
	public FlowPanel getView() {
		return view;
	}

	@Override
	public void onSelection() {
		view.getEditController().setValue(Manager.get().getControllerUrl());
	}

	@Override
	public void onClick(ClickEvent event) {
		scheduleExperiment();
	}

	private boolean isRepeating() {
		return view.getScheduleConfTable().getCbRepeat().getValue()
				&& !view.getRepeatTable().getScheduleDays().isEmpty();
	}

	/**
	 * Returns the time, when the experiment will be executed. If it's executed
	 * immediately, this method returns the current time.
	 * 
	 * @return stating timestamp
	 */
	private long getStartTime() {
		if (view.isExecutingImmediately()) {
			return System.currentTimeMillis();
		} else {
			String[] splitTime = view.getScheduleConfTable().getEditStartTime().getValue().split(":");
			String[] splitDate = view.getScheduleConfTable().getEditStartDate().getValue().split("\\.");

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
		FrontendScheduledExperiment scheduledExperiment = new FrontendScheduledExperiment();
		scheduledExperiment.setAccount(Manager.get().getAccountDetails().getAccountName());
		scheduledExperiment.setLabel(view.getEditLabel().getValue());
		scheduledExperiment.setStartTime(getStartTime());
		scheduledExperiment.setControllerUrl(Manager.get().getControllerUrl());
		scheduledExperiment.setScenarioDefinition(ScenarioManager.get().getCurrentScenarioDefinition());

		if (isRepeating()) {
			scheduledExperiment.setRepeating(true);
			scheduledExperiment.setRepeatDays(view.getRepeatTable().getScheduleDays());
			scheduledExperiment.setRepeatHours(view.getRepeatTable().getScheduleHours());
			scheduledExperiment.setRepeatMinutes(view.getRepeatTable().getScheduleMinutes());
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

		if (view.isExecutingImmediately()) {
			LOGGER.fine("Execute NOW");
			// view.selectTab(2);
		} else {
			// view.selectTab(1);
		}

		getParentController().getTabControllerTwo().loadScheduledExperiments();
	}
}
