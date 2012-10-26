package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.EnvironmentDefinitionChangedEvent;

import com.google.gwt.event.shared.EventHandler;
/**
 * 
 * @author Marius Oehler
 *
 */
public interface EnvironmentDefinitionChangedEventHandler extends EventHandler {
	void onEnvironmentChangedEvent(EnvironmentDefinitionChangedEvent event);
}
