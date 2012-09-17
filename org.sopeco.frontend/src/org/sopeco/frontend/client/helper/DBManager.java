package org.sopeco.frontend.client.helper;

import java.util.List;

import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DBManager {

	private static DatabaseManagerRPCAsync dbManager;
	private static List<DatabaseInstance> databaseList;
	
	/**
	 * 
	 */
	public static void init () {
		
		loadDatabases();
		
	}
	
	/**
	 * 
	 */
	private static void loadDatabases () {
		
		getDbManager().getAllDatabases(new AsyncCallback<List<DatabaseInstance>>() {
			@Override
			public void onSuccess(List<DatabaseInstance> result) {
				databaseList = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getLocalizedMessage());
			}
		});
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static DatabaseManagerRPCAsync getDbManager () {
		if ( dbManager == null ){
			dbManager = GWT.create(DatabaseManagerRPC.class);
		}
		
		return dbManager;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<DatabaseInstance> getDatabases () {
		return databaseList;
	}
}
