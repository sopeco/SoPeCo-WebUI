package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ExperimentAssignmentRenderedEvent;

import com.google.gwt.event.shared.EventHandler;
/**
 * 
 * @author Marius Oehler
 *
 */
public interface ExperimentAssignmentRenderedEventHandler extends EventHandler {
	void onExperimentAssignmentLoadedEvent(ExperimentAssignmentRenderedEvent event);
}
