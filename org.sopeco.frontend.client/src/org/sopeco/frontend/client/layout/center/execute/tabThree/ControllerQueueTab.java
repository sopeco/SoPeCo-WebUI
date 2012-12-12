package org.sopeco.frontend.client.layout.center.execute.tabThree;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ControllerQueueTab extends FlowPanel {

	private ActiveQueueItem activeItem;

	public ControllerQueueTab() {
		init();
	}

	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);
		
		activeItem = new ActiveQueueItem();
		add(activeItem);

		add(new QueueItem());
		add(new QueueItem());
		
		activeItem.setDummyProgress();
	}
}
