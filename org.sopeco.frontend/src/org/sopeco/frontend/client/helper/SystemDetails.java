package org.sopeco.frontend.client.helper;

import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.SystemDetailsRPC;
import org.sopeco.frontend.client.rpc.SystemDetailsRPCAsync;

import com.google.gwt.core.client.GWT;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SystemDetails {

	// MetaDatabaseDetails
	private static boolean metaDatabaseDetailsAvailable = false;
	private static String metaHost;
	private static String metaPort;

	private static SystemDetailsRPCAsync rpc = GWT.create(SystemDetailsRPC.class);
	
	private SystemDetails() {
	}

	public static void load () {
		loadMetaDatabaseDetails();
	}
	
	/*
	 * 
	 */
	private static void loadMetaDatabaseDetails() {
		if ( !metaDatabaseDetailsAvailable ) {
			Loader.showLoader();
			
			rpc.getMetaDatabaseDetails(new AsyncCallback<HashMap<String,String>>() {
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

	public static String getMetaDatabaseHost() {
		if (!metaDatabaseDetailsAvailable)
			return "";
		return metaHost;
	}

	public static String getMetaDatabasePort() {
		if (!metaDatabaseDetailsAvailable)
			return "";
		return metaPort;
	}

}
