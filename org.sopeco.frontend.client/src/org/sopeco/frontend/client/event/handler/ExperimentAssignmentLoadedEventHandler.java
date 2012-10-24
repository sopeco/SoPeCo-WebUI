package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ExperimentAssignmentLoadedEvent;

import com.google.gwt.event.shared.EventHandler;
/**
 * 
 * @author Marius Oehler
 *
 */
public interface ExperimentAssignmentLoadedEventHandler extends EventHandler {
	void onExperimentAssignmentLoadedEvent(ExperimentAssignmentLoadedEvent event);
}
