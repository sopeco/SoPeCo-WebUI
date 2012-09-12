package org.sopeco.frontend.client.layout;

import java.util.List;

import org.sopeco.frontend.client.layout.dialog.SelectDBDialog;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPC;
import org.sopeco.frontend.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.frontend.shared.definitions.DatabaseDefinition;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class TopFilterPanel extends DecoratorPanel {

	private TopFilterPanel thisPanel;
	private DatabaseManagerRPCAsync dbManager;

	private ListBox listboxDatabases;
	
	public TopFilterPanel() {
		thisPanel = this;
		
		dbManager = GWT.create(DatabaseManagerRPC.class);
		
		initialize();
	}

	private void initialize() {
		setWidth("100%");

		FlowPanel floatlPanel = new FlowPanel();

		floatlPanel.setStyleName("topFilterPanel");
		setWidget(floatlPanel);
		floatlPanel.setSize("100%", "");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		floatlPanel.add(horizontalPanel);

		Label lblSelectDatabase = new Label("select database:");
		horizontalPanel.add(lblSelectDatabase);
		lblSelectDatabase.setWidth("110px");

		listboxDatabases = new ListBox();
		horizontalPanel.add(listboxDatabases);
		listboxDatabases.setWidth("120px");
		listboxDatabases.setVisibleItemCount(1);

		Button btnAddDatabase = new Button("<img src=\"images/db_add.png\" />");
		btnAddDatabase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SelectDBDialog selectDBDialog = new SelectDBDialog(thisPanel);

				selectDBDialog.center();
			}
		});
		btnAddDatabase.setStyleName("sopeco-imageButton", true);
		horizontalPanel.add(btnAddDatabase);
		
		Button btnRemoveDatabase = new Button("<img src=\"images/db_remove.png\" />");
		btnRemoveDatabase.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
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

		Label lblSelectSzenario = new Label("select szenario");
		horizontalPanel_1.add(lblSelectSzenario);
		lblSelectSzenario.setWidth("110px");

		ListBox listBox_1 = new ListBox();
		horizontalPanel_1.add(listBox_1);
		listBox_1.setWidth("120px");
		listBox_1.setVisibleItemCount(1);

		Button btnManageScenarios = new Button("manage scenarios");
		horizontalPanel_1.add(btnManageScenarios);
		btnManageScenarios.setWidth("140px");
		
		updateDatabaseList();
	}

	public void updateDatabaseList() {
		Loader.showLoader();
		
		dbManager.getAllDatabases(new AsyncCallback<List<DatabaseDefinition>>() {
			@Override
			public void onSuccess(List<DatabaseDefinition> result) {
				listboxDatabases.clear();
				
				for ( DatabaseDefinition dbDefinition : result ) {
					listboxDatabases.addItem(dbDefinition.getName());
				}
				
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

}
