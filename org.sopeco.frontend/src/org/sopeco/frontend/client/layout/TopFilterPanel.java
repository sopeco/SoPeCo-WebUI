package org.sopeco.frontend.client.layout;

import java.util.List;

import org.sopeco.frontend.client.layout.dialog.AddScenarioDialog;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.TextInput;
import org.sopeco.frontend.client.layout.popups.TextInputOkHandler;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.frontend.client.rpc.ScenarioManagerRPC;
import org.sopeco.frontend.client.rpc.ScenarioManagerRPCAsync;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class TopFilterPanel extends DecoratorPanel implements ChangeHandler {

	private TopFilterPanel thisPanel;
	private DatabaseManagerRPCAsync dbManager;
	private ScenarioManagerRPCAsync scenarioManager;
	private List<DatabaseInstance> currentDatabaseList;

	private ListBox listboxDatabases, listboxScenarios;

	public TopFilterPanel() {
		thisPanel = this;

		dbManager = GWT.create(DatabaseManagerRPC.class);
		scenarioManager = GWT.create(ScenarioManagerRPC.class);

		initialize();
	}

	/*
	 * init the gui
	 */
	private void initialize() {
		setWidth("100%");

		FlowPanel floatlPanel = new FlowPanel();

		floatlPanel.setStyleName("topFilterPanel");
		setWidget(floatlPanel);
		floatlPanel.setSize("100%", "");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		floatlPanel.add(horizontalPanel);
		floatlPanel.add(new Label("connected to account XXX"));
		
		Label lblSelectDatabase = new Label("select database:");
		horizontalPanel.add(lblSelectDatabase);
		lblSelectDatabase.setWidth("110px");

		listboxDatabases = new ListBox();
		listboxDatabases.addChangeHandler(this);
		horizontalPanel.add(listboxDatabases);
		listboxDatabases.setWidth("120px");
		listboxDatabases.setVisibleItemCount(1);

		Button btnAddDatabase = new Button("<img src=\"images/db_add.png\" />");
		btnAddDatabase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
//				AddDBDialog selectDBDialog = new AddDBDialog(thisPanel);
//
//				selectDBDialog.center();
			}
		});
		btnAddDatabase.setStyleName("sopeco-imageButton", true);
		horizontalPanel.add(btnAddDatabase);

		Button btnRemoveDatabase = new Button("<img src=\"images/db_remove.png\" />");
		btnRemoveDatabase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				deleteSelectedDatabase();
			}
		});
		btnRemoveDatabase.setStyleName("sopeco-imageButton", true);
		horizontalPanel.add(btnRemoveDatabase);

		HTML htmlNewHtml = new HTML("", true);
		htmlNewHtml.setStyleName("spc-seperator");
		floatlPanel.add(htmlNewHtml);
		htmlNewHtml.setHeight("50px");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(10);
		horizontalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		floatlPanel.add(horizontalPanel_1);

		Label lblSelectScenario = new Label("select scenario");
		horizontalPanel_1.add(lblSelectScenario);
		lblSelectScenario.setWidth("110px");

		listboxScenarios = new ListBox();
		listboxScenarios.setEnabled(false);
		horizontalPanel_1.add(listboxScenarios);
		listboxScenarios.setWidth("120px");
		listboxScenarios.setVisibleItemCount(1);

		Button btnAddScenario = new Button("<img src=\"images/add.png\" />");
		btnAddScenario.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				AddScenarioDialog addScenario = new AddScenarioDialog();

				addScenario.center();
			}
		});
		btnAddScenario.setStyleName("sopeco-imageButton", true);
		horizontalPanel_1.add(btnAddScenario);

		Button btnRemoveScenario = new Button("<img src=\"images/remove.png\" />");
		btnRemoveScenario.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			}
		});
		btnRemoveScenario.setStyleName("sopeco-imageButton", true);
		horizontalPanel_1.add(btnRemoveScenario);

		updateDatabaseList();
	}

	/*
	 * loading the latest database list an update the listbox
	 */
	public void updateDatabaseList() {
		updateDatabaseList(false);
	}

	/*
	 * delete selected database
	 */
	private void deleteSelectedDatabase() {
		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex <= 0 )
			return;

		selectedIndex--;
		
		final DatabaseInstance instance = currentDatabaseList.get(selectedIndex);

		if (instance == null)
			return;

		ClickHandler deleteConfirmed = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Loader.showLoader();

				dbManager.removeDatabase(instance, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Loader.hideLoader();

						Message.error("Can't delete selected database");
					}

					@Override
					public void onSuccess(Boolean result) {
						Loader.hideLoader();

						updateDatabaseList();
					}
				});
			}
		};

		String confText = "Are you sure you want to delete the database <b>'" + instance.getDbName() + "'</b>?";

		Confirmation.confirm(confText, deleteConfirmed);
	}

	/*
	 * loading the latest database list, update the listbox and set the
	 * selection on the last item
	 */
	public void updateDatabaseList(final boolean selectLast) {
		Loader.showLoader();

		dbManager.getAllDatabases(new AsyncCallback<List<DatabaseInstance>>() {
			@Override
			public void onSuccess(List<DatabaseInstance> result) {
				listboxDatabases.clear();
				
				listboxDatabases.addItem("--select--");
				
				currentDatabaseList = result;

				for (DatabaseInstance dbInstance : result) {
					String name = dbInstance.getDbName();

					if (dbInstance.isProtectedByPassword())
						name = "* " + name;

					listboxDatabases.addItem(name);
				}

				if (selectLast && listboxDatabases.getItemCount() > 1)
					listboxDatabases.setSelectedIndex(listboxDatabases.getItemCount() - 1);

				switchDatabase();

				Loader.hideLoader();
			}

			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();

				Message.error(caught.getMessage());

				throw new RuntimeException(caught);
			}
		});
	}

	@Override
	public void onChange(ChangeEvent event) {
		if (event.getSource() == listboxDatabases) {
			switchDatabase();
		}
	}

	/*
	 * database was changed
	 */
	private void switchDatabase() {
		GWT.log("switch database");

		int selectedIndex = listboxDatabases.getSelectedIndex();

		if (selectedIndex <= 0 )
			return;
		
		selectedIndex--;

		final DatabaseInstance instance = currentDatabaseList.get(selectedIndex);

		if (instance == null)
			return;

		if (instance.isProtectedByPassword()) {
			TextInput.doInput(TextInput.ICO_PASSWORD, "Insert database password for '" + instance.getDbName() + "'",
					"Database password:", new TextInputOkHandler() {
						@Override
						public void onClick(ClickEvent event, String input) {
							switchDatabaseRequest(instance, input);
						}
					} );
		} else {
			switchDatabaseRequest(instance, "");
		}

	}

	private void switchDatabaseRequest(DatabaseInstance instance, String password) {
		Loader.showLoader();
		dbManager.selectDatabase(instance, password, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();

				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				Loader.hideLoader();

				updateScenarioList();
			}
		});
	}

	/*
	 * loading the latest scenariolist
	 */
	private void updateScenarioList() {
		GWT.log("update scenariolist");
		Loader.showLoader();

		listboxScenarios.setEnabled(true);

		Loader.hideLoader();
	}
}
