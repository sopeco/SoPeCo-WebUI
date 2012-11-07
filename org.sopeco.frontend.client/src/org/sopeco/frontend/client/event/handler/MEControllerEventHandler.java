package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.MEControllerEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 *
 */
public interface MEControllerEventHandler extends EventHandler {
	void onNewMEControllerEvent(MEControllerEvent event);
}
