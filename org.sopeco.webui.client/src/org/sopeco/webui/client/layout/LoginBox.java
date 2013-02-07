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
package org.sopeco.webui.client.layout;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.layout.dialog.AddDBDialog;
import org.sopeco.webui.client.layout.popups.Confirmation;
import org.sopeco.webui.client.layout.popups.InputDialog;
import org.sopeco.webui.client.layout.popups.InputDialogHandler;
import org.sopeco.webui.client.layout.popups.Loader;
import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.layout.popups.InputDialog.Icon;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.entities.AccountDetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The LoginBox, where users can create and delete or log in into accounts.
 * 
 * @author Marius Oehler
 * 
 */
public class LoginBox extends DialogBox implements ClickHandler, AsyncCallback<List<DatabaseInstance>>,
		InputDialogHandler {

	private static final String COOKIE_DATABASE = "selected_database";
	private static final String LOGIN_BOX_CSS_CLASS = "loginBox";
	private static final String VPANEL_CSS_CLASS = "vPanel";
	private static final String INTRO_IMG_CSS_CLASS = "introImage";
	private static final String HPANEL_CSS_CLASS = "hPanel";

	private static final String IMAGE_DATABASE = "images/database.png";
	private static final String IMAGE_ADD = "images/db_add.png";
	private static final String IMAGE_REMOVE = "images/db_remove.png";

	private static final Logger LOGGER = Logger.getLogger(LoginBox.class.getName());

	private Button btnConnect, btnAddDb, btnRemoveDb;
	private ListBox listboxDatabases;

	private String logInAccountAfterLoad = null;

	private InputDialog inputLogin, inputDelete;

	/**
	 * Constructor.
	 */
	public LoginBox() {
		super(false, true);
		R.resc.cssLoginBox().ensureInjected();

		initialize();

		loadDatabaseList(null);
	}

	/**
	 * Initialize the UI of this dialog.
	 */
	private void initialize() {
		setGlassEnabled(true);
		addStyleName(LOGIN_BOX_CSS_CLASS);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.addStyleName(VPANEL_CSS_CLASS);
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Image image = new Image(IMAGE_DATABASE);
		image.addStyleName(INTRO_IMG_CSS_CLASS);

		HTML htmlNewHtml = new HTML(R.get("select_account_login"));

		HorizontalPanel actionPanel = new HorizontalPanel();
		actionPanel.addStyleName(HPANEL_CSS_CLASS);
		actionPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		actionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		listboxDatabases = new ListBox();
		listboxDatabases.addItem(R.get("no_accounts"));
		listboxDatabases.setEnabled(false);
		listboxDatabases.setSelectedIndex(0);

		listboxDatabases.setSize("200px", "");
		listboxDatabases.setVisibleItemCount(1);

		btnAddDb = new Button("<img src=\"" + IMAGE_ADD + "\" />");
		btnAddDb.addStyleName("sopeco-imageButton");
		btnAddDb.addClickHandler(this);

		btnRemoveDb = new Button("<img src=\"" + IMAGE_REMOVE + "\" />");
		btnRemoveDb.addStyleName("sopeco-imageButton");
		btnRemoveDb.addClickHandler(this);

		btnConnect = new Button(R.get("connect"));
		btnConnect.setWidth("100%");
		btnConnect.addClickHandler(this);

		actionPanel.add(listboxDatabases);
		actionPanel.add(btnAddDb);
		actionPanel.add(btnRemoveDb);

		mainPanel.add(image);
		mainPanel.add(htmlNewHtml);
		mainPanel.add(actionPanel);
		mainPanel.add(btnConnect);

		setWidget(mainPanel);
	}

	/**
	 * Returns the database instance, which is related to the selected item of
	 * the listbox.
	 */
	private DatabaseInstance getSelectedDatabaseInstance() {
		int selectedIndex = listboxDatabases.getSelectedIndex();
		if (selectedIndex < 0 || selectedIndex >= Manager.get().getAvailableDatabases().size()) {
			return null;
		}
		return Manager.get().getAvailableDatabases().get(selectedIndex);
	}

	/**
	 * Loads all available databases/accounts from the server and logged into
	 * the account with the given name.
	 * 
	 * @param pLogInAccount
	 */
	public void loadDatabaseList(String pLogInAccount) {
		logInAccountAfterLoad = pLogInAccount;
		RPC.getDatabaseManagerRPC().getAllDatabases(this);
	}

	@Override
	public void onFailure(Throwable caught) {
		Message.error(R.get("faild_loading_accounts"));
	}

	@Override
	public void onSuccess(List<DatabaseInstance> result) {
		Manager.get().setAvailableDatabases(result);
		setDatabaseList(result);

		if (logInAccountAfterLoad != null) {
			setSelectedAccount(logInAccountAfterLoad);
			btnConnect.fireEvent(new ClickEvent() {
			});
			logInAccountAfterLoad = null;
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnConnect) {
			login();
		} else if (event.getSource() == btnRemoveDb) {
			deleteSelectedDatabase();
		} else if (event.getSource() == btnAddDb) {
			AddDBDialog selectDBDialog = new AddDBDialog(LoginBox.this);
			selectDBDialog.center();
		}
	}

	/**
	 * Logs the user in into the selected account.
	 */
	private void login() {
		/** code split point */
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Code download failed. Please refresh your browser window.");
			}

			@Override
			public void onSuccess() {
				GWT.log("connect to database..");

				final DatabaseInstance instance = getSelectedDatabaseInstance();
				if (instance == null) {
					return;
				}

				Manager.get().setSelectedDatabaseIndex(listboxDatabases.getSelectedIndex());

				Cookies.setCookie(COOKIE_DATABASE, instance.getDbName());

				if (instance.isProtectedByPassword()) {
					if (inputLogin == null) {
						inputLogin = new InputDialog(R.get("insert_db_passwd_for"), R.get("db_passwd") + ":", true,
								Icon.Password);
						inputLogin.addHandler(new InputDialogHandler() {
							@Override
							public void onInput(InputDialog source, String value) {
								switchDatabaseRequest(instance, value);
							}
						});
					}
					inputLogin.setValue("");
					inputLogin.center();
				} else {
					switchDatabaseRequest(instance, "");
				}
			}
		});
	}

	/**
	 * Deletes the database which is related to the given instance.
	 * 
	 * @param instance
	 *            DatabaseInstance of the database which will be deleted.
	 */
	private void deleteDatabase(final DatabaseInstance instance) {
		ClickHandler deleteConfirmed = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RPC.getDatabaseManagerRPC().removeDatabase(instance, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						LOGGER.severe(caught.getLocalizedMessage());
						Message.error(R.get("cant_delete_db"));
					}

					@Override
					public void onSuccess(Boolean result) {
						loadDatabaseList(null);
					}
				});
			}
		};

		String confText = R.get("sure_delete_db") + " <b>'" + instance.getDbName() + "'</b>?";
		Confirmation.confirm(confText, deleteConfirmed);
	}

	/**
	 * 
	 */
	private void deleteSelectedDatabase() {
		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex < 0) {
			return;
		}

		final DatabaseInstance instance = Manager.get().getAvailableDatabases().get(selectedIndex);

		if (instance == null) {
			return;
		}

		if (instance.isProtectedByPassword()) {
			if (inputDelete == null) {
				inputDelete = new InputDialog(R.get("pw_remove_db"), R.get("db_passwd") + ":", true, Icon.Password);
				inputDelete.addHandler(new InputDialogHandler() {
					@Override
					public void onInput(InputDialog source, String value) {
						RPC.getDatabaseManagerRPC().checkPassword(instance, value, new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught) {
								Message.error(caught.getMessage());
							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									deleteDatabase(instance);
								} else {
									Message.error(R.get("wrong_pw_cant_remove_db"));
								}
							}
						});
					}
				});
			}
			inputDelete.setValue("");
			inputDelete.center();
		} else {
			deleteDatabase(instance);
		}

	}

	@Override
	public void onInput(InputDialog source, String value) {
		if (source == inputDelete) {
		} else if (source == inputLogin) {

		}
	}

	/**
	 * 
	 * @param instance
	 */
	private void getAccountSettings(final DatabaseInstance instance) {
		RPC.getDatabaseManagerRPC().getAccountDetails(new AsyncCallback<AccountDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(AccountDetails result) {

				hide();
				Manager.get().setAccountDetails(result);

				SoPeCoUI.get().initializeMainView(instance);
			}
		});
	}

	/**
	 * 
	 * @param databases
	 */
	private void setDatabaseList(List<DatabaseInstance> databases) {
		listboxDatabases.clear();

		if (databases.isEmpty()) {
			listboxDatabases.setEnabled(false);
			btnRemoveDb.setEnabled(false);

			listboxDatabases.addItem(R.get("no_accounts"));
		} else {
			listboxDatabases.setEnabled(true);
			btnRemoveDb.setEnabled(true);

			for (DatabaseInstance dbInstance : databases) {
				String name = dbInstance.getDbName();

				if (dbInstance.isProtectedByPassword()) {
					name = "* " + name;
				}

				listboxDatabases.addItem(name);
			}

			String cookie = Cookies.getCookie(COOKIE_DATABASE);
			if (cookie != null) {
				setSelectedAccount(cookie);
			}
		}
	}

	/**
	 * Sets the selected item in the listboxDatabases to the given
	 * String/Item/Name.
	 * 
	 * @param name
	 *            String which will be selected
	 */
	private void setSelectedAccount(String name) {
		for (int i = 0; i < listboxDatabases.getItemCount(); i++) {
			if (listboxDatabases.getItemText(i).equals(name) || listboxDatabases.getItemText(i).equals("* " + name)) {
				listboxDatabases.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * 
	 * @param instance
	 * @param password
	 */
	private void switchDatabaseRequest(final DatabaseInstance instance, final String password) {
		Loader.showLoader();
		RPC.getDatabaseManagerRPC().login(instance, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();

				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				Loader.hideLoader();

				if (result) {
					getAccountSettings(instance);
				} else {
					Message.error(R.get("wrong_db_credentials"));
				}
			}
		});
	}
}
