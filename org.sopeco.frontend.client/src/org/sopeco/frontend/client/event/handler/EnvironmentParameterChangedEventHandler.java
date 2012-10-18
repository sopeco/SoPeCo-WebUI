package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.EnvironmentParameterChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface EnvironmentParameterChangedEventHandler extends EventHandler {
	void onEnvironmentParameterChangedEvent(EnvironmentParameterChangedEvent event);
}
