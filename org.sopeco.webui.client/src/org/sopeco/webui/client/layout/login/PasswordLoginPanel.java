/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.client.layout.login;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.HorizontalLine;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.client.helper.handler.EnterButtonHandler;
import org.sopeco.webui.client.resources.R;

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
