package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.PreperationAssignmentLoadedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentLoadedEvent extends GwtEvent<PreperationAssignmentLoadedEventHandler> {

	/** */
	public static final Type<PreperationAssignmentLoadedEventHandler> TYPE = new Type<PreperationAssignmentLoadedEventHandler>();

	@Override
	public Type<PreperationAssignmentLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PreperationAssignmentLoadedEventHandler handler) {
		handler.onAssignmentLoadedEvent(this);
	}

}
