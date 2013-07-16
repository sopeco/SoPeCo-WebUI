package org.sopeco.webui.server.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.config.Configuration;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.config.PersistenceConfiguration;
import org.sopeco.persistence.exceptions.WrongCredentialsException;
import org.sopeco.webui.server.persistence.FlexiblePersistenceProviderFactory;
import org.sopeco.webui.server.persistence.UiPersistence;
import org.sopeco.webui.server.rpc.servlet.SPCRemoteServlet;
import org.sopeco.webui.server.security.Crypto;
import org.sopeco.webui.server.user.UserManager;
import org.sopeco.webui.shared.entities.account.Account;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.entities.account.RememberMeToken;
import org.sopeco.webui.shared.helper.LoginResponse;
import org.sopeco.webui.shared.rpc.AccountManagementRPC;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AccountManagementRPCImpl extends SPCRemoteServlet implements AccountManagementRPC {

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
		account.setPaswordHash(Crypto.sha256(password));

		account.setDbHost(dbHost);
		account.setDbPort(dbPort);
		account.setDbName(accountName);
		account.setDbPassword(Crypto.encrypt(password, password));

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

		String hash = Crypto.sha256(password);
		if (account.getPaswordHash().equals(hash)) {
			return true;
		} else {
			LOGGER.debug("Password hashes are not equal! ");
			return false;
		}
	}

	@Override
	public LoginResponse loginWithPassword(String accountName, String password) {
		LoginResponse response = new LoginResponse(false, null);
		Account account = UiPersistence.getUiProvider().loadAccount(accountName);

		if (account == null) {
			LOGGER.debug("Account '{}' doesn't exist.", accountName);
			return response;
		}
		if (!account.getPaswordHash().equals(Crypto.sha256(password))) {
			LOGGER.debug("Wrong password. Password hashes are not equal!");
			return response;
		}

		IPersistenceProvider persistence = null;
		try {
			String databasePassword = Crypto.decrypt(password, account.getDbPassword());
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

		// Create token to remeber login for later sessions
		String secretToken = Crypto.sha256(System.currentTimeMillis() + getSessionId() + accountName);
		response.setRememberMeToken(secretToken);

		RememberMeToken rememberMeToken = new RememberMeToken();
		rememberMeToken.setTokenHash(Crypto.sha256(secretToken));
		rememberMeToken.setAccountId(account.getId());
		rememberMeToken.setExpireTimestamp(System.currentTimeMillis() + 1000 * 3600 * 24 * 7);
		rememberMeToken.setEncrypted(Crypto.encrypt(secretToken, password));

		UiPersistence.getUiProvider().storeRememberMeToken(rememberMeToken);

		// store loged in user
		UserManager.instance().getAllUsers();
		UserManager.instance().registerUser(getSessionId());

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
	public LoginResponse loginWithToken(String accountName, String rememberMeToken) {
		UiPersistence.getUiProvider().deleteExpiredRememberMeToken();
		LoginResponse response = new LoginResponse(false, null);

		String tokenHash = Crypto.sha256(rememberMeToken);
		RememberMeToken rmToken = UiPersistence.getUiProvider().loadRememberMeToken(tokenHash);

		if (rmToken == null) {
			return response;
		}

		UiPersistence.getUiProvider().removeRememberMeToken(rmToken);

		try {
			String password = Crypto.decrypt(rememberMeToken, rmToken.getEncrypted());

			return loginWithPassword(accountName, password);
		} catch (Exception e) {
			return response;
		}
	}

	@Override
	public AccountDetails getAccountDetails() {
		requiredLoggedIn();

		return getUser().getAccountDetails();
	}

	@Override
	public void storeAccountDetails(AccountDetails accountDetails) {
		requiredLoggedIn();

		UiPersistence.getUiProvider().storeAccountDetails(accountDetails);
	}

	@Override
	public void logout() {
		requiredLoggedIn();

		UserManager.instance().destroyUser(getUser());
	}
}
