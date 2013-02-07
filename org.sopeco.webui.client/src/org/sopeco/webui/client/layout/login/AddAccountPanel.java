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
import org.sopeco.gwt.widgets.ToggleSeparator;
import org.sopeco.gwt.widgets.ToggleSeparator.ToggleHandler;
import org.sopeco.webui.client.helper.SystemDetails;
import org.sopeco.webui.client.helper.handler.EnterButtonHandler;
import org.sopeco.webui.client.resources.R;

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

		gridDatabaseSettings = new Grid(2, 2);
		gridDatabaseSettings.setWidth("100%");
		gridDatabaseSettings.addStyleName("addAccountGrid");
		gridDatabaseSettings.setVisible(false);

		gridDatabaseSettings.setWidget(0, 0, new HTML(R.lang.database() + " " + R.lang.host() + ":"));
		gridDatabaseSettings.setWidget(1, 0, new HTML(R.lang.database() + " " + R.lang.port() + ":"));

		gridDatabaseSettings.setWidget(0, 1, tbDatabaseHost);
		gridDatabaseSettings.setWidget(1, 1, tbDatabasePort);

		gridDatabaseSettings.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		gridDatabaseSettings.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		btnAddAccount = new Button(R.lang.addAccount());
		btnAddAccount.addStyleName("buttons");

		btnCancel = new Button(R.lang.cancel());
		btnCancel.addStyleName("buttons");

		panelButtons = new FlowPanel();
		panelButtons.add(btnCancel);
		panelButtons.add(btnAddAccount);
		panelButtons.add(new ClearDiv());

		EnterButtonHandler enterHandler = new EnterButtonHandler(btnAddAccount);
		tbName.addKeyPressHandler(enterHandler);
		tbPassword.addKeyPressHandler(enterHandler);
		tbPasswordConfirm.addKeyPressHandler(enterHandler);

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
