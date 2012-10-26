package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.PreperationAssignmentsChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface PreperationAssignmentsChangedEventHandler extends EventHandler {
	void onAssignmentChangedEvent(PreperationAssignmentsChangedEvent event);
}
