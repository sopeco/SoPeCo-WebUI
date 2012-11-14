package org.sopeco.frontend.shared.helper;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class Utilities {
	private Utilities() {
	}

	/**
	 * Replaces all characters expect "a-zA-Z0-9_" (regex: [^a-zA-Z0-9_]) of the
	 * given String with "_".
	 * 
	 * @return
	 */
	public static String cleanString(String scenarioName) {
		return scenarioName.replaceAll("[^a-zA-Z0-9_]", "_");
	}
}
