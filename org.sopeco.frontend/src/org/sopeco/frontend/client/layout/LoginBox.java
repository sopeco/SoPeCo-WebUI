package org.sopeco.frontend.client.layout;

import java.util.List;

import org.sopeco.frontend.client.helper.DBManager;
import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.layout.dialog.AddDBDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

public class LoginBox extends DialogBox implements ClickHandler {

	private ListBox listboxDatabases;
	private LoginBox myself;
	
	public LoginBox() {
		super(false, true);
		
		myself = this;

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
		image.setWidth("");

		HTML htmlNewHtml = new HTML("<b style=\"margin-left:12px\">Select Account for SoPeCo Web-FrontEnd</b>", true);
		verticalPanel_1.add(htmlNewHtml);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);

		listboxDatabases = new ListBox();
		listboxDatabases.addItem("--SELECT--");
		listboxDatabases.setSelectedIndex(0);
		horizontalPanel.add(listboxDatabases);
		listboxDatabases.setSize("200px", "");
		listboxDatabases.setVisibleItemCount(1);

		Button btnAddDb = new Button("<img src=\"images/db_add.png\" />");
		btnAddDb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AddDBDialog selectDBDialog = new AddDBDialog(myself);

				selectDBDialog.center();
			}
		});
		horizontalPanel.add(btnAddDb);
		btnAddDb.setStyleName("sopeco-imageButton", true);

		Button btnRemoveDb = new Button("<img src=\"images/db_remove.png\" />");
		horizontalPanel.add(btnRemoveDb);
		btnRemoveDb.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteSelectedDatabase();
			}
		});

		Button btnConnect = new Button("connect");
		btnConnect.addClickHandler(this);
		verticalPanel.add(btnConnect);
		btnConnect.setWidth("100%");
		btnRemoveDb.setStyleName("sopeco-imageButton", true);
	}

	private void setDatabaseList(List<DatabaseInstance> databases) {
		listboxDatabases.clear();

		listboxDatabases.addItem("--SELECT--");

		for (DatabaseInstance dbInstance : databases) {
			String name = dbInstance.getDbName();

			if (dbInstance.isProtectedByPassword()) {
				name = "* " + name;
			}

			listboxDatabases.addItem(name);
		}
	}

	public void loadDatabaseList() {
		Loader.showLoader();

		DBManager.loadDatabases(new INotifyHandler<List<DatabaseInstance>>() {
			@Override
			public void call(boolean success, List<DatabaseInstance> result) {
				if (success) {
					setDatabaseList(result);
				} else {
					Message.error("Failed loading accounts..");
				}

				Loader.hideLoader();
			}
		});
	}

	@Override
	public void onClick(ClickEvent event) {
		GWT.log("connect to database..");

		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex <= 0) {
			Message.warning("Please select a account.");
			return;
		}

		selectedIndex--;

		final DatabaseInstance instance = DBManager.getLoadedDatabases().get(selectedIndex);

		if (instance == null) {
			return;
		}

		if (instance.isProtectedByPassword()) {
			TextInput.doInput(TextInput.ICO_PASSWORD, "Insert database password for '" + instance.getDbName() + "'",
					"Database password:", new TextInputOkHandler() {
						@Override
						public void onClick(ClickEvent event, String input) {
							switchDatabaseRequest(instance, input);
						}
					});
		} else {
			switchDatabaseRequest(instance, "");
		}

	}

	private void switchDatabaseRequest(DatabaseInstance instance, String password) {
		Loader.showLoader();
		DBManager.getDbManager().selectDatabase(instance, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();

				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {

				hide();

				Loader.hideLoader();
			}
		});
	}
	
	private void deleteSelectedDatabase() {
		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex <= 0 ) {
			return;
		}

		selectedIndex--;
		
		final DatabaseInstance instance = DBManager.getLoadedDatabases().get(selectedIndex);

		if (instance == null) {
			return;
		}

		ClickHandler deleteConfirmed = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Loader.showLoader();

				DBManager.getDbManager().removeDatabase(instance, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Loader.hideLoader();

						Message.error("Can't delete selected database");
					}

					@Override
					public void onSuccess(Boolean result) {
						Loader.hideLoader();

						loadDatabaseList();
					}
				});
			}
		};

		String confText = "Are you sure you want to delete the database <b>'" + instance.getDbName() + "'</b>?";

		Confirmation.confirm(confText, deleteConfirmed);
	}

}
