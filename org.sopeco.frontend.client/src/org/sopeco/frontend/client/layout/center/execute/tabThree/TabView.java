package org.sopeco.frontend.client.layout.center.execute.tabThree;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabView extends FlowPanel {

	private StatusPanel statusPanel;

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

		add(statusPanel);

		add(new QueueItem());
		add(new QueueItem());
	}

	/**
	 * @return the statusPanel
	 */
	public StatusPanel getStatusPanel() {
		return statusPanel;
	}

}
