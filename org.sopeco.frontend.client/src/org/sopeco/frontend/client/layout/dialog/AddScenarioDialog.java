package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.layout.NorthPanel;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AddScenarioDialog extends DialogBox {

	private Button btnAdd;
	private TextBox textboxName;
	private NorthPanel parentPanel;

	public AddScenarioDialog(NorthPanel parent) {
		super(false, true);

		this.parentPanel = parent;

		initialize();
	}

	/**
	 * initialize of the gui
	 */
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

		Label lblName = new Label("Scenarioname:");
		grid.setWidget(0, 0, lblName);

		textboxName = new TextBox();
		textboxName.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (textboxName.getText().length() > 0) {
					btnAdd.setEnabled(true);
				} else {
					btnAdd.setEnabled(false);
				}
			}
		});
		textboxName.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (textboxName.getText().length() > 0) {
					btnAdd.setEnabled(true);
				} else {
					btnAdd.setEnabled(false);
				}
			}
		});
		grid.setWidget(0, 1, textboxName);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		btnAdd = new Button("add");
		btnAdd.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addNewScenario();
			}
		});
		btnAdd.setEnabled(false);
		horizontalPanel.add(btnAdd);
		btnAdd.setWidth("150px");

		Button btnCancel = new Button("cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cancel();
			}
		});
		horizontalPanel.add(btnCancel);
		btnCancel.setWidth("150px");
	}

	private void cancel() {
		hide();
	}

	private void addNewScenario() {

		String scenarioName = textboxName.getText();

		if (scenarioName.isEmpty()) {
			return;
		}

		ScenarioManager.get().createScenario(scenarioName);

		hide();
	}

}
