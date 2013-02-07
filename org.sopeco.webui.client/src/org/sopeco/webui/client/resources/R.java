/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.resources;

import java.util.HashMap;

import org.sopeco.webui.client.helper.SimpleNotify;

import com.google.gwt.core.client.GWT;
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

	public static final LanguageConstants lang = GWT.create(LanguageConstants.class);
	public static final Resources resc = GWT.create(Resources.class);

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
