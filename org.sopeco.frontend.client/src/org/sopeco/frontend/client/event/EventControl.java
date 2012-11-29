package org.sopeco.frontend.client.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class EventControl {

	private static EventBus eventBus;
	private static List<HandlerRegistration> handlerRegistrationList;

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

		@Override
		public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
			HandlerRegistration reg = super.addHandler(type, handler);
			EventControl.addRegistration(reg);
			return reg;
		}
	}

	/**
	 * When a handler is added to the EventBus (addHandler), it will be added to
	 * the HandlerRegistrationList too.
	 * 
	 * @param reg
	 */
	private static void addRegistration(HandlerRegistration reg) {
		if (handlerRegistrationList == null) {
			handlerRegistrationList = new ArrayList<HandlerRegistration>();
		}
		handlerRegistrationList.add(reg);
	}

	/**
	 * Removes all registered handlers from the EventBus.
	 */
	public static void removeAllHandler() {
		if (handlerRegistrationList == null) {
			return;
		}
		List<HandlerRegistration> tempList = new ArrayList<HandlerRegistration>(handlerRegistrationList);
		for (HandlerRegistration reg : tempList) {
			reg.removeHandler();
			handlerRegistrationList.remove(reg);
		}
	}
}
