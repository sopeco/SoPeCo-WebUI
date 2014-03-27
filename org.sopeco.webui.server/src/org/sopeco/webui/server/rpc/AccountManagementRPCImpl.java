package org.sopeco.webui.server.rpc;

import javax.validation.constraints.Null;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.service.configuration.ServiceConfiguration;
import org.sopeco.service.persistence.entities.Account;
import org.sopeco.webui.server.persistence.UiPersistenceProvider;
import org.sopeco.webui.server.rest.ClientFactory;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.server.user.TokenManager;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.helper.LoginResponse;
import org.sopeco.webui.shared.rpc.AccountManagementRPC;

/**
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
public class AccountManagementRPCImpl extends SPCRemoteServlet implements AccountManagementRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementRPCImpl.class);

	private static final long serialVersionUID = 1337009456998146393L;

	@Override
	public boolean createAccount(String accountName, String password) {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				   										     ServiceConfiguration.SVC_ACCOUNT_CREATE);

		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		
		Response r = wt.request(MediaType.APPLICATION_JSON)
					   .post(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.warn("Creating account failed.");
			return false;
		}
		
		Account account = getAccount(getToken());
		
		if (account == null) {
			return false;
		}
		
		LOGGER.info("Creating AccountDetails for new Account.");
		
		// Create AccountDetails for own database
		AccountDetails details = UiPersistenceProvider.getInstance().loadAccountDetails(account.getId());
		if (details == null) {
			details = new AccountDetails();
			details.setId(account.getId());
			details.setAccountName(account.getName());
			UiPersistenceProvider.getInstance().storeAccountDetails(details);
		}
		
		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public boolean createAccount(String accountName, String password, String dbHost, int dbPort) {
		
		if (accountExist(accountName)) {
			return false;
		}
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
					       									 ServiceConfiguration.SVC_ACCOUNT_CREATE,
					       									 ServiceConfiguration.SVC_ACCOUNT_CUSTOMIZE);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_DATABASENAME, dbHost);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_DATABASEPORT, String.valueOf(dbPort));
		
		Response r = wt.request(MediaType.APPLICATION_JSON)
				       .post(Entity.entity(Null.class, MediaType.APPLICATION_JSON));

		if (r.getStatus() != Status.OK.getStatusCode()) {
			LOGGER.warn("Creating account failed.");
			return false;
		}
		
		Account account = getAccount(getToken());
		
		if (account == null) {
			return false;
		}

		LOGGER.info("Creating AccountDetails for new Account.");
		
		// Create AccountDetails for own database
		AccountDetails details = UiPersistenceProvider.getInstance().loadAccountDetails(account.getId());
		if (details == null) {
			details = new AccountDetails();
			details.setId(account.getId());
			details.setAccountName(account.getName());
			UiPersistenceProvider.getInstance().storeAccountDetails(details);
		}
		
		return true;
	}

	@Override
	public boolean accountExist(String accountName) {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
															 ServiceConfiguration.SVC_ACCOUNT_EXISTS);

		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		if (r.getStatus() != Status.OK.getStatusCode()) {
			return false;
		}
		
		boolean b = r.readEntity(Boolean.class);
		
		return b;
	}

	@Override
	public boolean checkPassword(String accountName, String password) {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
														     ServiceConfiguration.SVC_ACCOUNT_CHECK,
														     ServiceConfiguration.SVC_ACCOUNT_PASSWORD);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();

		return r.getStatus() == Status.OK.getStatusCode();
	}

	@Override
	public LoginResponse loginWithPassword(String accountName, String password) {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				       										 ServiceConfiguration.SVC_ACCOUNT_LOGIN);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_NAME, accountName);
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_PASSWORD, password);

		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		// if the status is not OK, then something has failed
		if (r.getStatus() != Status.OK.getStatusCode()) {
			
			return new LoginResponse(false, null);
		}

		String token = r.readEntity(String.class);
		
		Account account = getAccount(token);
		
		if (account == null) {
			LOGGER.warn("No Account to the the token '{}' exists!", token);
			return new LoginResponse(false, null);
		}
		
		// add the token to the tokenmanager
		TokenManager.instance().registerToken(getSessionId(), token, account.getId());
		
		return new LoginResponse(true, token);
	}

	/**
	 * The original idea of this method is not implemented. It's replaced with checking
	 * if the given token is valid.
	 * 
	 * The original idea was to have the user password encrypted into the given token.
	 */
	@Override
	public LoginResponse loginWithToken(String accountName, String rememberMeToken) {
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				   											 ServiceConfiguration.SVC_ACCOUNT_CHECK,
				   											 ServiceConfiguration.SVC_ACCOUNT_TOKEN);

		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, rememberMeToken);

		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		// if the status is not OK, then something has failed
		if (r.getStatus() != Status.OK.getStatusCode()) {
			return new LoginResponse(false, null);
		}
		
		Account account = getAccount(rememberMeToken);
		
		if (account == null) {
			LOGGER.warn("No Account to the the token '{}' exists!", rememberMeToken);
			return new LoginResponse(false, null);
		}
		
		// when the token was not in the tokenmanagaer, it's now added
		TokenManager.instance().registerToken(getSessionId(), rememberMeToken, account.getId());
		
		return new LoginResponse(true, rememberMeToken);
	}

	@Override
	public AccountDetails getAccountDetails() {
		requiredLoggedIn();

		Account account = getAccount(getToken());
		
		if (account == null) {
			LOGGER.warn("Unable to fetch Account from Service Layer.");
			return null;
		}
		
		return UiPersistenceProvider.getInstance().loadAccountDetails(account.getId());
	}

	@Override
	public void storeAccountDetails(AccountDetails accountDetails) {
		requiredLoggedIn();
		
		if (accountDetails == null) {
			return;
		}

		UiPersistenceProvider.getInstance().storeAccountDetails(accountDetails);
	}

	@Override
	public void logout() {
		requiredLoggedIn();
		
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
			   												 ServiceConfiguration.SVC_ACCOUNT_LOGOUT);

		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, getToken());
		
		Response r = wt.request(MediaType.APPLICATION_JSON)
					   .put(Entity.entity(Null.class, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() == Status.OK.getStatusCode()) {

			// deregister the token in the TokenManager
			TokenManager.instance().deleteToken(getToken());
			
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// HELPER /////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Fetches the Account from the Service Layer.
	 * 
	 * @param token	the token to identifiy the user, whose account is queried
	 * @return		the Account
	 */
	private Account getAccount(String token) {
		WebTarget wt = ClientFactory.getInstance().getClient(ServiceConfiguration.SVC_ACCOUNT,
				     										 ServiceConfiguration.SVC_ACCOUNT_CONNECTED);
		
		wt = wt.queryParam(ServiceConfiguration.SVCP_ACCOUNT_TOKEN, token);
		
		Response r = wt.request(MediaType.APPLICATION_JSON).get();
		
		return r.readEntity(Account.class);
	}
}
