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

import java.util.List;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.WrappedTextBox;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SmallTableLabel;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SelectAccountPanel extends VerticalPanel {

	private static final int CB_ACCOUNTS_WIDTH = 205;

	private Button btnConnect;
	private FlowPanel panelSelection;

	private Button btnAddAccount;

	private WrappedTextBox tbLogin;
	private WrappedTextBox tbPassword;

	private SmallTableLabel lblLogin;
	private SmallTableLabel lblPassword;

	public SelectAccountPanel() {
		init();
	}

	private void init() {
		btnAddAccount = new Button((AbstractImagePrototype.create(R.resc.imgIconDatabaseAdd()).getHTML()));
		btnAddAccount.addStyleName("accountButton");
		btnAddAccount.setTitle(R.lang.addNewAccount());

		btnConnect = new Button(R.lang.connect());
		btnConnect.addStyleName("btnConnect");

		panelSelection = new FlowPanel();
		panelSelection.addStyleName("panelSelection");
		panelSelection.add(btnAddAccount);
		panelSelection.add(btnConnect);
		panelSelection.add(new ClearDiv());

		tbLogin = new WrappedTextBox();
		tbPassword = new WrappedTextBox();
		tbPassword.setAsPasswordTextbox();

		lblLogin = new SmallTableLabel("Login");
		lblPassword = new SmallTableLabel("Password");

		String cookieText = Cookies.getCookie(LoginPanel.COOKIE_DATABASE);
		if (cookieText != null)
			tbLogin.getTextbox().setText(cookieText);

		addStyleName("content");
		addStyleName("dialogBox");
		addStyleName("dialogBox-padding");

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		Image sopecoLogo = new Image(R.resc.imgSoPeCoLogo().getSafeUri());
		sopecoLogo.setWidth("280px");
		sopecoLogo.setHeight("77px");
		sopecoLogo.getElement().getStyle().setMarginTop(25, Unit.PX);
		sopecoLogo.getElement().getStyle().setMarginBottom(10, Unit.PX);

		add(sopecoLogo);

		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		add(lblLogin);
		add(tbLogin);
		add(lblPassword);
		add(tbPassword);
		add(panelSelection);

		btnConnect.setEnabled(!tbLogin.getTextbox().getValue().isEmpty());
	}

	public Button getBtnConnect() {
		return btnConnect;
	}

	public Button getBtnAddAccount() {
		return btnAddAccount;
	}

	public WrappedTextBox getTbLogin() {
		return tbLogin;
	}

	public WrappedTextBox getTbPassword() {
		return tbPassword;
	}
}
