package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ScenarioChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ScenarioChangedEventHandler extends EventHandler {
	void onScenarioChanged(ScenarioChangedEvent scenarioChangedEvent);
}
