package org.sopeco.frontend.client.helper;

import java.util.HashMap;

import org.sopeco.frontend.client.helper.callback.ParallelCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class SystemDetails {

	private static boolean metaDatabaseDetailsAvailable = false;

	private static String metaHost;
	private static String metaPort;

	private SystemDetails() {
	}

	public static ParallelCallback<HashMap<String, String>> getLoadingCallback() {
		return new ParallelCallback<HashMap<String, String>>() {
			@Override
			public void onSuccess(HashMap<String, String> result) {
				metaHost = result.get("host");
				metaPort = result.get("port");
				metaDatabaseDetailsAvailable = true;
				super.onSuccess(result);
			}
		};
	}

	/**
	 * 
	 * @return
	 */
	public static String getMetaDatabaseHost() {
		if (!metaDatabaseDetailsAvailable) {
			return "";
		}

		return metaHost;
	}

	/**
	 * 
	 * @return
	 */
	public static String getMetaDatabasePort() {
		if (!metaDatabaseDetailsAvailable) {
			return "";
		}

		return metaPort;
	}

}
