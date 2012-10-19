package org.sopeco.frontend.shared.helper;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Container to transmit the Extensions.
 * 
 * @author Marius Oehler
 * 
 */
public class Extension implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private HashMap<String, String> config;

	public Extension() {
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the config
	 */
	public HashMap<String, String> getConfig() {
		return config;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param config
	 *            the config to set
	 */
	public void setConfig(HashMap<String, String> config) {
		this.config = config;
	}

}
