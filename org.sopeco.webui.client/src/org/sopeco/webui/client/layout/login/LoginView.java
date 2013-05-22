package org.sopeco.webui.client.layout.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite {

	private static final String ERROR_COLOR = "#dd4c40";

	private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

	interface LoginUiBinder extends UiBinder<Widget, LoginView> {
	}

	private LoginPanel loginPanel;

	public LoginView(LoginPanel login) {
		loginPanel = login;

		initWidget(uiBinder.createAndBindUi(this));

		resetFields();
	}

	@UiField
	TextBox tbAccount;
	@UiField
	PasswordTextBox tbPassword;

	@UiField
	Label errorAccount;
	@UiField
	Label errorPassword;

	@UiField
	PushButton btnLogin;

	@UiField
	CheckBox cbPersistentLogin;

	@UiHandler("btnLogin")
	void clickLogin(ClickEvent e) {
		boolean valid = true;

		// Strip whitespaces
		tbAccount.setText(tbAccount.getValue().trim());

		setAccountError("");
		setPasswordError("");

		if (tbAccount.getValue().isEmpty()) {
			setAccountError("Field must not be empty");
			valid = false;
		}

		if (valid) {
			loginPanel.loginIntoAccount(tbAccount.getValue(), tbPassword.getValue(), cbPersistentLogin.getValue());
		}
	}

	@UiHandler("btnCreate")
	void clickCreate(ClickEvent e) {
		loginPanel.switchToCreate();
	}

	@UiHandler({ "tbAccount", "tbPassword" })
	void onPasswordTextBoxKeyPress(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			clickLogin(null);
		}
	}

	public void resetFields() {
		setEnableLoginButton(true);
		setAccountError("");
		setPasswordError("");
		tbPassword.setText("");

		String cookieText = Cookies.getCookie(LoginPanel.COOKIE_DATABASE);
		if (cookieText != null)
			tbAccount.setText(cookieText);
	}

	public void setAccountError(String text) {
		setErrorText(tbAccount, errorAccount, text);
	}

	public void setPasswordError(String text) {
		setErrorText(tbPassword, errorPassword, text);
	}

	private void setErrorText(TextBox textbox, Label label, String text) {
		if (text.trim().isEmpty()) {
			textbox.getElement().getStyle().clearBorderColor();
			label.getElement().getStyle().setDisplay(Display.NONE);
		} else {
			textbox.getElement().getStyle().setBorderColor(ERROR_COLOR);
			label.getElement().getStyle().setDisplay(Display.BLOCK);
		}

		label.setText(text);
	}

	public void selectPasswordField() {
		tbPassword.setFocus(true);
		tbPassword.setSelectionRange(0, tbPassword.getValue().length());
	}

	public void setEnableLoginButton(boolean state) {
		btnLogin.setEnabled(state);
	}
}
