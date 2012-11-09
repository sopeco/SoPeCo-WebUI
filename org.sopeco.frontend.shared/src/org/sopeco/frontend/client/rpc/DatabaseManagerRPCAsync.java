package org.sopeco.frontend.client.rpc;

import java.util.List;

import org.sopeco.frontend.client.entities.AccountDetails;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface DatabaseManagerRPCAsync {

	void getAllDatabases(AsyncCallback<List<DatabaseInstance>> callback);

	void removeDatabase(DatabaseInstance databaseInstance, AsyncCallback<Boolean> callback);

	void addDatabase(DatabaseInstance databaseInstance, String passwd, AsyncCallback<Boolean> callback);

	void selectDatabase(DatabaseInstance databaseInstance, String passwd, AsyncCallback<Boolean> callback);

	void checkPassword(DatabaseInstance databaseInstance, String passwd, AsyncCallback<Boolean> callback);

	void getAccountDetails(AsyncCallback<AccountDetails> callback);

	void storeAccountDetails(AccountDetails details, AsyncCallback<Void> callback);
}
