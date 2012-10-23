package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.PreperationAssignmentLoadedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface PreperationAssignmentLoadedEventHandler extends EventHandler {
	void onAssignmentLoadedEvent(PreperationAssignmentLoadedEvent event);
}
