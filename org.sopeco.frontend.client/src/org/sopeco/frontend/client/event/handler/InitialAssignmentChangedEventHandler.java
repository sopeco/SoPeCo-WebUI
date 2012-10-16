package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface InitialAssignmentChangedEventHandler extends EventHandler {
	void onInitialAssignmentChanged(InitialAssignmentChangedEvent event);
}
