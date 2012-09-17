package org.sopeco.frontend.client;

import org.sopeco.frontend.client.helper.ServerPush;
import org.sopeco.frontend.client.helper.SystemDetails;
import org.sopeco.frontend.client.layout.LoginBox;
import org.sopeco.frontend.client.layout.MainPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.StartupService;
import org.sopeco.frontend.client.rpc.StartupServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Org_sopeco_frontend implements EntryPoint {

	private MainPanel mainPanel;

	public void onModuleLoad() {
		pushServer();

		initialize();
		
		SystemDetails.load();

		ServerPush.start();
		
//		LoginBox box = new LoginBox();
//		box.center();
	}

	private void initialize() {
		RootPanel rootPanel = RootPanel.get();
		
		rootPanel.add(getMainPanel());
	}

	public MainPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new MainPanel();
		}
		return mainPanel;
	}

	private void pushServer() {
		StartupServiceAsync startup = GWT.create(StartupService.class);

		startup.start(new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				GWT.log("startup passed");
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error("Error at startup");
			}
		});
	}
}
