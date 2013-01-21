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
package org.sopeco.frontend.client.layout.center.execute.tabThree;

import java.util.Date;
import java.util.List;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.frontend.shared.entities.RunningControllerStatus;
import org.sopeco.frontend.shared.helper.MECLogEntry;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerThree extends TabController {

	private TabView tabView;
	private RunningControllerStatus controllerExperiment;
	private Timer elapsedTimeTimer;

	public TabControllerThree(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	private void initialize() {
		tabView = new TabView();
		setIdle();

		elapsedTimeTimer = new Timer() {
			@Override
			public void run() {
				updateTimes();
			}
		};
	}

	@Override
	public FlowPanel getView() {
		return tabView;
	}

	@Override
	public void onSelection() {
	}

	/**
	 * Sets the status panel idle.
	 */
	public void setIdle() {
		tabView.getStatusPanel().setStatusLabel("Idle");
		tabView.getStatusPanel().setAccount("-");
		tabView.getStatusPanel().setScenario("-");
		tabView.getStatusPanel().setTimeStart("-");
		tabView.getStatusPanel().setTimeElapsed("-");
		tabView.getStatusPanel().setTimeRemaining("-");
		tabView.getStatusPanel().getProgressBar().setValue(0);
	}

	public void setControllerQueue(List<FrontendScheduledExperiment> experiments) {
		tabView.removeAllQueueItems();
		for (FrontendScheduledExperiment fse : experiments) {
			tabView.addQueueItem(new QueueItem(fse));
		}
	}

	public void setCurrentControllerExperiment(RunningControllerStatus experiment) {
		controllerExperiment = experiment;

		// tabView.getStatusPanel().setStatusLabel(experiment.getLabel() + " - "
		// + experiment.getStatusString());
		tabView.getStatusPanel().setAccount(experiment.getAccount());
		tabView.getStatusPanel().setScenario(experiment.getScenario());
		// TODO
		tabView.getStatusPanel().setExperiments("n/a");

		DateTimeFormat dft = DateTimeFormat.getFormat("HH:mm:ss");

		tabView.getStatusPanel().clearLog();
		tabView.getStatusPanel().addLogText(new HTML("<b>Executing '" + experiment.getLabel() + "'</b>"));

		for (MECLogEntry log : experiment.getEventLogList()) {
			HTML html = new HTML(dft.format(new Date(log.getTime())) + ": " + log.getMessage());
			if (log.isError()) {
				html.addStyleName("errorMessage");
				html.setHTML("<b>" + html.getHTML() + "</b><br>" + log.getErrorMessage());
			}
			tabView.getStatusPanel().addLogText(html);
		}

		if (experiment.getEventLogList().size() == 1) {
			// Start
			elapsedTimeTimer.scheduleRepeating(1000);
			tabView.getStatusPanel().getProgressBar().setValue(0, false);
		}

		// tabView.getStatusPanel().setTimeRemaining("n/a");
		// tabView.getStatusPanel().getProgressBar().setValue(0);

		DateTimeFormat dtf = DateTimeFormat.getFormat("hh:mm aa / dd.MM.yyyy");

		tabView.getStatusPanel().setTimeStart(dtf.format(new Date(experiment.getTimeStart())));
		updateTimes();

		if (experiment.isFinished()) {
			elapsedTimeTimer.cancel();
			tabView.getStatusPanel().getProgressBar().setValue(100);
			tabView.getStatusPanel().setTimeRemaining("-");
		}
		// else if (experiment.getStatus() == EStatus.START_MEASUREMENT) {
		// elapsedTimeTimer.scheduleRepeating(1000);
		// tabView.getStatusPanel().getProgressBar().setValue(0, false);
		// }
	}

	// TODO
	private void updateTimes() {
		String elapsed = "";
		String remaining = "";

		NumberFormat fmt = NumberFormat.getFormat("00");

		long durationElapsed = (System.currentTimeMillis() - controllerExperiment.getTimeStart()) / 1000;
		long durationRemaining = (controllerExperiment.getTimeRemaining() / 1000) - durationElapsed;

		int hoursElapsed = (int) (durationElapsed / 3600);
		durationElapsed = durationElapsed % 3600;
		int minElapsed = (int) (durationElapsed / 60);
		int secElapsed = (int) (durationElapsed % 60);
		elapsed += fmt.format(hoursElapsed) + ":" + fmt.format(minElapsed) + ":" + fmt.format(secElapsed);

		if (durationRemaining > 0) {
			int hoursRemaining = (int) (durationRemaining / 3600);
			durationRemaining = durationRemaining % 3600;
			int minRemaining = (int) (durationRemaining / 60);
			int secRemaining = (int) (durationRemaining % 60);
			remaining += fmt.format(hoursRemaining) + ":" + fmt.format(minRemaining) + ":" + fmt.format(secRemaining);
		} else {
			remaining = "n/a";
		}

		if (controllerExperiment.getProgress() != -1) {
			tabView.getStatusPanel().getProgressBar().setValue(controllerExperiment.getProgress());
		} else if (controllerExperiment.getProgress() == -1 && controllerExperiment.getTimeRemaining() > 0) {
			float progress = 100 / (controllerExperiment.getTimeRemaining() / 1000)
					* ((controllerExperiment.getTimeRemaining() / 1000) - durationRemaining);
			progress = Math.min(progress, 99);
			tabView.getStatusPanel().getProgressBar().setValue(progress);
		}

		tabView.getStatusPanel().setTimeElapsed(elapsed);
		tabView.getStatusPanel().setTimeRemaining(remaining);
	}
}
