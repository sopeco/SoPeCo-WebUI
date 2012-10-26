package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.PreperationAssignmentRenderedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentRenderedEvent extends GwtEvent<PreperationAssignmentRenderedEventHandler> {

	/** */
	public static final Type<PreperationAssignmentRenderedEventHandler> TYPE = new Type<PreperationAssignmentRenderedEventHandler>();

	@Override
	public Type<PreperationAssignmentRenderedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PreperationAssignmentRenderedEventHandler handler) {
		handler.onAssignmentLoadedEvent(this);
	}

}
