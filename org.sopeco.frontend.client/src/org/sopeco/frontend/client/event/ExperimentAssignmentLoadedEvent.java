package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ExperimentAssignmentLoadedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentLoadedEvent extends GwtEvent<ExperimentAssignmentLoadedEventHandler> {

	/** */
	public static final Type<ExperimentAssignmentLoadedEventHandler> TYPE = new Type<ExperimentAssignmentLoadedEventHandler>();

	@Override
	public Type<ExperimentAssignmentLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExperimentAssignmentLoadedEventHandler handler) {
		handler.onExperimentAssignmentLoadedEvent(this);
	}

}
