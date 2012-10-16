package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationChangedEvent extends GwtEvent<SpecificationChangedEventHandler> {

	public static Type<SpecificationChangedEventHandler> TYPE = new Type<SpecificationChangedEventHandler>();
	private String selectedSpecification;

	public SpecificationChangedEvent(String selected) {
		selectedSpecification = selected;
	}

	public String getSelectedSpecification() {
		return selectedSpecification;
	}

	@Override
	public Type<SpecificationChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SpecificationChangedEventHandler handler) {
		handler.onSpecificationChangedEvent(this);
	}

}
