package org.sopeco.webui.shared.rpc;

import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.helper.LoginResponse;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface AccountManagementRPCAsync {

	void createAccount(String accountName, String password, AsyncCallback<Boolean> callback);

	void createAccount(String accountName, String password, String dbHost, int dbPort, AsyncCallback<Boolean> callback);

	void accountExist(String accountName, AsyncCallback<Boolean> callback);

	void checkPassword(String accountName, String password, AsyncCallback<Boolean> callback);

	void loginWithPassword(String accountName, String password, AsyncCallback<LoginResponse> callback);

	void loginWithToken(String accountName, String rememberMeToken, AsyncCallback<LoginResponse> callback);

	void getAccountDetails(AsyncCallback<AccountDetails> callback);

	void storeAccountDetails(AccountDetails accountDetails, AsyncCallback<Void> callback);
	
	void logout(AsyncCallback<Void> callback);
}
