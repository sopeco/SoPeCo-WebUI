package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.shared.definitions.DatabaseDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatabaseManagerRPCAsync {

	void getAllDatabases(AsyncCallback<List<DatabaseDefinition>> callback);

	void removeDatabase(DatabaseDefinition databaseDefinition,
			AsyncCallback<Boolean> callback);
	
	void addDatabase(DatabaseDefinition databaseDefinition,
			AsyncCallback<Boolean> callback);

}
