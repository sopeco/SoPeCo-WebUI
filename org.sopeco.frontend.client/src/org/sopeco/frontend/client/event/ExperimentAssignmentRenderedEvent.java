package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ExperimentAssignmentRenderedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentRenderedEvent extends GwtEvent<ExperimentAssignmentRenderedEventHandler> {

	/** */
	public static final Type<ExperimentAssignmentRenderedEventHandler> TYPE = new Type<ExperimentAssignmentRenderedEventHandler>();

	@Override
	public Type<ExperimentAssignmentRenderedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExperimentAssignmentRenderedEventHandler handler) {
		handler.onExperimentAssignmentLoadedEvent(this);
	}

}
