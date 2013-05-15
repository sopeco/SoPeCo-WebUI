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

import java.util.Date;

import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.entities.account.AccountDetails;
import org.sopeco.webui.shared.helper.LoginResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class LoginPanel extends FlowPanel {

	public static final String COOKIE_DATABASE = "selected_database";

	public static final String COOKIE_RM_ACCOUNT = "RMAccount";
	public static final String COOKIE_RM_TOKEN = "RMToken";

	/** Number of milliseconds in seven days. */
	private static final long COOKIE_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;

	private SimplePanel verticalCell;

	private CreateAccount createAccount;
	private LoginView loginView;

	private HTML htmlFEVersionInfo;

	private FlowPanel selectLanguagePanel;
	private FlowPanel logoPanel;

	private boolean initialized = false;

	/**
	 * Cosntructor.
	 */
	public LoginPanel() {
		if (!rememberMe()) {
			init();
		}
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		if (!initialized) {
			initialized = true;
			
			R.css.cssLoginBox().ensureInjected();
			addStyleName("loginPanel");

			htmlFEVersionInfo = new HTML(SoPeCoUI.getBuildInfo());
			htmlFEVersionInfo.addStyleName("htmlFEVersionInfo");

			Image imgLogo = new Image("/branding.png");

			if (SoPeCoUI.hasBranding()) {
				logoPanel = new FlowPanel();
				logoPanel.addStyleName("imgSapResearch");
				logoPanel.add(new HTML("powered by"));
				logoPanel.add(imgLogo);
			}

			createLanguagePanel();

			verticalCell = new SimplePanel();

			createAccount = new CreateAccount(this);
			loginView = new LoginView(this);

			verticalCell.add(loginView);
			add(verticalCell);
			add(htmlFEVersionInfo);
			if (logoPanel != null) {
				add(logoPanel);
			}
		}
	}

	private boolean rememberMe() {
		final String accountName = Cookies.getCookie(COOKIE_RM_ACCOUNT);
		final String token = Cookies.getCookie(COOKIE_RM_TOKEN);

		if (accountName != null && token != null) {
			RPC.getAccountManagementRPC().login(accountName, token, new AsyncCallback<LoginResponse>() {
				@Override
				public void onSuccess(LoginResponse result) {
					handleLoginResponse(result, accountName, true);
				}

				@Override
				public void onFailure(Throwable caught) {
					throw new IllegalStateException(caught);
				}
			});
			return true;
		} else {
			return false;
		}
	}

	private void handleLoginResponse(LoginResponse response, String accountName, boolean persistentLogin) {
		if (response.isSuccessful()) {
			Date expireDate = new Date();
			expireDate.setTime(expireDate.getTime() + COOKIE_EXPIRE_TIME);
			Cookies.setCookie(COOKIE_DATABASE, accountName, expireDate);

			if (persistentLogin) {
				Cookies.setCookie(COOKIE_RM_ACCOUNT, accountName, expireDate);
				Cookies.setCookie(COOKIE_RM_TOKEN, response.getRememberMeToken(), expireDate);
			}

			getAccountSettings();
		} else {
			init();
			loginView.setPasswordError(R.lang.msgPasswordIncorrect());
			loginView.selectPasswordField();
			loginView.setEnableLoginButton(true);

			Cookies.removeCookie(COOKIE_RM_ACCOUNT);
			Cookies.removeCookie(COOKIE_RM_TOKEN);
		}
	}

	private void createLanguagePanel() {
		selectLanguagePanel = new FlowPanel();
		selectLanguagePanel.addStyleName("selectLanguage");

		selectLanguagePanel.add(new HTML(R.lang.selectLanguage()));
		selectLanguagePanel
				.add(new Anchor(AbstractImagePrototype.create(R.img.flagEn()).getSafeHtml(), "frontend.jsp"));
		selectLanguagePanel.add(new Anchor(AbstractImagePrototype.create(R.img.flagDe()).getSafeHtml(),
				"frontend.jsp?locale=de"));
	}

	public void switchToLogin() {
		verticalCell.clear();
		loginView.resetFields();
		verticalCell.add(loginView);
	}

	public void switchToCreate() {
		verticalCell.clear();
		createAccount.resetInput();
		verticalCell.add(createAccount);
	}

	public void loginIntoAccount(final String accountName, final String password, final boolean persistentLogin) {
		loginView.setEnableLoginButton(false);

		/** code split point */
		GWT.runAsync(new RunAsyncCallback() {
			@Override
			public void onFailure(Throwable reason) {
				// TODO error handling
				Window.alert(R.lang.codeDownloadFailed());
			}

			@Override
			public void onSuccess() {
				RPC.getAccountManagementRPC().login(accountName, password, persistentLogin,
						new AsyncCallback<LoginResponse>() {
							@Override
							public void onFailure(Throwable caught) {
								throw new RuntimeException(caught);
							}

							@Override
							public void onSuccess(LoginResponse result) {
								handleLoginResponse(result, accountName, persistentLogin);
							}
						});
			}
		});
	}

	private void getAccountSettings() {
		RPC.getAccountManagementRPC().getAccountDetails(new AsyncCallback<AccountDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(AccountDetails result) {
				Manager.get().setAccountDetails(result);
				SoPeCoUI.get().initializeMainView();
			}
		});
	}

}
