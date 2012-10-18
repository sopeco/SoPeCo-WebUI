package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.EnvironmentParameterChangedEventHandler;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentParameterChangedEvent extends GwtEvent<EnvironmentParameterChangedEventHandler> {

	public static Type<EnvironmentParameterChangedEventHandler> TYPE = new Type<EnvironmentParameterChangedEventHandler>();
	private ParameterDefinition oldParameter, newParameter;

	public EnvironmentParameterChangedEvent(ParameterDefinition pOldparameter, ParameterDefinition pNewParameter) {
		oldParameter = pOldparameter;
		newParameter = pNewParameter;
	}

	/**
	 * @return the oldParameter
	 */
	public ParameterDefinition getOldParameter() {
		return oldParameter;
	}

	/**
	 * @return the newParameter
	 */
	public ParameterDefinition getNewParameter() {
		return newParameter;
	}

	@Override
	public Type<EnvironmentParameterChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EnvironmentParameterChangedEventHandler handler) {
		handler.onEnvironmentParameterChangedEvent(this);
	}

}
