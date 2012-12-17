package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.ExecuteTabPanel;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerTwo extends TabController {

	private ScheduleTab view;
	private List<FrontendScheduledExperiment> scheduledExperimentList;

	public TabControllerTwo(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	private void initialize() {
		view = new ScheduleTab();
	}

	public void updateList(List<FrontendScheduledExperiment> result) {
		scheduledExperimentList = result;
		view.refreshUi(scheduledExperimentList);
		updateExperimentCount();
	}

	public void refreshUi() {
		double metering = Metering.start();

		view.clear();

		Map<Long, ScheduleItemPanel> scheduledItems = new TreeMap<Long, ScheduleItemPanel>();

		for (FrontendScheduledExperiment exp : scheduledExperimentList) {
			ScheduleItemPanel item = new ScheduleItemPanel(exp);
			scheduledItems.put(exp.getNextExecutionTime(), item);
		}

		for (ScheduleItemPanel item : scheduledItems.values()) {
			view.add(item);
		}

		Metering.stop(metering);
	}

	@Override
	public FlowPanel getView() {
		return view;
	}

	@Override
	public void onSelection() {

	}

	public void updateExperimentCount() {
		((ExecuteTabPanel) getParentController().getView()).getTabBar().setTabText(1,
				R.get("ScheduledExperiments") + " [" + scheduledExperimentList.size() + "]");
	}
}
