package org.sopeco.webui.server.helper;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class LinkChecker {

	private LinkChecker() {
	}

	private static final String LOGO = "branding.png";

	public static String hasBranding(HttpServletRequest request) {
		String logoUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/"
				+ LOGO;

		try {
			URL u = new URL(logoUrl);
			HttpURLConnection huc = (HttpURLConnection) u.openConnection();
			huc.setRequestMethod("HEAD");
			huc.connect();
			if (huc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return "true";
			} else {
				return "false";
			}
		} catch (Exception e) {
			return "false";
		}

	}

}
