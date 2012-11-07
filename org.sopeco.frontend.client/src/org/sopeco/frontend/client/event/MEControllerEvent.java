package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.MEControllerEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerEvent extends GwtEvent<MEControllerEventHandler> {

	/** */
	public static final Type<MEControllerEventHandler> TYPE = new Type<MEControllerEventHandler>();

	/** Why is this Event fired. */
	public enum EventType {
		/** The controller was changed. */
		CONTROLLER_CHANGED,
		/** The status of the controller was updated. */
		STATUS_UPDATED
	}

	private EventType eventType;

	public MEControllerEvent(EventType type) {
		eventType = type;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	@Override
	public Type<MEControllerEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MEControllerEventHandler handler) {
		handler.onNewMEControllerEvent(this);
	}

}
