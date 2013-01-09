package org.sopeco.frontend.shared.helper;

import org.sopeco.frontend.shared.push.PushSerializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EventLogLite implements PushSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long time;
	private String message;
	private boolean error;

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param pTime
	 *            the time to set
	 */
	public void setTime(long pTime) {
		this.time = pTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param pMessage
	 *            the message to set
	 */
	public void setMessage(String pMessage) {
		this.message = pMessage;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param pError
	 *            the error to set
	 */
	public void setError(boolean pError) {
		this.error = pError;
	}

}
