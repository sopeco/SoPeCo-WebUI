package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ExperimentAssignmentsChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ExperimentAssignmentsChangedEventHandler extends EventHandler {
	void onAssignmentChangedEvent(ExperimentAssignmentsChangedEvent event);
}
