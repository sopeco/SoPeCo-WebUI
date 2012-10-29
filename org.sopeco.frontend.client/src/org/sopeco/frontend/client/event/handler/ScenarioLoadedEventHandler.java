package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ScenarioLoadedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ScenarioLoadedEventHandler extends EventHandler {
	void onScenarioLoadedEvent(ScenarioLoadedEvent scenarioLoadedEvent);
}
