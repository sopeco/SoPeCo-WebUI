package org.sopeco.webui.shared.rpc;

import org.sopeco.service.persistence.entities.AccountDetails;
import org.sopeco.webui.shared.helper.LoginResponse;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author Marius Oehler
 * 
 */
@RemoteServiceRelativePath("accountManagementRPC")
public interface AccountManagementRPC extends RemoteService {

	boolean createAccount(String accountName, String password);

	boolean createAccount(String accountName, String password, String dbHost, int dbPort);

	boolean accountExist(String accountName);

	boolean checkPassword(String accountName, String password);

	LoginResponse loginWithPassword(String accountName, String password);

	LoginResponse loginWithToken(String accountName, String rememberMeToken);

	AccountDetails getAccountDetails();

	void storeAccountDetails(AccountDetails accountDetails);
	
	void logout();
}
