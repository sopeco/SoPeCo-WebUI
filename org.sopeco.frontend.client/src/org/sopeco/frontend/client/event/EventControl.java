package org.sopeco.frontend.client.event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class EventControl {

	private static EventBus eventBus;

	private EventControl() {
	}

	/**
	 * Returns an object (singleton) of the EventBus class.
	 * 
	 * @return
	 */
	public static EventBus get() {
		if (eventBus == null) {
			eventBus = GWT.create(SimpleEventBus.class);
		}
		return eventBus;
	}
}
