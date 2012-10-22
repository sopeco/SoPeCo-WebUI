package org.sopeco.frontend.shared.helper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

	private Map<String, Map<String, String>> extensionMap;

	public Extension() {
		extensionMap = new HashMap<String, Map<String, String>>();
	}

	/**
	 * @return the extensionMap
	 */
	public Map<String, Map<String, String>> getExtensionMap() {
		return extensionMap;
	}
}
