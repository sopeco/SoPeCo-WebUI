package org.sopeco.frontend.client.layout.login;

import org.sopeco.frontend.client.helper.SystemDetails;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.HorizontalLine;
import org.sopeco.gwt.widgets.ToggleSeparator;
import org.sopeco.gwt.widgets.ToggleSeparator.ToggleHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AddAccountPanel extends FlowPanel implements ToggleHandler {

	private static final String NOT_VALID_CLASS = "notValid";

	private Grid grid;
	private Grid gridDatabaseSettings;

	private TextBox tbName;
	private PasswordTextBox tbPassword;
	private PasswordTextBox tbPasswordConfirm;

	private TextBox tbDatabaseHost;
	private TextBox tbDatabasePort;
	private PasswordTextBox tbDatabasePassword;

	private Button btnAddAccount;
	private Button btnCancel;

	private FlowPanel panelButtons;

	private ToggleSeparator toggleDatabase;

	public AddAccountPanel() {
		init();
	}

	public void init() {
		Headline headline = new Headline(R.lang.addNewAccount());

		tbName = new TextBox();
		tbPassword = new PasswordTextBox();
		tbPasswordConfirm = new PasswordTextBox();

		HTML finePrint = new HTML("* " + R.lang.optional());
		finePrint.addStyleName("finePrint");

		grid = new Grid(4, 2);
		grid.setWidth("100%");
		grid.addStyleName("addAccountGrid");

		grid.setWidget(0, 0, new HTML(R.lang.accountname() + ":"));
		grid.setWidget(1, 0, new HTML(R.lang.password() + " *:"));
		grid.setWidget(2, 0, new HTML(R.lang.confirmPassword() + " *:"));

		grid.setWidget(0, 1, tbName);
		grid.setWidget(1, 1, tbPassword);
		grid.setWidget(2, 1, tbPasswordConfirm);
		grid.setWidget(3, 1, finePrint);

		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		toggleDatabase = new ToggleSeparator(R.lang.show() + " " + R.lang.database() + " " + R.lang.settings(),
				R.lang.hide() + " " + R.lang.database() + " " + R.lang.settings());
		toggleDatabase.addToggleHandler(this);

		tbDatabaseHost = new TextBox();
		tbDatabasePort = new TextBox();
		tbDatabasePassword = new PasswordTextBox();

		gridDatabaseSettings = new Grid(3, 2);
		gridDatabaseSettings.setWidth("100%");
		gridDatabaseSettings.addStyleName("addAccountGrid");
		gridDatabaseSettings.setVisible(false);

		gridDatabaseSettings.setWidget(0, 0, new HTML(R.lang.database() + " " + R.lang.host() + ":"));
		gridDatabaseSettings.setWidget(1, 0, new HTML(R.lang.database() + " " + R.lang.port() + ":"));
		gridDatabaseSettings.setWidget(2, 0, new HTML(R.lang.database() + " " + R.lang.password() + ":"));

		gridDatabaseSettings.setWidget(0, 1, tbDatabaseHost);
		gridDatabaseSettings.setWidget(1, 1, tbDatabasePort);
		gridDatabaseSettings.setWidget(2, 1, tbDatabasePassword);

		gridDatabaseSettings.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		gridDatabaseSettings.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		gridDatabaseSettings.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		btnAddAccount = new Button(R.lang.addAccount());
		btnAddAccount.addStyleName("addAccountButtons");

		btnCancel = new Button(R.lang.cancel());
		btnCancel.addStyleName("addAccountButtons");

		panelButtons = new FlowPanel();
		panelButtons.add(btnCancel);
		panelButtons.add(btnAddAccount);
		panelButtons.add(new ClearDiv());

		addStyleName("content");
		addStyleName("dialogBox");
		addStyleName("dialogBox-padding");

		add(headline);
		add(grid);
		add(toggleDatabase);
		add(gridDatabaseSettings);
		add(new HorizontalLine());
		add(panelButtons);

		reset();
	}

	public Button getBtnAddAccount() {
		return btnAddAccount;
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public void reset() {
		tbName.setText("");
		tbPassword.setText("");
		tbPasswordConfirm.setText("");

		tbDatabaseHost.setText(SystemDetails.getMetaDatabaseHost());
		tbDatabasePort.setText(SystemDetails.getMetaDatabasePort());
		tbDatabasePassword.setText("");
	}

	@Override
	public void onToggle(boolean state) {
		gridDatabaseSettings.setVisible(state);
	}

	public TextBox getTbName() {
		return tbName;
	}

	public PasswordTextBox getTbPassword() {
		return tbPassword;
	}

	public TextBox getTbDatabaseHost() {
		return tbDatabaseHost;
	}

	public TextBox getTbDatabasePort() {
		return tbDatabasePort;
	}

	public PasswordTextBox getTbDatabasePassword() {
		return tbDatabasePassword;
	}

	public boolean formValid() {
		boolean empty = false;
		for (TextBox tb : new TextBox[] { tbName, tbDatabaseHost, tbDatabasePort }) {
			if (tb.getText().trim().isEmpty()) {
				tb.addStyleName(NOT_VALID_CLASS);
				empty = true;
			} else {
				tb.removeStyleName(NOT_VALID_CLASS);
			}
		}

		if (!tbPassword.getText().equals(tbPasswordConfirm.getText())) {
			tbPasswordConfirm.addStyleName(NOT_VALID_CLASS);
			return false;
		} else {
			tbPasswordConfirm.removeStyleName(NOT_VALID_CLASS);
		}
		
		if (empty) {
			return false;
		}

		return true;
	}
}
