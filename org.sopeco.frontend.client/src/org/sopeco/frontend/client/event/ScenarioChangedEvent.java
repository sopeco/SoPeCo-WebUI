package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ScenarioChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioChangedEvent extends GwtEvent<ScenarioChangedEventHandler> {

	/** */
	public static final Type<ScenarioChangedEventHandler> TYPE = new Type<ScenarioChangedEventHandler>();
	private String scenarioName;

	public ScenarioChangedEvent(String name) {
		scenarioName = name;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	@Override
	public Type<ScenarioChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ScenarioChangedEventHandler handler) {
		handler.onScenarioChanged(this);
	}
}
