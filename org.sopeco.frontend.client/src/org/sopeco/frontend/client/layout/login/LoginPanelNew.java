package org.sopeco.frontend.client.layout.login;

import java.util.List;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.AccountDetails;
import org.sopeco.frontend.shared.helper.Utilities;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class LoginPanelNew extends FlowPanel implements AsyncCallback<List<DatabaseInstance>>, ClickHandler {

	public static final String COOKIE_DATABASE = "selected_database";

	private SimplePanel verticalCell;

	private SelectAccountPanel selectAccountPanel;
	private AddAccountPanel addAccountPanel;

	private HTML htmlFEVersionInfo;

	/**
	 * Cosntructor.
	 */
	public LoginPanelNew() {
		init();

		fetchAccounts();
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		R.resc.cssLoginBox().ensureInjected();
		addStyleName("loginPanel");

		htmlFEVersionInfo = new HTML(FrontendEntryPoint.getBuildInfo());
		htmlFEVersionInfo.addStyleName("htmlFEVersionInfo");

		Image imgSapResearch = new Image(R.resc.imgSapResearchGrayOrange());
		imgSapResearch.addStyleName("imgSapResearch");

		verticalCell = new SimplePanel();

		selectAccountPanel = new SelectAccountPanel();
		selectAccountPanel.getBtnConnect().addClickHandler(this);
		selectAccountPanel.getBtnAddAccount().addClickHandler(this);

		addAccountPanel = new AddAccountPanel();
		addAccountPanel.getBtnCancel().addClickHandler(this);
		addAccountPanel.getBtnAddAccount().addClickHandler(this);

		verticalCell.add(selectAccountPanel);
		add(verticalCell);
		add(htmlFEVersionInfo);
		add(imgSapResearch);
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Errorhandling
	}

	@Override
	public void onSuccess(List<DatabaseInstance> result) {
		selectAccountPanel.updateAccountList(result);
	}

	/**
	 * Fetches the available accounts from the server.
	 */
	private void fetchAccounts() {
		RPC.getDatabaseManagerRPC().getAllDatabases(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == selectAccountPanel.getBtnConnect()) {
			// Connect to the selected account if accounts are available
			if (Manager.get().getAvailableDatabases() != null && !Manager.get().getAvailableDatabases().isEmpty()) {
				selectAccountPanel.setIsLogginIn(true);
				login();
			}
		} else if (event.getSource() == selectAccountPanel.getBtnAddAccount()) {
			verticalCell.clear();
			addAccountPanel.reset();
			verticalCell.add(addAccountPanel);
		} else if (event.getSource() == addAccountPanel.getBtnCancel()) {
			verticalCell.clear();
			verticalCell.add(selectAccountPanel);
		} else if (event.getSource() == addAccountPanel.getBtnAddAccount()) {
			if (addAccountPanel.formValid()) {
				addAccount();
			}
		}
	}

	/**
	 * Returns the DatabaseInstance object which is related to the selected
	 * account.
	 * 
	 * @return DatabaseInstance of the selected account
	 */
	private DatabaseInstance getSelectedAccount() {
		return Manager.get().getAvailableDatabases().get(selectAccountPanel.getCbAccounts().getSelectedIndex());
	}

	private void login() {

		final DatabaseInstance instance = getSelectedAccount();
		Manager.get().setSelectedDatabaseIndex(selectAccountPanel.getCbAccounts().getSelectedIndex());
		Cookies.setCookie(COOKIE_DATABASE, instance.getDbName());

		if (instance.isProtectedByPassword()) {
			// TODO
			// if (inputLogin == null) {
			// inputLogin = new InputDialog(R.get("insert_db_passwd_for"),
			// R.get("db_passwd") + ":", true,
			// Icon.Password);
			// inputLogin.addHandler(new InputDialogHandler() {
			// @Override
			// public void onInput(InputDialog source, String value) {
			// switchDatabaseRequest(instance, value);
			// }
			// });
			// }
			// inputLogin.setValue("");
			// inputLogin.center();
		} else {
			loginIntoAccount(instance, "");
		}
	}

	private void loginIntoAccount(final DatabaseInstance account, final String password) {
		/** code split point */
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				// TODO error handling
				Window.alert(R.lang.codeDownloadFailed());
			}

			@Override
			public void onSuccess() {
				RPC.getDatabaseManagerRPC().selectDatabase(account, password, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Message.error(caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {

						if (result) {
							getAccountSettings(account);
						} else {
							Message.error(R.get("wrong_db_credentials"));
						}
					}
				});
			}
		});
	}

	private void getAccountSettings(final DatabaseInstance instance) {
		RPC.getDatabaseManagerRPC().getAccountDetails(new AsyncCallback<AccountDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(AccountDetails result) {

				Manager.get().setAccountDetails(result);
				FrontendEntryPoint.get().initializeMainView(instance);
			}
		});
	}

	private void addAccount() {

		DatabaseInstance newAccount = new DatabaseInstance();

		final String accountName = Utilities.cleanString(addAccountPanel.getTbName().getText());

		newAccount.setDbName(accountName);
		newAccount.setHost(addAccountPanel.getTbDatabaseHost().getText());
		newAccount.setPort(addAccountPanel.getTbDatabasePort().getText());

		String password;
		if (addAccountPanel.getTbPassword().getText().isEmpty()) {
			newAccount.setProtectedByPassword(false);
			password = "";
		} else {
			newAccount.setProtectedByPassword(true);
			password = addAccountPanel.getTbPassword().getText();
		}

		RPC.getDatabaseManagerRPC().addDatabase(newAccount, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error("Database was not added: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				verticalCell.clear();
				verticalCell.add(selectAccountPanel);
				fetchAccounts();
			}
		});

	}
}
