package org.sopeco.webui.server.rpc;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.service.rest.exchange.ServiceResponse;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.server.user.TokenManager;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.helper.LoginResponse;
import org.sopeco.webui.shared.rpc.AccountManagementRPC;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class AccountManagementRPCImpl extends SPCRemoteServlet implements AccountManagementRPC {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementRPCImpl.class);

	private static final long serialVersionUID = 1337009456998146393L;

	@Override
	public boolean createAccount(String accountName, String password) {
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				   										       ServiceConfiguration.SVC_ACCOUNT_CREATE);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON)
										  .post(new GenericType<ServiceResponse<Boolean>>() { });
		
		return sr_b.getObject();
	}

	@Override
	public boolean createAccount(String accountName, String password, String dbHost, int dbPort) {
		
		if (accountExist(accountName)) {
			return false;
		}
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
					       									   ServiceConfiguration.SVC_ACCOUNT_CREATE,
					       									   ServiceConfiguration.SVC_ACCOUNT_CUSTOMIZE);
		
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_DATABASENAME, dbHost);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_DATABASEPORT, String.valueOf(dbPort));
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON).post(new GenericType<ServiceResponse<Boolean>>() { });

		return sr_b.getObject();
	}

	@Override
	public boolean accountExist(String accountName) {
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
															   ServiceConfiguration.SVC_ACCOUNT_EXISTS);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON)
										  .get(new GenericType<ServiceResponse<Boolean>>() { });
		
		return sr_b.getObject();
	}

	@Override
	public boolean checkPassword(String accountName, String password) {
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
														       ServiceConfiguration.SVC_ACCOUNT_CHECK,
														       ServiceConfiguration.SVC_ACCOUNT_PASSWORD);
		
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON)
										  .get(new GenericType<ServiceResponse<Boolean>>() { });
		
		return sr_b.getObject();
	}

	@Override
	public LoginResponse loginWithPassword(String accountName, String password) {
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				       										   ServiceConfiguration.SVC_ACCOUNT_LOGIN);
		
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		
		ServiceResponse<String> sr_s = wr.accept(MediaType.APPLICATION_JSON)
										 .get(new GenericType<ServiceResponse<String>>() { });
		

		System.out.println(sr_s.getStatus().getStatusCode());
		System.out.println(sr_s.getMessage());
		System.out.println(sr_s);
		
		// if the status is not OK, then something has failed
		if (sr_s.getStatus() != Status.OK) {
			
			return new LoginResponse(false, null);
		}

		// add the token to the tokenmanager
		TokenManager.instance().registerToken(getSessionId(), sr_s.getObject());
		
		return new LoginResponse(true, sr_s.getObject());
	}

	/**
	 * The original idea of this method is not implemented. It's replaced with checking
	 * if the given token is valid.
	 * 
	 * The original idea was to have the user password encrypted into the given token.
	 */
	@Override
	public LoginResponse loginWithToken(String accountName, String rememberMeToken) {
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				   											   ServiceConfiguration.SVC_ACCOUNT_CHECK,
				   											   ServiceConfiguration.SVC_ACCOUNT_TOKEN);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, rememberMeToken);
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON)
										  .get(new GenericType<ServiceResponse<Boolean>>() { });
		
		// if the status is not OK, then something has failed
		if (sr_b.getStatus() != Status.OK) {
			
			return new LoginResponse(false, null);
		}
		
		// when the token was not in the tokenmanagaer, it's now added
		TokenManager.instance().registerToken(getSessionId(), rememberMeToken);
		
		return new LoginResponse(true, rememberMeToken);
	}

	@Override
	public AccountDetails getAccountDetails() {
		requiredLoggedIn();

		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
					ServiceConfiguration.SVC_ACCOUNT_INFO);
		
		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		ServiceResponse<AccountDetails> sr_b = wr.accept(MediaType.APPLICATION_JSON)
												.get(new GenericType<ServiceResponse<AccountDetails>>() { });
		
		if (sr_b.getStatus() != Status.OK) {
			return null;
		}

		return sr_b.getObject();
	}

	@Override
	public void storeAccountDetails(AccountDetails accountDetails) {
		requiredLoggedIn();

		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				   										ServiceConfiguration.SVC_ACCOUNT_INFO);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		wr.accept(MediaType.APPLICATION_JSON)
		  .put(new GenericType<ServiceResponse<Boolean>>() { }, accountDetails);
	}

	@Override
	public void logout() {
		requiredLoggedIn();
		
		WebResource wr = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
			   												   ServiceConfiguration.SVC_ACCOUNT_LOGOUT);

		wr = wr.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		ServiceResponse<Boolean> sr_b = wr.accept(MediaType.APPLICATION_JSON)
										  .put(new GenericType<ServiceResponse<Boolean>>() { });
		
		if (sr_b.getStatus() == Status.OK) {

			// deregister the token in the TokenManager
			TokenManager.instance().deleteToken(getToken());
			
		}
	}
}
