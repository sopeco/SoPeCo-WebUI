package org.sopeco.webui.client.layout.login;

import org.sopeco.webui.client.helper.SystemDetails;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CreateAccount extends Composite {

	private static final String ERROR_COLOR = "#dd4c40";

	private enum EField {
		NAME, PASSWORD, PASSWORD_CONFIRM, DB_HOST, DB_PORT
	}

	private static AddAccountUiBinder uiBinder = GWT.create(AddAccountUiBinder.class);

	interface AddAccountUiBinder extends UiBinder<Widget, CreateAccount> {
	}

	private LoginPanel loginPanel;

	public CreateAccount(LoginPanel login) {
		initWidget(uiBinder.createAndBindUi(this));

		loginPanel = login;

		resetInput();
	}

	@UiField
	TextBox accountName;
	@UiField
	PasswordTextBox password;
	@UiField
	PasswordTextBox passwordConfirm;
	@UiField
	Label toggleText;
	@UiField
	TextBox dbPort;
	@UiField
	TextBox dbHost;

	@UiField
	HorizontalPanel panelDb;
	@UiField
	HorizontalPanel panelDbHost;
	@UiField
	HorizontalPanel panelDbPort;

	@UiField
	Label errorName;
	@UiField
	Label errorPassword;
	@UiField
	Label errorPasswordConfirm;
	@UiField
	Label errorDbHost;
	@UiField
	Label errorDbPort;

	@UiField
	CheckBox ownDatabase;

	@UiHandler("ownDatabase")
	void changed(ValueChangeEvent<Boolean> e) {
		panelDb.getElement().getStyle().setOpacity(e.getValue() ? 1 : 0.7);
		panelDbHost.getElement().getStyle().setProperty("display", e.getValue() ? "table" : "none");
		panelDbPort.getElement().getStyle().setProperty("display", e.getValue() ? "table" : "none");
	}

	@UiHandler("btnAdd")
	void clickAdd(ClickEvent e) {
		startInputVerification();
	}

	@UiHandler("btnCancel")
	void clickCancel(ClickEvent e) {
		loginPanel.switchToLogin();
	}

	@UiHandler({ "accountName", "password", "passwordConfirm", "dbPort", "dbHost" })
	void onPasswordTextBoxKeyPress(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			startInputVerification();
		}
	}

	/**
	 * Creates an account with the data of the TextBoxes and after the creation,
	 * it'll log into this new account.
	 */
	private void createAccount() {

		RPC.getAccountManagementRPC().createAccount(accountName.getValue(), password.getValue(), dbHost.getValue(),
				Integer.parseInt(dbPort.getValue()), new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						throw new RuntimeException(caught);
					}

					@Override
					public void onSuccess(Boolean result) {
						loginPanel.loginIntoAccount(accountName.getValue(), password.getValue(), false);
					}
				});

		// DatabaseInstance newAccount = new
		// DatabaseInstance(accountName.getValue(), dbHost.getValue(),
		// dbPort.getValue(), false);
		//
		// RPC.getDatabaseManagerRPC().addDatabase(newAccount,
		// password.getValue(), new AsyncCallback<Boolean>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// throw new RuntimeException(caught);
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// loginPanel.loginIntoAccount(accountName.getValue(),
		// password.getValue());
		// }
		// });
	}

	/**
	 * Checks if the given accountName already exists. If the name doesn't
	 * exist, it'll call the {@link #createAccount()} method.
	 */
	private void startInputVerification() {
		RPC.getAccountManagementRPC().accountExist(accountName.getValue(), new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				verifyInput(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}
		});
	}

	/**
	 * Clears all fields and set it to the default value.
	 */
	public void resetInput() {
		for (EField e : EField.values()) {
			getTextBox(e).setValue("");
			setErrorText(e, "");
		}
		dbHost.setText(SystemDetails.getMetaDatabaseHost());
		dbPort.setText(SystemDetails.getMetaDatabasePort());
		ownDatabase.setValue(false, true);
	}

	/**
	 * Checks whether all relevant input fields contain valid values.
	 * 
	 * @return <code>false</code> if there any invalid value otherwise
	 *         <code>true</code>
	 */
	private void verifyInput(boolean nameAlreadyExists) {
		boolean result = true;

		// Reset errorMessages
		for (EField e : EField.values()) {
			setErrorText(e, "");
		}

		if (getTextBox(EField.NAME).getValue().isEmpty()) {
			setErrorText(EField.NAME, R.lang.msgFieldNotEmpty());
			result = false;
		} else if (getTextBox(EField.NAME).getValue().matches("^[0-9]+.*$")) {
			setErrorText(EField.NAME, R.lang.msgFieldNotStartNumber());
			result = false;
		} else if (!getTextBox(EField.NAME).getValue().matches("^[0-9a-zA-Z_]*$")) {
			setErrorText(EField.NAME, R.lang.msgNoSpecialChars());
			result = false;
		} else if (nameAlreadyExists) {
			setErrorText(EField.NAME, R.lang.msgAccountExists());
			result = false;
		}

		if (!getTextBox(EField.PASSWORD_CONFIRM).getValue().equals(getTextBox(EField.PASSWORD).getValue())) {
			setErrorText(EField.PASSWORD_CONFIRM, R.lang.msgPasswordNotMatch());
			result = false;
		}

		if (ownDatabase.getValue()) {

			if (!getTextBox(EField.DB_PORT).getValue().matches(
					"(\\d{1,4}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])")) {
				setErrorText(EField.DB_PORT, R.lang.msgPortMustBeInRange());
				result = false;
			}

		}

		// Create account if everything is ok
		if (result) {
			createAccount();
		}
	}

	/**
	 * Sets and shows the given text as a error message under the specified
	 * TextBox.
	 * 
	 * @param Which
	 *            TextBox is related to this errorMessage
	 * @param The
	 *            errorMessage
	 */
	private void setErrorText(EField textBox, String text) {
		Label label;
		switch (textBox) {
		case NAME:
			label = errorName;
			break;
		case DB_HOST:
			label = errorDbHost;
			break;
		case DB_PORT:
			label = errorDbPort;
			break;
		case PASSWORD:
			label = errorPassword;
			break;
		case PASSWORD_CONFIRM:
			label = errorPasswordConfirm;
			break;
		default:
			throw new IllegalArgumentException("Illegal enumeration: " + textBox);
		}

		if (text.isEmpty()) {
			getTextBox(textBox).getElement().getStyle().clearBorderColor();
			label.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			getTextBox(textBox).getElement().getStyle().setBorderColor(ERROR_COLOR);
			label.getElement().getStyle().setDisplay(Display.BLOCK);
		}

		label.setText(text);
	}

	/**
	 * Returns the TextBox, which is used for the input of the value of EField
	 * (Name, Password..).
	 * 
	 * @param Type
	 *            of the desired input
	 * @return TextBox for desired input
	 */
	private TextBox getTextBox(EField textBox) {
		switch (textBox) {
		case NAME:
			return accountName;
		case DB_HOST:
			return dbHost;
		case DB_PORT:
			return dbPort;
		case PASSWORD:
			return password;
		case PASSWORD_CONFIRM:
			return passwordConfirm;
		default:
			throw new IllegalArgumentException("Illegal enumeration: " + textBox);
		}
	}
}
