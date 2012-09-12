package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.helper.handler.NumbersOnlyHandler;
import org.sopeco.frontend.client.layout.TopFilterPanel;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.frontend.shared.definitions.DatabaseDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectDBDialog extends DialogBox {

	private TopFilterPanel parentPanel;
	
	private DatabaseManagerRPCAsync dbManagerRPC;
	private TextBox textboxDbName, textboxHost, textboxPort, textboxPasswd;
	private Button btnAdd;

	public SelectDBDialog(TopFilterPanel parentPanel) {
		super(false, true);

		this.parentPanel = parentPanel;
		
		initializeGui();

		dbManagerRPC = GWT.create(DatabaseManagerRPC.class);
	}

	private void initializeGui() {
		setGlassEnabled(true);

		setText("Add new database");

		VerticalPanel verticalPanel_0 = new VerticalPanel();
		setWidget(verticalPanel_0);
		verticalPanel_0.setSize("100%", "100%");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_0.add(verticalPanel_1);
		verticalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		Grid grid = new Grid(4, 2);
		verticalPanel_1.add(grid);
		grid.setCellPadding(10);
		grid.setSize("300px", "225px");

		Label lblName = new Label("Database");
		grid.setWidget(0, 0, lblName);

		textboxDbName = new TextBox();
		grid.setWidget(0, 1, textboxDbName);

		Label lblHost = new Label("Host");
		grid.setWidget(1, 0, lblHost);

		textboxHost = new TextBox();
		grid.setWidget(1, 1, textboxHost);

		Label lblPort = new Label("Port");
		grid.setWidget(2, 0, lblPort);

		textboxPort = new TextBox();
		textboxPort.addKeyPressHandler(new NumbersOnlyHandler());
		textboxPort.addChangeHandler(new NumbersOnlyHandler());
		grid.setWidget(2, 1, textboxPort);

		Label lblPassword = new Label("Password");
		grid.setWidget(3, 0, lblPassword);
		grid.getCellFormatter().setVerticalAlignment(3, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);

		textboxPasswd = new TextBox();
		grid.setWidget(3, 1, textboxPasswd);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		verticalPanel_1.add(horizontalPanel_2);

		btnAdd = new Button("add");
		btnAdd.setEnabled(false);
		horizontalPanel_2.add(btnAdd);
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addNewDatabase();
			}
		});
		btnAdd.setWidth("150px");

		Button btnCancel = new Button("cancel");
		btnCancel.setText("cancel");
		horizontalPanel_2.add(btnCancel);
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				close();
			}
		});
		btnCancel.setWidth("150px");

		for (TextBox tb : new TextBox[] { textboxDbName, textboxHost,
				textboxPort, textboxPasswd }) {
			TextfieldHandler tfHandler = new TextfieldHandler();
			tb.addChangeHandler(tfHandler);
			tb.addKeyUpHandler(tfHandler);
		}
	}

	/*
	 * close the dialog
	 */
	private void close() {
		this.hide();
	}

	/*
	 * adding a new database
	 */
	private void addNewDatabase() {
		DatabaseDefinition newDb = new DatabaseDefinition();

		newDb.setName(textboxDbName.getText());
		newDb.setHost(textboxDbName.getText());
		newDb.setPort(textboxDbName.getText());
		newDb.setPassword(textboxDbName.getText());

		Loader.showLoader();

		dbManagerRPC.addDatabase(newDb, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();
				Message.error("Database was not added");
			}

			@Override
			public void onSuccess(Boolean result) {
				Loader.hideLoader();
				close();
				parentPanel.updateDatabaseList();
			}
		});
	}

	/*
	 * updates the database list
	 */
	/*
	 * public void updateDatabaseListbox() { int currentSelectedIndex =
	 * listboxDatabases.getSelectedIndex();
	 * 
	 * listboxDatabases.clear();
	 * 
	 * btnApply.setEnabled(false);
	 * 
	 * for (DatabaseDefinition dbDefinition : databaseList) {
	 * listboxDatabases.addItem(dbDefinition.getName() + " " +
	 * dbDefinition.getConnectionURL() + " " + dbDefinition.getId()); }
	 * 
	 * if (listboxDatabases.getItemCount() > 0)
	 * listboxDatabases.setSelectedIndex(0); }
	 */

	private class TextfieldHandler implements ChangeHandler, KeyUpHandler {
		@Override
		public void onChange(ChangeEvent event) {
			event();
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			event();
		}

		private void event() {
			if (check())
				btnAdd.setEnabled(true);
			else
				btnAdd.setEnabled(false);
		}

		private boolean check() {
			if (textboxDbName.getText().trim().length() <= 0)
				return false;
			if (textboxHost.getText().trim().length() <= 0)
				return false;
			if (textboxPort.getText().trim().length() <= 0)
				return false;

			return true;
		}
	}
}
