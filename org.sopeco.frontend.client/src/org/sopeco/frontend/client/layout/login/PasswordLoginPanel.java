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
public class PasswordLoginPanel extends FlowPanel {

	private Button btnCancel;
	private Button btnContinue;

	private HTML htmlInfoText;
	private HTML htmlWrongPassword;

	private PasswordTextBox tbPassword;

	public PasswordLoginPanel() {
		init();
	}

	private void init() {
		Headline headline = new Headline("Enter account password");

		htmlInfoText = new HTML();

		htmlWrongPassword = new HTML(R.lang.wrongPassword());
		htmlWrongPassword.addStyleName("htmlWrongPassword");
		htmlWrongPassword.setVisible(false);

		tbPassword = new PasswordTextBox();

		HorizontalPanel panelPassword = new HorizontalPanel();
		panelPassword.addStyleName("panelPasswordLogin");
		panelPassword.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panelPassword.add(new HTML(R.lang.account() + " " + R.lang.password() + ":"));
		panelPassword.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		panelPassword.add(tbPassword);

		btnCancel = new Button(R.lang.cancel());
		btnCancel.addStyleName("buttons");

		btnContinue = new Button(R.lang.continueString());
		btnContinue.addStyleName("buttons");

		FlowPanel panelButtons = new FlowPanel();
		panelButtons.add(btnCancel);
		panelButtons.add(btnContinue);
		panelButtons.add(new ClearDiv());

		tbPassword.addKeyPressHandler(new EnterButtonHandler(btnContinue));
		
		addStyleName("content");
		addStyleName("dialogBox");
		addStyleName("dialogBox-padding");

		add(headline);
		add(htmlInfoText);
		add(panelPassword);
		add(htmlWrongPassword);
		add(new HorizontalLine());
		add(panelButtons);
	}

	public void setAccountInfos(DatabaseInstance account) {
		htmlInfoText
				.setHTML("This account is password protected. You have to enter the passwort to log in into the account '<b>"
						+ account.getDbName() + "</b>'.");
		htmlWrongPassword.setVisible(false);
		tbPassword.setText("");
	}

	public Button getBtnCancel() {
		return btnCancel;
	}

	public Button getBtnContinue() {
		return btnContinue;
	}

	public PasswordTextBox getTbPassword() {
		return tbPassword;
	}

	public HTML getHtmlWrongPassword() {
		return htmlWrongPassword;
	}

}
