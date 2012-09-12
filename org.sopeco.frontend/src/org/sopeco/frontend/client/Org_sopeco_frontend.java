package org.sopeco.frontend.client;

import org.sopeco.frontend.client.layout.MainPanel;
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
	}
	
	private void initialize () {
		RootPanel rootPanel = RootPanel.get();
		
		rootPanel.add(getMainPanel());
	}
	
	public MainPanel getMainPanel () {
		if (mainPanel == null){
			mainPanel = new MainPanel();
		}
		return mainPanel;
	}
	
	private void pushServer () {
		StartupServiceAsync startup = GWT.create(StartupService.class);
		
		startup.start(new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				GWT.log("start passed");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("start failed");
			}
		});
	}
}
