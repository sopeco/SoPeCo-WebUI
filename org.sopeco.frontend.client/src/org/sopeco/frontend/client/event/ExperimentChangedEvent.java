package org.sopeco.frontend.client.event;

import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentChangedEvent extends GwtEvent<ExperimentChangedEventHandler> {

	/** */
	public static final Type<ExperimentChangedEventHandler> TYPE = new Type<ExperimentChangedEventHandler>();
	private String experimentName;

	public ExperimentChangedEvent(String name) {
		experimentName = name;
	}

	/**
	 * @return the experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	@Override
	public Type<ExperimentChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExperimentChangedEventHandler handler) {
		handler.onExperimentChanged(this);
	}

}
