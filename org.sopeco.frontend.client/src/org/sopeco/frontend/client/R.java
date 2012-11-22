package org.sopeco.frontend.client;

import java.util.HashMap;

import org.sopeco.frontend.client.helper.SimpleNotify;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class R {
	private static final String DEFAULT_LANG = "rsc/lang/en.ini";
	private static boolean loaded = false;
	private static final String LINESEPERATOR = "\n";
	private static final String PAIRSEPERATOR = "=";

	private static HashMap<String, String> langMap = new HashMap<String, String>();

	private R() {
	}

	/**
	 * Returns the string of the lang. file which is related to the key.
	 * 
	 * @param key
	 *            key
	 * @return text
	 */
	public static String get(String key) {
		String txt = langMap.get(key);

		if (txt == null) {
			return "{" + key + "}";
		}

		return txt;
	}

	/**
	 * Loads the default lang. file.
	 * 
	 * @return true if file is loaded
	 */
	public static void loadLangFile(final SimpleNotify callback) {
		if (loaded) {
			return;
		}
		loaded = true;

		try {
			new RequestBuilder(RequestBuilder.GET, DEFAULT_LANG + "?" + Math.random()).sendRequest("",
					new RequestCallback() {
						@Override
						public void onResponseReceived(Request req, Response resp) {
							buildMap(resp.getText());
							callback.call();
						}

						@Override
						public void onError(Request res, Throwable throwable) {
							throw new RuntimeException(throwable);
						}
					});
		} catch (RequestException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param propertyString
	 */
	private static void buildMap(String propertyString) {
		for (String row : propertyString.split(LINESEPERATOR)) {
			if (row.isEmpty()) {
				continue;
			}

			row = row.replaceAll("[\\r]", "");

			String[] pair = row.split(PAIRSEPERATOR);

			if (pair == null || pair.length != 2) {
				continue;
			}

			langMap.put(pair[0], pair[1]);
		}
	}
}
