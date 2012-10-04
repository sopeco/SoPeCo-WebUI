package org.sopeco.frontend.client.helper;

import java.util.HashMap;

import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class SystemDetails {

	// MetaDatabaseDetails
	private static boolean metaDatabaseDetailsAvailable = false;

	private static String metaHost;
	private static String metaPort;

	private SystemDetails() {
	}

	/**
	 * 
	 */
	public static void load() {
		loadMetaDatabaseDetails();
	}

	/**
	 * 
	 */
	private static void loadMetaDatabaseDetails() {
		if (!metaDatabaseDetailsAvailable) {
			Loader.showLoader();

			RPC.getSystemDetailsRPC().getMetaDatabaseDetails(new AsyncCallback<HashMap<String, String>>() {
				@Override
				public void onSuccess(HashMap<String, String> result) {
					metaHost = result.get("host");
					metaPort = result.get("port");

					Loader.hideLoader();
				}

				@Override
				public void onFailure(Throwable caught) {
					Loader.hideLoader();

					Message.error("Can't load system details!");
				}
			});
		}
		metaDatabaseDetailsAvailable = true;
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
