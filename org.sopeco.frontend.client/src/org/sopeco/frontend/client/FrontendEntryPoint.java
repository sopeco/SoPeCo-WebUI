package org.sopeco.frontend.client;

import java.util.HashMap;

import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.helper.SystemDetails;
import org.sopeco.frontend.client.helper.callback.CallbackBatch;
import org.sopeco.frontend.client.helper.callback.ParallelCallback;
import org.sopeco.frontend.client.layout.LoginBox;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry class of this webapplication.
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendEntryPoint implements EntryPoint {

	private DatabaseInstance connectedDatabase;
	private static FrontendEntryPoint frontend;

	private CallbackBatch loadingBatch;

	/**
	 * will be executed at the start of the application.
	 */
	public void onModuleLoad() {
		frontend = this;

		loadFirstStep();

		// TODO: alle callbacks die am anfang etwas laden muessen, als
		// parallelcallback in die batch..
		ParallelCallback<ExtensionContainer> loadExtensions = Extensions.getLoadingCallback();
		ParallelCallback<HashMap<String, String>> loadSystemDetails = SystemDetails.getLoadingCallback();

		loadingBatch = new CallbackBatch(loadExtensions, loadSystemDetails) {
			@Override
			protected void onSuccess() {
				System.out.println("loading finished...");
			}

			@Override
			protected void onFailure() {
				Message.error("Loading data failed..");
			}
		};

		RPC.getExtensionRPC().getExtensions(loadExtensions);
		RPC.getSystemDetailsRPC().getMetaDatabaseDetails(loadSystemDetails);
	}

	private void loadFirstStep() {
		Timer waitForLang = new Timer() {
			@Override
			public void run() {
				if (R.loadLangFile()) {
					loadSecondStep();
					cancel();
				}
			}
		};
		waitForLang.scheduleRepeating(5);
	}

	private void loadSecondStep() {
		changeDatabase();
	}

	/**
	 * Initialize/reset the main view of the application.
	 * 
	 * @param newConnectedDatabase
	 *            the database of the current connection.
	 */
	public void initializeMainView(DatabaseInstance newConnectedDatabase) {
		connectedDatabase = newConnectedDatabase;

		ScenarioManager.clear();
		MainLayoutPanel.destroy();

		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.add(MainLayoutPanel.get());
	}

	/**
	 * clears the root layout panel.
	 */
	private void clearRootLayout() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();

		rootLayoutPanel.clear();
	}

	/**
	 * Causing a change of the database.
	 */
	public void changeDatabase() {
		clearRootLayout();

		LoginBox box = new LoginBox(this);
		box.center();
	}

	/**
	 * returns the database instance of the current connection/session.
	 * 
	 * @return database instance
	 */
	public DatabaseInstance getConnectedDatabase() {
		return connectedDatabase;
	}

	/**
	 * Returns the FrontendEntryPoint object, which is like the "main method" of
	 * the current process.
	 * 
	 * @return FrontendEntryPoint-Object
	 */
	public static FrontendEntryPoint get() {
		return frontend;
	}

	public static native String getDocumentLastModifiedDate()
	/*-{
		return document.lastModified;
	}-*/;
}
