package org.sopeco.frontend.client.layout;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginBox extends DialogBox {
	public LoginBox() {
		super(false, true);
		
		initialize();
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
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setSpacing(5);
		verticalPanel.add(horizontalPanel);

		ListBox listboxDatabases = new ListBox();
		listboxDatabases.addItem("--SELECT--");
		listboxDatabases.setSelectedIndex(0);
		horizontalPanel.add(listboxDatabases);
		listboxDatabases.setSize("200px", "");
		listboxDatabases.setVisibleItemCount(1);

		Button btnAddDb = new Button("<img src=\"images/db_add.png\" />");
		horizontalPanel.add(btnAddDb);
		btnAddDb.setStyleName("sopeco-imageButton", true);

		Button btnRemoveDb = new Button("<img src=\"images/db_remove.png\" />");
		horizontalPanel.add(btnRemoveDb);
		btnRemoveDb.setStyleName("sopeco-imageButton", true);
	}
}
