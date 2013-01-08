package org.sopeco.frontend.client.layout.center.execute.tabThree;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabView extends FlowPanel {

	private StatusPanel statusPanel;
	private List<QueueItem> queueItems;

	/**
	 * Constructor.
	 */
	public TabView() {
		init();
	}

	/**
	 * Initialize all objects.
	 */
	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);

		statusPanel = new StatusPanel();
		queueItems = new ArrayList<QueueItem>();
		
		add(statusPanel);
	}

	/**
	 * Adding a QueueItem to the view.
	 * 
	 * @param item
	 */
	public void addQueueItem(QueueItem item) {
		queueItems.add(item);
		add(item);
	}

	/**
	 * Removes all QueueItems from the view.
	 */
	public void removeAllQueueItems() {
		for (QueueItem item : queueItems) {
			item.removeFromParent();
		}
		queueItems.clear();
	}

	/**
	 * @return the statusPanel
	 */
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

}
