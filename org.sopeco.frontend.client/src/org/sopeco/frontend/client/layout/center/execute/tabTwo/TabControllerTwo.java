/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.ExecuteTabPanel;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.frontend.shared.push.PushListPackage;
import org.sopeco.frontend.shared.push.PushSerializable;

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
			// FrontendScheduledExperiment exp = (FrontendScheduledExperiment)
			// object;
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
