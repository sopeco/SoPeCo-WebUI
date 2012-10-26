package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.PreperationAssignmentRenderedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface PreperationAssignmentRenderedEventHandler extends EventHandler {
	void onAssignmentLoadedEvent(PreperationAssignmentRenderedEvent event);
}
