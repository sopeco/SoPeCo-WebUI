package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.PreperationAssignmentsChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentsChangedEvent extends GwtEvent<PreperationAssignmentsChangedEventHandler> {

	/** */
	public static final Type<PreperationAssignmentsChangedEventHandler> TYPE = new Type<PreperationAssignmentsChangedEventHandler>();

	@Override
	public Type<PreperationAssignmentsChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PreperationAssignmentsChangedEventHandler handler) {
		handler.onAssignmentChangedEvent(this);
	}

}
