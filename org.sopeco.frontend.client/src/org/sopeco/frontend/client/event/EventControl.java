package org.sopeco.frontend.client.event;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
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
			eventBus = GWT.create(MyEventBus.class);
		}
		return eventBus;
	}

	private static class MyEventBus extends SimpleEventBus {
		@Override
		public void fireEvent(GwtEvent<?> event) {
			super.fireEvent(event);

			Exception x = new Exception();
			GWT.log("Event fired from: " + x.getStackTrace()[1].getFileName() + " "
					+ x.getStackTrace()[1].getMethodName() + ":" + x.getStackTrace()[1].getLineNumber());
		}
	}
}
