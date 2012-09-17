package org.sopeco.frontend.client.layout.dialog;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;

public class AddScenarioDialog extends DialogBox {

	private Button btnNewButton;
	private TextBox textboxName;

	public AddScenarioDialog() {
		super(false, true);

		initialize();
	}

	private void initialize() {
		setGlassEnabled(true);

		setText("Add new scenario");

		VerticalPanel verticalPanel = new VerticalPanel();
		setWidget(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		Grid grid = new Grid(1, 2);
		grid.setCellPadding(4);
		verticalPanel.add(grid);
		grid.setWidth("300px");

		Label lblNewLabel = new Label("Scenarioname:");
		grid.setWidget(0, 0, lblNewLabel);

		textboxName = new TextBox();
		textboxName.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (textboxName.getText().length() > 0)
					btnNewButton.setEnabled(true);
				else
					btnNewButton.setEnabled(false);
			}
		});
		textboxName.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (textboxName.getText().length() > 0)
					btnNewButton.setEnabled(true);
				else
					btnNewButton.setEnabled(false);
			}
		});
		grid.setWidget(0, 1, textboxName);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		btnNewButton = new Button("add");
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addNewScenario();
			}
		});
		btnNewButton.setEnabled(false);
		horizontalPanel.add(btnNewButton);
		btnNewButton.setWidth("150px");

		Button btnNewButton_1 = new Button("cancel");
		btnNewButton_1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cancel();
			}
		});
		horizontalPanel.add(btnNewButton_1);
		btnNewButton_1.setWidth("150px");
	}

	private void cancel() {
		hide();
	}
	
	private void addNewScenario () {
		
		hide();
	}
}
