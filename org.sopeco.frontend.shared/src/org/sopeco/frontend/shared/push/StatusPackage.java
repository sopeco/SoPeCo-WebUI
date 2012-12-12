package org.sopeco.frontend.shared.push;

import org.sopeco.frontend.client.rpc.PushRPC.Type;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusPackage extends PushPackage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum EventType {
		ACQUIRE_MEC, ACQUIRE_MEC_FAILED, ERROR, EXECUTE_EXPERIMENTRUN, FINALIZE_EXPERIMENTSERIES, INIT_MEC, MEASUREMENT_FINISHED, PREPARE_EXPERIMENTSERIES, RELEASE_MEC
	}

	private EventType eventType;

	public StatusPackage() {
		super(Type.CONTROLLER_STATUS);
	}

	public StatusPackage(EventType pType) {
		super(Type.CONTROLLER_STATUS);
		eventType = pType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType pType) {
		this.eventType = pType;
	}

}
