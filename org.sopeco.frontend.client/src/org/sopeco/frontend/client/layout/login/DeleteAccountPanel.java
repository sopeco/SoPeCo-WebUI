package org.sopeco.frontend.client.layout.login;

import org.sopeco.frontend.client.helper.handler.EnterButtonHandler;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.HorizontalLine;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class DeleteAccountPanel extends FlowPanel {

	private Button btnCancel;
	private Button btnDelete;

	private HTML htmlText;
	private HTML htmlPasswordProtected;
	private HTML htmlWrongPassword;

	private PasswordTextBox tbPassword;
	private HorizontalPanel panelPassword;

	public DeleteAccountPanel() {
		init();
	}

	public void init() {
		Headline headline = new Headline("Delete account");

		htmlText = new HTML();

		HTML htmlPassword = new HTML(R.lang.account() + " " + R.lang.password() + ":");

		htmlPasswordProtected = new HTML(
				"This account is password protected. You have to enter the passwort to remove it.");
		htmlPasswordProtected.addStyleName("htmlPasswordProtected");

		htmlWrongPassword = new HTML(R.lang.wrongPassword());
		htmlWrongPassword.addStyleName("htmlWrongPassword");
		htmlWrongPassword.setVisible(false);

		tbPassword = new PasswordTextBox();

		panelPassword = new HorizontalPanel();
		panelPassword.addStyleName("panelPassword");
		panelPassword.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelPassword.add(htmlPassword);
		panelPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelPassword.add(tbPassword);

		btnCancel = new Button(R.lang.cancel());
		btnCancel.addStyleName("buttons");

		btnDelete = new Button(R.lang.delete());
		btnDelete.addStyleName("buttons");

		FlowPanel panelButtons = new FlowPanel();
		panelButtons.add(btnCancel);
		panelButtons.add(btnDelete);
		panelButtons.add(new ClearDiv());

		tbPassword.addKeyPressHandler(new EnterButtonHandler(btnDelete));
		
		addStyleName("deleteAccountPanel");
		addStyleName("content");
		addStyleName("dialogBox");
		addStyleName("dialogBox-padding");

		add(headline);
		add(htmlText);
		add(htmlPasswordProtected);
		add(panelPassword);
		add(htmlWrongPassword);
		add(new HorizontalLine());
		add(panelButtons);
	}

	public void setAccountInfos(DatabaseInstance account) {
		htmlText.setHTML("Are you sure you want to delete the account <b>'" + account.getDbName() + "'</b>?");

		panelPassword.setVisible(account.isProtectedByPassword());
		tbPassword.setText("");
		htmlPasswordProtected.setVisible(account.isProtectedByPassword());
		htmlWrongPassword.setVisible(false);
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public PasswordTextBox getTbPassword() {
		return tbPassword;
	}

	public HTML getHtmlWrongPassword() {
		return htmlWrongPassword;
	}

}
