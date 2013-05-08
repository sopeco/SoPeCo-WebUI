package org.sopeco.webui.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.config.PersistenceConfiguration;
import org.sopeco.persistence.exceptions.WrongCredentialsException;
import org.sopeco.webui.client.rpc.AccountManagementRPC;
import org.sopeco.webui.server.helper.Security;
import org.sopeco.webui.server.persistence.FlexiblePersistenceProviderFactory;
import org.sopeco.webui.server.persistence.UiPersistence;
import org.sopeco.webui.shared.entities.account.Account;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.entities.account.RememberMeToken;
import org.sopeco.webui.shared.helper.LoginResponse;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AccountManagementRPCImpl extends SuperRemoteServlet implements AccountManagementRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountManagementRPCImpl.class);

	/** */
	private static final long serialVersionUID = 1337009456998146393L;

	@Override
	public boolean createAccount(String accountName, String password) {
		PersistenceConfiguration c = PersistenceConfiguration.getSessionSingleton(Configuration.getGlobalSessionId());
		return createAccount(accountName, password, c.getMetaDataHost(), Integer.parseInt(c.getMetaDataPort()));
	}

	@Override
	public boolean createAccount(String accountName, String password, String dbHost, int dbPort) {
		if (accountExist(accountName)) {
			LOGGER.info("It already exists an account named '{}'", accountName);
			return false;
		}

		Account account = new Account();
		account.setName(accountName);
		account.setPaswordHash(Security.sha256(password));

		account.setDbHost(dbHost);
		account.setDbPort(dbPort);
		account.setDbName(accountName);
		account.setDbPassword(Security.encrypt(password, password));

		account.setLastInteraction(-1);

		account = UiPersistence.getUiProvider().storeAccount(account);

		LOGGER.debug("Account created with id {}", account.getId());

		return true;
	}

	@Override
	public boolean accountExist(String accountName) {
		Account testIfExist = UiPersistence.getUiProvider().loadAccount(accountName);

		if (testIfExist == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkPassword(String accountName, String password) {
		Account account = UiPersistence.getUiProvider().loadAccount(accountName);

		if (account == null) {
			LOGGER.debug("Account '{}' doesn't exist.", accountName);
			return false;
		}

		String hash = Security.sha256(password);
		if (account.getPaswordHash().equals(hash)) {
			return true;
		} else {
			LOGGER.debug("Password hashes are not equal! ");
			return false;
		}
	}

	@Override
	public LoginResponse login(String accountName, String password, boolean persistentLogin) {
		LoginResponse response = new LoginResponse(false, null);
		Account account = UiPersistence.getUiProvider().loadAccount(accountName);

		if (account == null) {
			LOGGER.debug("Account '{}' doesn't exist.", accountName);
			return response;
		}
		if (!account.getPaswordHash().equals(Security.sha256(password))) {
			LOGGER.debug("Wrong password. Password hashes are not equal!");
			return response;
		}

		IPersistenceProvider persistence = null;
		try {
			String databasePassword = Security.decrypt(password, account.getDbPassword());
			if (databasePassword.isEmpty()) {
				persistence = FlexiblePersistenceProviderFactory.createPersistenceProvider(getSession(),
						account.getDbHost(), account.getDbPort() + "", account.getDbName());
			} else {
				persistence = FlexiblePersistenceProviderFactory.createPersistenceProvider(getSession(),
						account.getDbHost(), account.getDbPort() + "", account.getDbName(), databasePassword);
			}
		} catch (WrongCredentialsException e) {
			LOGGER.warn("Wrong password database credentials!");
			return response;
		}

		if (persistence == null) {
			LOGGER.warn("Connection to the database failed.");
			return response;
		}

		// Login successfull
		response.setSuccessful(true);

		if (persistentLogin) {
			String secretToken = Security.sha256(System.currentTimeMillis() + getSessionId() + accountName);
			response.setRememberMeToken(secretToken);

			RememberMeToken rememberMeToken = new RememberMeToken();
			rememberMeToken.setTokenHash(Security.sha256(secretToken));
			rememberMeToken.setAccountId(account.getId());
			rememberMeToken.setExpireTimestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7);
			rememberMeToken.setEncrypted(Security.encrypt(secretToken, password));

			UiPersistence.getUiProvider().storeRememberMeToken(rememberMeToken);
		}

		getUser().setCurrentAccount(account);
		getUser().setCurrentPersistenceProvider(persistence);

		AccountDetails details = UiPersistence.getUiProvider().loadAccountDetails(account.getId());
		if (details == null) {
			details = new AccountDetails();
			details.setId(account.getId());
			details.setAccountName(account.getName());
			UiPersistence.getUiProvider().storeAccountDetails(details);
		}

		return response;
	}

	@Override
	public LoginResponse login(String accountName, String rememberMeToken) {
		UiPersistence.getUiProvider().deleteExpiredRememberMeToken();
		LoginResponse response = new LoginResponse(false, null);

		String tokenHash = Security.sha256(rememberMeToken);
		RememberMeToken rmToken = UiPersistence.getUiProvider().loadRememberMeToken(tokenHash);

		if (rmToken == null) {
			return response;
		}

		UiPersistence.getUiProvider().removeRememberMeToken(rmToken);

		try {
			String password = Security.decrypt(rememberMeToken, rmToken.getEncrypted());
			
			return login(accountName, password, true);
		} catch (Exception e) {
			return response;
		}
	}

	@Override
	public AccountDetails getAccountDetails() {
		return getUser().getAccountDetails();
	}

	@Override
	public void storeAccountDetails(AccountDetails accountDetails) {
		UiPersistence.getUiProvider().storeAccountDetails(accountDetails);
	}
}
