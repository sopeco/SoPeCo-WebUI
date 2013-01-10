package org.sopeco.frontend.server.papconnector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HTTPConnector {
	
	public HTTPConnector(){
		
	}
	
	public String[] executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			System.out.println("POST " + url + "?" + urlParameters);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			List<String> response = new ArrayList<String>();
			while ((line = rd.readLine()) != null) {
				response.add(line);
			}
			rd.close();
			return response.toArray(new String[0]);

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public String[] executeGet(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL + "?" + urlParameters);
			System.out.println("GET " + url);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			List<String> response = new ArrayList<String>();
			while ((line = rd.readLine()) != null) {
				response.add(line);
			}
			rd.close();
			return response.toArray(new String[0]);

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
