package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ExperimentAssignmentsChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentsChangedEvent extends GwtEvent<ExperimentAssignmentsChangedEventHandler> {

	/** */
	public static final Type<ExperimentAssignmentsChangedEventHandler> TYPE = new Type<ExperimentAssignmentsChangedEventHandler>();

	@Override
	public Type<ExperimentAssignmentsChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExperimentAssignmentsChangedEventHandler handler) {
		handler.onAssignmentChangedEvent(this);
	}

}
