package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.shared.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduleTab extends FlowPanel {
	public ScheduleTab() {
		init();
	}

	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);
	}

	public void refreshList() {
		double metering = Metering.start();
		clear();

		Map<Long, ScheduledItem> scheduledItems = new TreeMap<Long, ScheduledItem>();

		for (ScheduledExperiment e : Manager.get().getCurrentScenarioDetails().getScheduledExperimentsList()) {
			ScheduledItem item = new ScheduledItem(e);
			// add(item);
			item.setParent(this);
			scheduledItems.put(e.getNextExecutionTime(), item);
		}

		for (ScheduledItem item : scheduledItems.values()) {
			add(item);
		}
		Metering.stop(metering);
	}

	public void removeExperiment(ScheduledItem item) {
		ScheduledExperiment exp = item.getExperiment();
		Manager.get().getCurrentScenarioDetails().getScheduledExperimentsList().remove(exp);
		Manager.get().storeAccountDetails();

		MainLayoutPanel.get().getExecuteController().refreshScheduleTab();
	}
}
