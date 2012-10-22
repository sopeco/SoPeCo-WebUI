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
public class ExtensionContainer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<ExtensionTypes, Map<String, Map<String, String>>> extensionMap;

	public ExtensionContainer() {
		extensionMap = new HashMap<ExtensionTypes, Map<String, Map<String, String>>>();
	}

	/**
	 * @return the extensionMap
	 */
	public Map<ExtensionTypes, Map<String, Map<String, String>>> getMap() {
		return extensionMap;
	}

	/**
	 * Returns a map with all extension of the given type.
	 * 
	 * @param type
	 * @return
	 */
	public Map<String, Map<String, String>> getExtensions(ExtensionTypes type) {
		return extensionMap.get(type);
	}
}
