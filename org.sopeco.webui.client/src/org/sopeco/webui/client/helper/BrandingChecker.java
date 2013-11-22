package org.sopeco.webui.client.helper;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class BrandingChecker {

	private static boolean checked = false;
	private static boolean branded;
	private static String url = "/branding.png";

	private BrandingChecker() {
	}

	/**
	 * Checks if an image for the branding exists and calls the callback with
	 * the result (boolean).
	 * 
	 * @param callback
	 */
	public static void checkBranding(final SimpleCallback<Boolean> callback) {
		if (checked) {
			callback.callback(branded);
		} else {
			try {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
				builder.sendRequest(null, new RequestCallback() {
					@Override
					public void onError(Request request, Throwable exception) {
						checked = true;
						branded = false;
						callback.callback(branded);
					}

					@Override
					public void onResponseReceived(Request request, Response response) {
						if (200 == response.getStatusCode()) {
							checked = true;
							branded = true;
						} else {
							checked = true;
							branded = false;
						}
						callback.callback(branded);
					}
				});
			} catch (Exception e) {
				checked = true;
				branded = false;
				callback.callback(branded);
			}
		}
	}

}
