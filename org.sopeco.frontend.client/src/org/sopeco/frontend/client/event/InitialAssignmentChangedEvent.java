package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.InitialAssignmentChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class InitialAssignmentChangedEvent extends GwtEvent<InitialAssignmentChangedEventHandler> {

	/**
	 * 
	 */
	public enum ChangeType {
		/** Assignment added. */
		Added,
		/** Assignment removed. */
		Removed
	}

	public static Type<InitialAssignmentChangedEventHandler> TYPE = new Type<InitialAssignmentChangedEventHandler>();
	private String fullParameterName;
	private ChangeType type;

	public InitialAssignmentChangedEvent(String name, ChangeType cType) {
		fullParameterName = name;
		type = cType;
	}

	public String getFullParameterName() {
		return fullParameterName;
	}

	/**
	 * @return the type
	 */
	public ChangeType getChangeType() {
		return type;
	}

	@Override
	public Type<InitialAssignmentChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(InitialAssignmentChangedEventHandler handler) {
		handler.onInitialAssignmentChanged(this);
	}

}
