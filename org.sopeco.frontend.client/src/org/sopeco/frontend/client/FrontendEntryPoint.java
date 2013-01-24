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
package org.sopeco.frontend.client;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.helper.ServerPush;
import org.sopeco.frontend.client.helper.SimpleNotify;
import org.sopeco.frontend.client.helper.SystemDetails;
import org.sopeco.frontend.client.helper.callback.CallbackBatch;
import org.sopeco.frontend.client.helper.callback.ParallelCallback;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.login.LoginPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.log.LogHandler;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry class of this webapplication.
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendEntryPoint implements EntryPoint, SimpleNotify {

	private static FrontendEntryPoint frontend;

	private static final Logger LOGGER = Logger.getLogger(FrontendEntryPoint.class.getName());


	/**
	 * Returns the FrontendEntryPoint object of this application.
	 * 
	 * @return FrontendEntryPoint-Object
	 */
	public static FrontendEntryPoint get() {
		return frontend;
	}


	/**
	 * Returns a string, which contains the date when this document was modified
	 * (normally the tomcat deploy date).
	 * 
	 * @return date as a string
	 */
	public static native String getDocumentLastModifiedDate()
	/*-{
		return document.lastModified;
	}-*/;

	public static native String getBuildInfo()
	/*-{
		return $wnd.buildInfo;
	}-*/;

	private DatabaseInstance connectedDatabase;

	private CallbackBatch loadingBatch;

	/**
	 * This method will be executed at the start of the application. It's like
	 * the "main-method" of the application.
	 */
	@Override
	public void onModuleLoad() {
		frontend = this;

		R.resc.cssCommon().ensureInjected();

		configLogger();
		R.loadLangFile(FrontendEntryPoint.this);
	}

	/**
	 * Sets the level and the handler of the root-logger.
	 */
	private void configLogger() {
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.FINE);
		rootLogger.addHandler(LogHandler.get());
	}

	/**
	 * Called when the language file was loaded.
	 */
	@Override
	public void call() {
		rpcLoad();
	}

	/**
	 * Loading details from SoPeCo and the from the Backend (supported
	 * exploration extensions, database-host,...).
	 */
	private void rpcLoad() {
		ParallelCallback<ExtensionContainer> loadExtensions = Extensions.getLoadingCallback();
		ParallelCallback<HashMap<String, String>> loadSystemDetails = SystemDetails.getLoadingCallback();

		loadingBatch = new CallbackBatch(loadExtensions, loadSystemDetails) {
			@Override
			protected void onFailure(List<Throwable> exceptionList) {
				for (Throwable t : exceptionList) {
					LOGGER.severe(t.getLocalizedMessage());
					Message.error(t.getMessage());
				}
			}

			@Override
			protected void onSuccess() {
				// All stuff was loaded
				changeDatabase();
				ServerPush.get().startRequest();
			}
		};

		RPC.getExtensionRPC().getExtensions(loadExtensions);
		RPC.getSystemDetailsRPC().getMetaDatabaseDetails(loadSystemDetails);
	}

	/**
	 * Causing a change of the database.
	 */
	public void changeDatabase() {
		clearRootLayout();
		Manager.get().reset();
		EventControl.removeAllHandler();

		// LoginBox box = new LoginBox();
		// box.center();
		RootLayoutPanel.get().add(new LoginPanel());
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
		rootLayoutPanel.clear();
		rootLayoutPanel.add(MainLayoutPanel.get());
	}

	/**
	 * Clears the root-layout-panel. The browser-window is empty after this
	 * method.
	 */
	private void clearRootLayout() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.clear();
	}

}
