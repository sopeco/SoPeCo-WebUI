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
package org.sopeco.webui.client.mec;

import java.util.List;
import java.util.logging.Logger;

import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.event.EventControl;
import org.sopeco.webui.client.event.MEControllerEvent;
import org.sopeco.webui.client.event.MEControllerEvent.EventType;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.mec.MEControllerSettingsView.StatusImage;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.rpc.RPC;
import org.sopeco.webui.shared.helper.MEControllerProtocol;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerSettings implements HasValueChangeHandlers<Boolean>, ValueChangeHandler<String>, ClickHandler,
		KeyPressHandler {

	public static final int DEFAULT_PORT_RMI = 1099;
	public static final int DEFAULT_PORT_REST = 1300;

	private static final Logger LOGGER = Logger.getLogger(MEControllerSettings.class.getName());

	private HandlerManager manager;
	private MEControllerSettingsView view;

	private boolean lastValue;

	public MEControllerSettings() {
		init();
	}

	private void init() {
		manager = new HandlerManager(this);
		view = new MEControllerSettingsView();

		view.getComboBoxProtocol().addValueChangeHandler(this);
		view.getComboBoxController().addValueChangeHandler(this);
		view.getTextboxHost().addValueChangeHandler(this);
		view.getTextboxPort().addValueChangeHandler(this);
		view.getTextboxPassword().getTextbox().addValueChangeHandler(this);
		view.getTextboxToken().getTextbox().addValueChangeHandler(this);

		view.getTextboxHost().addKeyPressHandler(this);
		view.getTextboxPort().addKeyPressHandler(this);
		view.getTextboxPassword().getTextbox().addKeyPressHandler(this);
		view.getTextboxToken().getTextbox().addKeyPressHandler(this);

		view.getImgReload().addClickHandler(this);
		
		resetViewToDefault();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return manager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == view.getComboBoxProtocol()) {
			changedProtocol(event.getValue());
		} else if (event.getSource() == view.getComboBoxController()) {
			changedController(event.getValue());
		} else if (event.getSource() == view.getTextboxHost()) {
			changedHost(event.getValue());
		} else if (event.getSource() == view.getTextboxPort()) {
			changedPort(event.getValue());
		} else if (event.getSource() == view.getTextboxPassword().getTextbox()) {
			changedPassword(event.getValue());
		} else if (event.getSource() == view.getTextboxToken().getTextbox()) {
			changedToken(event.getValue());
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getImgReload()) {
			clickedReload();
		}
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER) {
			if (event.getSource() instanceof TextBox) {
				((TextBox) event.getSource()).setFocus(false);
			}
		}
	}

	private void changedProtocol(String protocol) {
		LOGGER.info("Changed protocol to: " + protocol);

		if (view.getSelectedProtocol() == MEControllerProtocol.SOCKET) {
			view.setStyleToken();
		} else {
			view.setStyleHostPort();

			if (view.getSelectedProtocol() == MEControllerProtocol.REST_HTTP) {
				view.getTextboxPort().setText("" + DEFAULT_PORT_REST);
			} else if (view.getSelectedProtocol() == MEControllerProtocol.RMI) {
				view.getTextboxPort().setText("" + DEFAULT_PORT_RMI);
			}
		}

		loadController();
	}

	private void changedController(String controller) {
		LOGGER.info("Changed controller to: " + controller);
	}

	private void changedPort(String port) {
		LOGGER.info("Changed port to: " + port);

		loadController();
	}

	private void changedPassword(String password) {
		LOGGER.info("Changed password");
	}

	private void changedHost(String host) {
		LOGGER.info("Changed host to: " + host);

		loadController();
	}

	private void changedToken(String token) {
		LOGGER.info("Changed token to: " + token);

		loadController();
	}

	private void clickedReload() {
		loadController();
	}

	public MEControllerSettingsView getView() {
		return view;
	}

	private double loadingId;

	public void loadController() {
		MEControllerProtocol protocol = view.getSelectedProtocol();
		String host = null;
		int port = 0;
		if (protocol == MEControllerProtocol.SOCKET) {
			host = view.getTextboxToken().getTextbox().getText();
		} else {
			host = view.getTextboxHost().getText();
			port = Integer.parseInt(view.getTextboxPort().getText());
		}

		view.setStatusImage(StatusImage.LOADING);
		fireValueChanged(false);

		// running rpcs will not be continued
		final double thisLoadingId = Math.random();
		loadingId = thisLoadingId;

		RPC.getGetRPC().getControllerFromMEC(protocol, host, port, new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				if (thisLoadingId == loadingId) {
					view.setStatusImage(StatusImage.RED);
				}
				SoPeCoUI.get().onUncaughtException(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				if (thisLoadingId == loadingId) {
					updateControllerCombobox(result);
				}
			}
		});
	}

	private void updateControllerCombobox(List<String> controller) {
		if (controller == null) {
			setControllerOffline();
		} else if (controller.isEmpty()) {
			setControllerEmpty();
		} else {
			setControllerOnline(controller);
		}
	}

	private void setControllerUnknown() {
		view.getComboBoxController().setEnabled(false);
		view.setStatusImage(StatusImage.GRAY);
		view.getHtmlStatus().setText("Unknown controller state");
		fireValueChanged(false);
	}

	private void setControllerOffline() {
		String currentText = view.getComboBoxController().getText();
		if (currentText.isEmpty() || currentText.equals(R.lang.noMecStarted())) {
			view.getComboBoxController().setText(R.lang.noMecOnline());
		}
		view.getComboBoxController().setEnabled(false);
		view.setStatusImage(StatusImage.RED);
		view.getHtmlStatus().setText(R.lang.noMecOnline());
		fireValueChanged(false);
	}

	private void setControllerEmpty() {
		String currentText = view.getComboBoxController().getText();
		if (currentText.isEmpty() || currentText.equals(R.lang.noMecOnline())) {
			view.getComboBoxController().setText(R.lang.noMecStarted());
		}
		view.getComboBoxController().setEnabled(false);
		view.setStatusImage(StatusImage.YELLOW);
		view.getHtmlStatus().setText(R.lang.noMecStarted());
		fireValueChanged(false);
	}

	private void setControllerOnline(List<String> controller) {
		String selectedController = view.getComboBoxController().getText();

		view.getComboBoxController().clear();
		for (String s : controller) {
			view.getComboBoxController().addItem(s);
		}
		view.getComboBoxController().setEnabled(true);
		view.setStatusImage(StatusImage.GREEN);
		view.getHtmlStatus().setText("Controller is online");
		view.getComboBoxController().setSelectedText(selectedController);

		fireValueChanged(true);
	}

	public void resetViewToDefault() {
		view.getComboBoxProtocol().setSelectedText("rmi://");
		view.getTextboxPort().setText("1099");
		view.getComboBoxController().setEnabled(false);
		view.getTextboxHost().setText("localhost");
		view.getTextboxToken().getTextbox().setText("token");

		if (view.getSelectedProtocol() == MEControllerProtocol.SOCKET) {
			view.setStyleToken();
		} else {
			view.setStyleHostPort();
		}

		setControllerUnknown();
	}

	public void resetViewToCurrent() {
		view.getComboBoxProtocol().setSelectedText(Manager.get().getCurrentScenarioDetails().getControllerProtocol());
		view.getTextboxPort().setText("" + Manager.get().getCurrentScenarioDetails().getControllerPort());
		view.getComboBoxController().setText(Manager.get().getCurrentScenarioDetails().getControllerName());
		view.getTextboxHost().setText(Manager.get().getCurrentScenarioDetails().getControllerHost());
		view.getTextboxToken().getTextbox().setText(Manager.get().getCurrentScenarioDetails().getControllerHost());

		if (view.getSelectedProtocol() == MEControllerProtocol.SOCKET) {
			view.setStyleToken();
		} else {
			view.setStyleHostPort();
		}

		setControllerUnknown();
	}

	public void fireValueChanged(boolean value) {
		lastValue = value;
		ValueChangeEvent.fire(this, value);
	}

	public boolean getLastValue() {
		return lastValue;
	}

	public void saveControllerSettings() {
		String protocol = view.getComboBoxProtocol().getText();
		String controller = view.getComboBoxController().getText();
		String host = null;
		int port = 0;

		if (view.getSelectedProtocol() == MEControllerProtocol.SOCKET) {
			host = view.getTextboxToken().getTextbox().getText();
		} else {
			host = view.getTextboxHost().getText();
			port = Integer.parseInt(view.getTextboxPort().getText());
		}

		// Set new settings
		Manager.get().getCurrentScenarioDetails().setControllerHost(host);
		Manager.get().getCurrentScenarioDetails().setControllerPort(port);
		Manager.get().getCurrentScenarioDetails().setControllerProtocol(protocol);
		Manager.get().getCurrentScenarioDetails().setControllerName(controller);

		// Store new settings in the database
		Manager.get().storeAccountDetails();

		// Fire event controllerChanged and load new controller environment
		EventControl.get().fireEvent(new MEControllerEvent(EventType.CONTROLLER_CHANGED));
		ScenarioManager.get().loadDefinitionFromCurrentController();
	}
}
