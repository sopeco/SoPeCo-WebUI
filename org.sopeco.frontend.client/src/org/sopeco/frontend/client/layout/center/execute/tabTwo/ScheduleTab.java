package org.sopeco.frontend.client.layout.center.execute.tabTwo;

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
		
		add(new ScheduledItem());
		add(new ScheduledItem());
		add(new ScheduledItem());
	}
}
