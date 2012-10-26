package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.EnvironmentDefinitionChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentDefinitionChangedEvent extends GwtEvent<EnvironmentDefinitionChangedEventHandler> {

	/** */
	public static final Type<EnvironmentDefinitionChangedEventHandler> TYPE = new Type<EnvironmentDefinitionChangedEventHandler>();

	@Override
	public Type<EnvironmentDefinitionChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EnvironmentDefinitionChangedEventHandler handler) {
		handler.onEnvironmentChangedEvent(this);
	}
}
