package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;
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

	public void refreshUi(List<FrontendScheduledExperiment> pExperimentList) {
		double metering = Metering.start();

		clear();

		Map<Long, ScheduleItemPanel> scheduledItems = new TreeMap<Long, ScheduleItemPanel>();

		for (FrontendScheduledExperiment exp : pExperimentList) {
			ScheduleItemPanel item = new ScheduleItemPanel(exp);
			scheduledItems.put(exp.getNextExecutionTime(), item);
		}

		for (ScheduleItemPanel item : scheduledItems.values()) {
			add(item);
		}

		Metering.stop(metering);
	}
}
