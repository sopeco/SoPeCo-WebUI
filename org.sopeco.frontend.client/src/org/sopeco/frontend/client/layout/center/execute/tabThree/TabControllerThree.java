package org.sopeco.frontend.client.layout.center.execute.tabThree;

import java.util.Date;
import java.util.List;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.shared.entities.CurrentControllerExperiment;
import org.sopeco.frontend.shared.entities.CurrentControllerExperiment.EStatus;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerThree extends TabController {

	private TabView tabView;
	private CurrentControllerExperiment controllerExperiment;
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

	public void setCurrentControllerExperiment(CurrentControllerExperiment experiment) {
		controllerExperiment = experiment;

		tabView.getStatusPanel().setStatusLabel(experiment.getLabel() + " - " + experiment.getStatusString());
		tabView.getStatusPanel().setAccount(experiment.getAccount());
		tabView.getStatusPanel().setScenario(experiment.getScenario());
		//TODO
		tabView.getStatusPanel().setExperiments("n/a");
		
		// tabView.getStatusPanel().setTimeRemaining("n/a");
		// tabView.getStatusPanel().getProgressBar().setValue(0);

		DateTimeFormat dtf = DateTimeFormat.getFormat("hh:mm aa / dd.MM.yyyy");

		tabView.getStatusPanel().setTimeStart(dtf.format(new Date(experiment.getTimeStart())));
		updateTimes();

		if (experiment.getStatus() == EStatus.MEASUREMENT_FINISHED) {
			elapsedTimeTimer.cancel();
			tabView.getStatusPanel().getProgressBar().setValue(100);
			tabView.getStatusPanel().setTimeRemaining("-");
		} else if (experiment.getStatus() == EStatus.START_MEASUREMENT) {
			elapsedTimeTimer.scheduleRepeating(1000);
			tabView.getStatusPanel().getProgressBar().setValue(0, false);
		}
	}

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

		if (controllerExperiment.getProgress() == -1 && controllerExperiment.getTimeRemaining() > 0) {
			float progress = 100 / (controllerExperiment.getTimeRemaining() / 1000)
					* ((controllerExperiment.getTimeRemaining() / 1000) - durationRemaining);
			progress = Math.min(progress, 99);
			tabView.getStatusPanel().getProgressBar().setValue(progress);
		}

		tabView.getStatusPanel().setTimeElapsed(elapsed);
		tabView.getStatusPanel().setTimeRemaining(remaining);
	}
}
