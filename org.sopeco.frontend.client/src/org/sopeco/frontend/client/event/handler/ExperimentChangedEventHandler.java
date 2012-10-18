package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.ExperimentChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ExperimentChangedEventHandler extends EventHandler {
	void onExperimentChanged(ExperimentChangedEvent event);
}
