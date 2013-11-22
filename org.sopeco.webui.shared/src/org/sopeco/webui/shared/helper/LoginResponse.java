package org.sopeco.webui.shared.helper;

import java.io.Serializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class LoginResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean successful;

	public LoginResponse(boolean successful, String rememberMeToken) {
		this.successful = successful;
		this.rememberMeToken = rememberMeToken;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public String getRememberMeToken() {
		return rememberMeToken;
	}

	private String rememberMeToken;

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public void setRememberMeToken(String rememberMeToken) {
		this.rememberMeToken = rememberMeToken;
	}

	public LoginResponse() {
	}

}
