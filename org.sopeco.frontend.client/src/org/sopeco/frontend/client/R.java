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
	private static HashMap<String, String> langMap = new HashMap<String, String>();
	private static final String LINESEPERATOR = "\n";
	private static boolean loaded = false;

	private static final String PAIRSEPERATOR = "=";

	/**
	 * Returns the string of the language file(map) which is related to the
	 * given key.
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
	 * Loads the default language file and calls the callback, if it
	 * successfully finished the request. If the file was already loaded, the
	 * method is canceled.
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
						public void onError(Request res, Throwable throwable) {
							throw new RuntimeException(throwable);
						}

						@Override
						public void onResponseReceived(Request req, Response resp) {
							buildMap(resp.getText());
							callback.call();
						}
					});
		} catch (RequestException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Splits key-value-pairs of the given string and sets them in the langMap
	 * map.
	 * 
	 * @param propertyString
	 */
	private static void buildMap(String propertyString) {
		for (String row : propertyString.split(LINESEPERATOR)) {
			if (row.isEmpty() || row.startsWith("#")) {
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

	private R() {
	}
}
