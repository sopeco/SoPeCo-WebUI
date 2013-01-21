package org.sopeco.frontend.shared.helper;

import javax.persistence.Entity;

import org.sopeco.frontend.shared.push.PushSerializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MECLogEntry implements PushSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long time;
	private String message;
	private boolean error;
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String pErrorMessage) {
		this.errorMessage = pErrorMessage;
	}

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
