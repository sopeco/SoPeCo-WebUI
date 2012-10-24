package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ScenarioLoadedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioLoadedEvent extends GwtEvent<ScenarioLoadedEventHandler> {

	/** */
	public static final Type<ScenarioLoadedEventHandler> TYPE = new Type<ScenarioLoadedEventHandler>();

	@Override
	public Type<ScenarioLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScenarioLoadedEventHandler handler) {
		handler.onScenarioLoadedEvent(this);
	}

}
