package org.sopeco.frontend.client.layout;

import java.util.List;

import org.sopeco.frontend.client.FrontendEntryPoint;
import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.entities.AccountDetails;
import org.sopeco.frontend.client.helper.DBManager;
import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.helper.serverstatus.Deactivatable;
import org.sopeco.frontend.client.helper.serverstatus.Serverstatus;
import org.sopeco.frontend.client.layout.dialog.AddDBDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInput.Icon;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

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
 * 
 * @author Marius Oehler
 * 
 */
public class LoginBox extends DialogBox implements ClickHandler, Deactivatable {

	private static final String COOKIE_DATABSE = "selected_database";

	private ListBox listboxDatabases;
	private LoginBox myself;
	private Button btnConnect, btnAddDb, btnRemoveDb;

	private FrontendEntryPoint parentModule;

	public LoginBox(FrontendEntryPoint parent) {
		super(false, true);

		myself = this;
		parentModule = parent;

		Serverstatus.register(this);

		initialize();

		loadDatabaseList();
	}

	private void initialize() {
		setGlassEnabled(true);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(8);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel_1);

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(8);
		verticalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel_1.add(verticalPanel_1);

		Image image = new Image("images/database.png");
		verticalPanel_1.add(image);
		image.setHeight("128px");

		R.get("");

		HTML htmlNewHtml = new HTML("<b style=\"margin-left:12px\">" + R.get("select_account_login") + "</b>", true);
		verticalPanel_1.add(htmlNewHtml);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);

		listboxDatabases = new ListBox();
		listboxDatabases.addItem(R.get("no_accounts"));
		listboxDatabases.setEnabled(false);
		listboxDatabases.setSelectedIndex(0);
		horizontalPanel.add(listboxDatabases);
		listboxDatabases.setSize("200px", "");
		listboxDatabases.setVisibleItemCount(1);

		btnAddDb = new Button("<img src=\"images/db_add.png\" />");
		btnAddDb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AddDBDialog selectDBDialog = new AddDBDialog(myself);

				selectDBDialog.center();
			}
		});
		horizontalPanel.add(btnAddDb);
		btnAddDb.setStyleName("sopeco-imageButton", true);

		btnRemoveDb = new Button("<img src=\"images/db_remove.png\" />");
		horizontalPanel.add(btnRemoveDb);
		btnRemoveDb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteSelectedDatabase();
			}
		});

		btnConnect = new Button(R.get("connect"));
		btnConnect.addClickHandler(this);
		verticalPanel.add(btnConnect);
		btnConnect.setWidth("100%");
		btnRemoveDb.setStyleName("sopeco-imageButton", true);
	}

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

			String cookie = Cookies.getCookie(COOKIE_DATABSE);
			if (cookie != null) {
				for (int i = 0; i < databases.size() && i < listboxDatabases.getItemCount(); i++) {
					if (databases.get(i).getDbName().equals(cookie)) {
						listboxDatabases.setSelectedIndex(i);
					}
				}
			}
		}
	}

	public void loadDatabaseList() {
		Loader.showLoader();

		DBManager.loadDatabases(new INotifyHandler<List<DatabaseInstance>>() {
			@Override
			public void call(boolean success, List<DatabaseInstance> result) {
				if (success) {
					setDatabaseList(result);

					// TODO
					if (FrontendEntryPoint.DEVELOPMENT) {
						btnConnect.fireEvent(new ClickEvent() {
						});
					}

				} else {
					Serverstatus.setOffline();

					Message.error(R.get("faild_loading_accounts"));
				}

				Loader.hideLoader();
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Code download failed");
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub

				GWT.log("connect to database..");

				int selectedIndex = listboxDatabases.getSelectedIndex();

				if (selectedIndex < 0) {
					Message.warning(R.get("select_account"));
					return;
				}

				final DatabaseInstance instance = DBManager.getLoadedDatabases().get(selectedIndex);

				if (instance == null) {
					return;
				}

				Cookies.setCookie(COOKIE_DATABSE, instance.getDbName());

				if (instance.isProtectedByPassword()) {
					TextInput.doInput(Icon.Password, R.get("insert_db_passwd_for") + " '" + instance.getDbName() + "'",
							R.get("db_passwd") + ":", new TextInputOkHandler() {
								@Override
								public void onInput(ClickEvent event, String input) {
									switchDatabaseRequest(instance, input);
								}
							});
				} else {
					switchDatabaseRequest(instance, "");
				}
			}
		});
	}

	private void switchDatabaseRequest(final DatabaseInstance instance, final String password) {
		Loader.showLoader();
		DBManager.getDbManager().selectDatabase(instance, password, new AsyncCallback<Boolean>() {
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
//					hide();
//					parentModule.initializeMainView(instance);
				} else {
					Message.error(R.get("wrong_db_credentials"));
				}
			}
		});
	}

	private void getAccountSettings(final DatabaseInstance instance) {
		DBManager.getDbManager().getAccountDetails(new AsyncCallback<AccountDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(AccountDetails result) {
				hide();
				Manager.get().setAccountDetails(result);
				parentModule.initializeMainView(instance);
			}
		});
	}

	private void deleteSelectedDatabase() {
		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex < 0) {
			return;
		}

		final DatabaseInstance instance = DBManager.getLoadedDatabases().get(selectedIndex);

		if (instance == null) {
			return;
		}

		if (instance.isProtectedByPassword()) {
			TextInput.doInput(Icon.Password, R.get("pw_remove_db"), R.get("db_passwd") + ":", new TextInputOkHandler() {
				@Override
				public void onInput(ClickEvent event, String input) {
					DBManager.getDbManager().checkPassword(instance, input, new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								deleteDatabase(instance);
							} else {
								Message.error(R.get("wrong_pw_cant_remove_db"));
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							Message.error(caught.getMessage());
						}
					});
				}
			});
		} else {
			deleteDatabase(instance);
		}

	}

	private void deleteDatabase(final DatabaseInstance instance) {
		ClickHandler deleteConfirmed = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Loader.showLoader();

				DBManager.getDbManager().removeDatabase(instance, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Loader.hideLoader();

						Message.error(R.get("cant_delete_db"));
					}

					@Override
					public void onSuccess(Boolean result) {
						Loader.hideLoader();

						loadDatabaseList();
					}
				});
			}
		};

		String confText = R.get("sure_delete_db") + " <b>'" + instance.getDbName() + "'</b>?";

		Confirmation.confirm(confText, deleteConfirmed);
	}

	@Override
	public void goOffline() {
		btnAddDb.setEnabled(false);
		btnRemoveDb.setEnabled(false);
		btnConnect.setEnabled(false);
		listboxDatabases.setEnabled(false);
	}

	@Override
	public void goOnline() {
		btnAddDb.setEnabled(true);
		btnRemoveDb.setEnabled(true);
		btnConnect.setEnabled(true);
		listboxDatabases.setEnabled(true);
	}
}
