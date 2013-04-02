package org.sopeco.webui.client.test;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.layout.center.visualization.VisualizationController;
import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.rpc.DatabaseManagerRPC;
import org.sopeco.webui.client.rpc.DatabaseManagerRPCAsync;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.client.rpc.VisualizationRPC;
import org.sopeco.webui.client.rpc.VisualizationRPCAsync;
import org.sopeco.webui.shared.entities.AccountDetails;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class GwtTestLogin extends GWTTestCase {
	private SoPeCoUI soPeCoUI;
	private DatabaseInstance db = null;
	String moduleName = "org.sopeco.webui.SoPeCo_UI";

	@Override
	public String getModuleName() {
		return moduleName;
	}

	public void testIt(){
//		DatabaseManagerRPCAsync dbmanager = GWT.create(DatabaseManagerRPC.class);
//		System.out.println("Created db manager " + dbmanager);
//		delayTestFinish(20000);
//		soPeCoUI = new SoPeCoUI();
//		soPeCoUI.onModuleLoad();
//		System.out.println("Created SoPeCoUI-Module... -----------------------------------------------------------------------------------------------------");
//		dbmanager.getAllDatabases(new AsyncCallback<List<DatabaseInstance>>() {
//			
//			@Override
//			public void onSuccess(List<DatabaseInstance> result) {
//				if (result.size() <= 0){
//					fail("No databases found");
//				} else {
//					System.out.println("answer -----------------------------------------------------------------------------------------------------");
//					for (DatabaseInstance dbi : result){
//						System.out.println(dbi.getDbName());
//					}
//					db = result.get(0);
//					login();
//				}
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				fail(caught.getMessage());
//			}
//		});
	}
	
	public void login(){
//		RPC.getDatabaseManagerRPC().login(db, "", new AsyncCallback<Boolean>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				fail(caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(Boolean result) {
//				GWT.log("Succesfully logged in. -----------------------------------------------------------------------------------------------------");
//				getAccountDetails();
//			}
//		});
	}
	
	public void getAccountDetails(){
//		RPC.getDatabaseManagerRPC().getAccountDetails(new AsyncCallback<AccountDetails>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				fail(caught.getMessage());
//			}
//
//			@Override
//			public void onSuccess(AccountDetails result) {
//				Manager.get().setAccountDetails(result);
//				SoPeCoUI.get().initializeMainView(db);
//				GWT.log("Initialized main view -----------------------------------------------------------------------------------------------------");
//				finishTest();
//			}
//		});
	}
}
