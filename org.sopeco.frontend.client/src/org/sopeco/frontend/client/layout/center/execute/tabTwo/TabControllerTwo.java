package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.ExecuteTabPanel;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerTwo extends TabController {

	private static final Logger LOGGER = Logger.getLogger(TabControllerTwo.class.getName());

	private List<FrontendScheduledExperiment> scheduledExperimentList;

	private ScheduleTab view;

	/**
	 * Constructor. Invokes the {@link #initialize()} method.
	 * 
	 * @param parentController
	 */
	public TabControllerTwo(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	/**
	 * Initializes the view.
	 */
	private void initialize() {
		view = new ScheduleTab();
	}

	/**
	 * Loads all experiments from the server, which belong to this account.
	 * After loading, the view will be updated.
	 */
	public void loadScheduledExperiments() {
		RPC.getExecuteRPC().getScheduledExperiments(new AsyncCallback<List<FrontendScheduledExperiment>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(List<FrontendScheduledExperiment> result) {
				setScheduledExperiments(result);
			}
		});
	}

	/**
	 * Sets the list of the ScheduledExperiments.
	 */
	public void setScheduledExperiments(List<FrontendScheduledExperiment> result) {
		scheduledExperimentList = result;
		updateView();
	}

	/**
	 * Updates the view. Invokes the {@link #loadScheduledExperiments()} , if no
	 * experiments have been loaded yet.
	 */
	public void updateView() {
		if (scheduledExperimentList == null) {
			loadScheduledExperiments();
		} else {
			updateViewNow();
		}
	}

	/**
	 * Updates the view. The scheduled experiments must be loaded.
	 */
	private void updateViewNow() {
		double metering = Metering.start();

		// Sort and Filter ScheduledExperiment
		List<ScheduleItem> scheduledItemsList = new ArrayList<ScheduleItem>();

		String currentScenario = Manager.get().getCurrentScenarioDetails().getScenarioName();

		for (FrontendScheduledExperiment exp : scheduledExperimentList) {
			if (!exp.getScenarioDefinition().getScenarioName().equals(currentScenario)) {
				continue;
			}
			if (exp.getDurations() != null && !exp.getDurations().isEmpty()) {

			}
			ScheduleItem item = new ScheduleItem(exp);
			item.setTabControllerTwo(this);
			scheduledItemsList.add(item);
		}

		Collections.sort(scheduledItemsList);

		// Refresh the view. Add all active experiments and then the disabled.
		view.clear();
		for (ScheduleItem item : scheduledItemsList) {
			if (item.getExperiment().isEnabled()) {
				view.add(item);
			}
		}
		for (ScheduleItem item : scheduledItemsList) {
			if (!item.getExperiment().isEnabled()) {
				view.add(item);
			}
		}

		// Update TabTitle
		updateExperimentCount(scheduledItemsList.size());

		Metering.stop(metering);
	}

	/**
	 * This method will be invoked when the user enables or disables an
	 * experiment.
	 * 
	 * @param experiment
	 * @param enabled
	 *            new status
	 */
	public void setExperimentEnable(final FrontendScheduledExperiment experiment, final boolean enabled) {
		RPC.getExecuteRPC().setScheduledExperimentEnabled(experiment.getId(), enabled, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					experiment.setEnabled(enabled);
					updateView();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
				LOGGER.severe(caught.getLocalizedMessage());
			}
		});
	}

	/**
	 * Removes the experiment, which is related to the given ScheduleItem.
	 * 
	 * @param scheduleItem
	 */
	public void removeScheduledExperiment(final ScheduleItem scheduleItem) {
		Confirmation.confirm("Do you want to delete the scheduled experiment really? ", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RPC.getExecuteRPC().removeScheduledExperiment(scheduleItem.getExperiment().getId(),
						new AsyncCallback<Boolean>() {
							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									scheduledExperimentList.remove(scheduleItem.getExperiment());
									scheduleItem.removeFromParent();
								} else {
									LOGGER.severe("Can't delete experiment");
									Message.error("Can't delete experiment");
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								Message.error(caught.getMessage());
								LOGGER.severe(caught.getLocalizedMessage());
							}
						});
			}
		});
	}

	@Override
	public FlowPanel getView() {
		return view;
	}

	@Override
	public void onSelection() {
	}

	/**
	 * Updates the title of the second tab.
	 * 
	 * @param count
	 *            the number which is displayed in the title.
	 */
	public void updateExperimentCount(int count) {
		((ExecuteTabPanel) getParentController().getView()).getTabBar().setTabText(1,
				R.get("ScheduledExperiments") + " [" + count + "]");
	}
}
