/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout.login;

import java.util.Date;
import java.util.List;

import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.entities.AccountDetails;
import org.sopeco.webui.shared.helper.Utilities;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class LoginPanel extends FlowPanel implements ClickHandler {

	public static final String COOKIE_DATABASE = "selected_database";

	private static final long COOKIE_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

	private SimplePanel verticalCell;

	private SelectAccountPanel selectAccountPanel;
	private AddAccountPanel addAccountPanel;
	private DeleteAccountPanel deleteAccountPanel;
	private PasswordLoginPanel passwordLoginPanel;

	private HTML htmlFEVersionInfo;

	private FlowPanel selectLanguagePanel;
	private FlowPanel logoPanel ;
	
	/**
	 * Cosntructor.
	 */
	public LoginPanel() {
		init();

		fetchAccounts();
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		R.resc.cssLoginBox().ensureInjected();
		addStyleName("loginPanel");

		htmlFEVersionInfo = new HTML(SoPeCoUI.getBuildInfo());
		htmlFEVersionInfo.addStyleName("htmlFEVersionInfo");

		Image imgLogo = new Image(R.resc.imgSDQLogo().getSafeUri());

		logoPanel = new FlowPanel();
		logoPanel.addStyleName("imgSapResearch");
		logoPanel.add(new HTML("powered by"));
		logoPanel.add(imgLogo);
		
		createLanguagePanel();

		verticalCell = new SimplePanel();

		selectAccountPanel = new SelectAccountPanel();
		selectAccountPanel.getBtnConnect().addClickHandler(this);
		selectAccountPanel.getBtnAddAccount().addClickHandler(this);
		selectAccountPanel.getBtnRemoveAccount().addClickHandler(this);

		addAccountPanel = new AddAccountPanel();
		addAccountPanel.getBtnCancel().addClickHandler(this);
		addAccountPanel.getBtnAddAccount().addClickHandler(this);

		deleteAccountPanel = new DeleteAccountPanel();
		deleteAccountPanel.getBtnCancel().addClickHandler(this);
		deleteAccountPanel.getBtnDelete().addClickHandler(this);

		passwordLoginPanel = new PasswordLoginPanel();
		passwordLoginPanel.getBtnCancel().addClickHandler(this);
		passwordLoginPanel.getBtnContinue().addClickHandler(this);

		verticalCell.add(selectAccountPanel);
		add(verticalCell);
		add(htmlFEVersionInfo);
		add(logoPanel);
		add(selectLanguagePanel);
	}

	private void createLanguagePanel() {
		selectLanguagePanel = new FlowPanel();
		selectLanguagePanel.addStyleName("selectLanguage");

		selectLanguagePanel.add(new HTML(R.lang.selectLanguage()));
		selectLanguagePanel.add(new Anchor(AbstractImagePrototype.create(R.resc.imgFlagEn()).getSafeHtml(),
				"frontend.jsp"));
		selectLanguagePanel.add(new Anchor(AbstractImagePrototype.create(R.resc.imgFlagDe()).getSafeHtml(),
				"frontend.jsp?locale=de"));
	}

	/**
	 * Fetches the available accounts from the server.
	 */
	private void fetchAccounts() {
		fetchAccounts(null);
	}

	private void fetchAccounts(final String setSelectedAccount) {
		fetchAccounts(setSelectedAccount, false);
	}

	private void fetchAccounts(final String setSelectedAccount, final boolean login) {
		RPC.getDatabaseManagerRPC().getAllDatabases(new AsyncCallback<List<DatabaseInstance>>() {
			@Override
			public void onSuccess(List<DatabaseInstance> result) {
				selectAccountPanel.updateAccountList(result);

				if (setSelectedAccount != null) {
					selectAccountPanel.getCbAccounts().setSelectedText(setSelectedAccount);
				}

				if (login) {
					login();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Errorhandling
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == selectAccountPanel.getBtnConnect()) {
			// Connect to the selected account if accounts are available
			if (Manager.get().getAvailableDatabases() != null && !Manager.get().getAvailableDatabases().isEmpty()) {
				login();
			}
		} else if (event.getSource() == selectAccountPanel.getBtnAddAccount()) {
			verticalCell.clear();
			addAccountPanel.reset();
			verticalCell.add(addAccountPanel);
			addAccountPanel.getTbName().setFocus(true);
		} else if (event.getSource() == addAccountPanel.getBtnCancel()) {
			verticalCell.clear();
			verticalCell.add(selectAccountPanel);
		} else if (event.getSource() == addAccountPanel.getBtnAddAccount()) {
			if (addAccountPanel.formValid()) {
				addAccount();
			}
		} else if (event.getSource() == selectAccountPanel.getBtnRemoveAccount()) {
			verticalCell.clear();
			deleteAccountPanel.setAccountInfos(getSelectedAccount());
			verticalCell.add(deleteAccountPanel);
			deleteAccountPanel.getTbPassword().setFocus(true);
		} else if (event.getSource() == deleteAccountPanel.getBtnCancel()) {
			verticalCell.clear();
			verticalCell.add(selectAccountPanel);
		} else if (event.getSource() == deleteAccountPanel.getBtnDelete()) {
			delete();
		} else if (event.getSource() == passwordLoginPanel.getBtnCancel()) {
			verticalCell.clear();
			verticalCell.add(selectAccountPanel);
		} else if (event.getSource() == passwordLoginPanel.getBtnContinue()) {
			loginCheckPassword();
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

	private void delete() {
		final DatabaseInstance account = getSelectedAccount();

		if (account.isProtectedByPassword()) {
			RPC.getDatabaseManagerRPC().checkPassword(account, deleteAccountPanel.getTbPassword().getText(),
					new AsyncCallback<Boolean>() {
						@Override
						public void onFailure(Throwable caught) {
							Message.error(caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								deleteAccount(account);
							} else {
								deleteAccountPanel.getHtmlWrongPassword().setVisible(true);
								deleteAccountPanel.getTbPassword().setText("");
							}
						}
					});
		} else {
			deleteAccount(account);
		}
	}

	private void deleteAccount(DatabaseInstance account) {
		RPC.getDatabaseManagerRPC().removeDatabase(account, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(R.get("cant_delete_db"));
			}

			@Override
			public void onSuccess(Boolean result) {
				verticalCell.clear();
				verticalCell.add(selectAccountPanel);
				fetchAccounts();
			}
		});
	}

	private void login() {
		final DatabaseInstance account = getSelectedAccount();
		Manager.get().setSelectedDatabaseIndex(selectAccountPanel.getCbAccounts().getSelectedIndex());

		Date expireDate = new Date();
		long sevenDaysInFuture = expireDate.getTime() + COOKIE_EXPIRE_TIME;
		expireDate.setTime(sevenDaysInFuture);

		Cookies.setCookie(COOKIE_DATABASE, account.getDbName(), expireDate);

		if (account.isProtectedByPassword()) {
			verticalCell.clear();
			passwordLoginPanel.setAccountInfos(account);
			verticalCell.add(passwordLoginPanel);
			passwordLoginPanel.getTbPassword().setFocus(true);
		} else {
			loginIntoAccount(account, "");
		}

	}

	private void loginCheckPassword() {
		final DatabaseInstance account = getSelectedAccount();
		final String password = passwordLoginPanel.getTbPassword().getText();
		RPC.getDatabaseManagerRPC().checkPassword(account, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					loginIntoAccount(account, password);
				} else {
					passwordLoginPanel.getHtmlWrongPassword().setVisible(true);
					passwordLoginPanel.getTbPassword().setText("");
				}
			}
		});
	}

	private void loginIntoAccount(final DatabaseInstance account, final String password) {
		selectAccountPanel.setIsLogginIn(true);

		/** code split point */
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				// TODO error handling
				Window.alert(R.lang.codeDownloadFailed());
			}

			@Override
			public void onSuccess() {
				RPC.getDatabaseManagerRPC().login(account, password, new AsyncCallback<Boolean>() {
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
				SoPeCoUI.get().initializeMainView(instance);
			}
		});
	}

	private void addAccount() {

		final DatabaseInstance newAccount = new DatabaseInstance();
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

				String selectAccount = accountName;
				if (newAccount.isProtectedByPassword()) {
					selectAccount = "* " + selectAccount;
				}

				fetchAccounts(selectAccount, true);
			}
		});

	}
}
