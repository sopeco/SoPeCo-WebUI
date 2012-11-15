package org.sopeco.frontend.client.helper;

import java.util.List;

import org.sopeco.frontend.client.helper.INotifyHandler.Result;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class DBManager {

	private static DatabaseManagerRPCAsync dbManager;
	private static List<DatabaseInstance> databaseList;

	private DBManager() {
	}

	/**
	 * 
	 */
	public static void init() {

		loadDatabases(null);

	}

	/**
	 * 
	 */
	public static void loadDatabases(INotifyHandler<List<DatabaseInstance>> notify) {

		getDbManager().getAllDatabases(new LoadingDbHandler(notify));

	}

	/**
	 * 
	 * @return
	 */
	public static DatabaseManagerRPCAsync getDbManager() {
		if (dbManager == null) {
			dbManager = GWT.create(DatabaseManagerRPC.class);
		}

		return dbManager;
	}

	/**
	 * 
	 * @return
	 */
	public static List<DatabaseInstance> getLoadedDatabases() {
		return databaseList;
	}

	private static class LoadingDbHandler implements AsyncCallback<List<DatabaseInstance>> {
		private INotifyHandler<List<DatabaseInstance>> notify;

		public LoadingDbHandler(INotifyHandler<List<DatabaseInstance>> notifyHandler) {
			this.notify = notifyHandler;
		}

		@Override
		public void onFailure(Throwable caught) {
			Message.error(caught.getLocalizedMessage());

			if (notify != null) {
				Result<List<DatabaseInstance>> callResult = new Result<List<DatabaseInstance>>(false, null);
				notify.call(callResult);
			}
		}

		@Override
		public void onSuccess(List<DatabaseInstance> result) {
			databaseList = result;

			if (notify != null) {
				Result<List<DatabaseInstance>> callResult = new Result<List<DatabaseInstance>>(true, result);
				notify.call(callResult);
			}
		}
	}
}
