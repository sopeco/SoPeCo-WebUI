package org.sopeco.frontend.client.event.handler;

import org.sopeco.frontend.client.event.SpecificationChangedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface SpecificationChangedEventHandler extends EventHandler {
	void onSpecificationChangedEvent(SpecificationChangedEvent event);
}
