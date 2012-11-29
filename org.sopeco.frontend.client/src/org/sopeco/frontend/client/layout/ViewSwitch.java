package org.sopeco.frontend.client.layout;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.layout.center.CenterType;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ViewSwitch {
	private static final Logger LOGGER = Logger.getLogger("view");

	/**
	 * Switch the view to the panel with the given panel-type.
	 * 
	 * @param type
	 *            type of the panel which will be shown.
	 */
	public void switchTo(CenterType type) {
		switchTo(type, true);
	}

	private void switchTo(CenterType type, boolean historyEvent) {
		LOGGER.fine("Switch view to type: " + type.toString());

		MainLayoutPanel.get().updateCenterPanel(type);
		MainLayoutPanel.get().getNavigationController().hideChangeSpecpanel();
	}

	/**
	 * Switch the view to the experiment-view and displays the experiment with
	 * the given name. Then it fires an ExperimentChanged-Event.
	 * 
	 * @param experimentName
	 *            name of the experiment
	 */
	public void switchToExperiment(String experimentName) {
		LOGGER.fine("Switch view to experiment: " + experimentName);

		if (MainLayoutPanel.get().getCenterType() != CenterType.Experiment) {
			switchTo(CenterType.Experiment);
		}

		MainLayoutPanel.get().getNavigationController().highlightExperiment(experimentName);

		ExperimentChangedEvent expChangedEvent = new ExperimentChangedEvent(experimentName);
		EventControl.get().fireEvent(expChangedEvent);
	}
}
