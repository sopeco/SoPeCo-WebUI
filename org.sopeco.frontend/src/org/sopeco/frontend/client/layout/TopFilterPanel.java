package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.layout.dialog.SelectDBDialog;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class TopFilterPanel extends DecoratorPanel {
	public TopFilterPanel() {
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

		ListBox listBox = new ListBox();
		horizontalPanel.add(listBox);
		listBox.setWidth("120px");
		listBox.setVisibleItemCount(1);

		Button btnManageDatabases = new Button("manage databases");
		btnManageDatabases.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SelectDBDialog selectDBDialog = new SelectDBDialog();
				
				selectDBDialog.center();
			}
		});
		horizontalPanel.add(btnManageDatabases);
		btnManageDatabases.setWidth("140px");

		HTML htmlNewHtml = new HTML("", true);
		htmlNewHtml.setStyleName("spc-seperator");
		floatlPanel.add(htmlNewHtml);
		htmlNewHtml.setHeight("50px");

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(10);
		horizontalPanel_1
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
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
	}

}
