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
package org.sopeco.frontend.client.mec;

import java.util.Map;

import org.sopeco.frontend.client.SoPeCoUI;
import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.helper.INotifyHandler.Result;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.mec.ControllerView.ViewStatus;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.MEControllerProtocol;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
@SuppressWarnings("rawtypes")
public class MECController extends FlowPanel implements ValueChangeHandler<String>, ClickHandler, INotifyHandler,
		HasValueChangeHandlers<Boolean> {

	private boolean mecIsOnline = false;
	private ControllerView view;

	private Map<String, String[]> socketController;

	public MECController(boolean hasInfoText, boolean hasButtons) {
		view = new ControllerView(hasInfoText, hasButtons);
		view.addAllHandler(this, this);
	}

	public Widget getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getImgReCheck()) {
			checkEnteredHost();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == view.getCbProtocol()) {
			if (view.getCbProtocol().getSelectedIndex() == 2) {
				updateSocketCB();
				view.switchToSocketCotnroller(true);
			} else {
				view.switchToSocketCotnroller(false);
				view.getTbPort().setText(
						"" + ControllerInteraction.DEFAULT_PORTS[view.getCbProtocol().getSelectedIndex()]);
			}
		} else if (event.getSource() == view.getTbHostname()) {
			checkEnteredHost();
		} else if (event.getSource() == view.getTbPort()) {
			checkEnteredHost();
		} else if (event.getSource() == view.getCbSocketController()) {
			if (!socketController.isEmpty()) {
				view.setAvailableController(socketController.get(view.getCbSocketController().getText()));
			}
		}
	}

	private void updateSocketCB() {
		RPC.getGetRPC().getConnectedSocketController(new AsyncCallback<Map<String, String[]>>() {
			@Override
			public void onSuccess(Map<String, String[]> result) {
				socketController = result;
				view.getCbSocketController().clear();
				if (result.size() == 0) {
					view.getCbSocketController().setEnabled(false);
					view.getCbSocketController().addItem("No MEController connected");
				} else {
					view.getCbSocketController().setEnabled(true);
					for (String s : result.keySet()) {
						view.getCbSocketController().addItem(s);
					}

				}

				if (!socketController.isEmpty()) {
					view.setAvailableController(socketController.get(view.getCbSocketController().getText()));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				SoPeCoUI.get().onUncaughtException(caught);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void checkEnteredHost() {
		// TODO - temporary workaround
		if (view.getCbProtocol().getSelectedIndex() == 2) {
			view.setViewStatus(ViewStatus.ONLINE);
			ValueChangeEvent.fire(this, true);
			mecIsOnline = true;
			return;
		}
		// *****
		
		String host = view.getTbHostname().getText();
		int port = Integer.parseInt(view.getTbPort().getText());

		ControllerInteraction.isPortReachable(host, port, this);

		view.setViewStatus(ViewStatus.CHECKING);
		mecIsOnline = false;
		ValueChangeEvent.fire(this, mecIsOnline);
	}

	@SuppressWarnings("unchecked")
	private void retrieveController() {
		String host = view.getTbHostname().getText();
		int port = Integer.parseInt(view.getTbPort().getText());
		MEControllerProtocol protocol;
		switch (view.getCbProtocol().getSelectedIndex()) {
		case 0:
			protocol = MEControllerProtocol.RMI;
			break;
		case 1:
			protocol = MEControllerProtocol.REST_HTTP;
			break;
		case 2:
			protocol = MEControllerProtocol.SOCKET;
			return;
		default:
			throw new IllegalStateException();
		}

		ControllerInteraction.retrieveController(protocol, host, port, this);
	}

	@Override
	public void call(Result result) {
		if (result == null) {
			mecIsOnline = false;
			view.setViewStatus(ViewStatus.OFFLINE);
			return;
		} else if (result.getKey().equals(ControllerInteraction.KEY_PORT_REACHABLE)) {
			boolean value = (Boolean) result.getValue();
			if (result.wasSuccessful() && value) {
				retrieveController();
			} else {
				mecIsOnline = false;
				view.setViewStatus(ViewStatus.OFFLINE);
				view.getCbController().clear();
				ValueChangeEvent.fire(this, false);
			}
		} else if (result.getKey().equals(ControllerInteraction.KEY_RETRIEVE_MEC)) {
			String[] value = (String[]) result.getValue();
			view.setAvailableController(value);

			if (value.length > 0) {
				mecIsOnline = true;
				view.setViewStatus(ViewStatus.ONLINE);
				view.getCbController().setEnabled(true);
			} else {
				mecIsOnline = false;
				view.setViewStatus(ViewStatus.UNKNOWN);
				view.getCbController().addItem("No controller avaialble");
				view.getCbController().setEnabled(false);
			}
			ValueChangeEvent.fire(this, mecIsOnline);
		}
	}

	/**
	 * @return the mecIsOnline
	 */
	public boolean isMecOnline() {
		return mecIsOnline;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return view.getCbProtocol().getText() + view.getTbHostname().getText() + ":" + view.getTbPort().getText() + "/"
				+ view.getCbController().getText();
	}

	/**
	 * 
	 */
	public void refreshUI() {
		String protocol = Manager.get().getCurrentScenarioDetails().getControllerProtocol();
		if (protocol.equals("rmi://")) {
			view.switchToSocketCotnroller(false);
			view.getCbProtocol().setSelectedIndex(0);
		} else if (protocol.equals("http://")) {
			view.switchToSocketCotnroller(false);
			view.getCbProtocol().setSelectedIndex(1);
		} else if (protocol.equals("socket://")) {
			view.switchToSocketCotnroller(true);
			view.getCbProtocol().setSelectedIndex(2);	
			view.getCbSocketController().addItem(Manager.get().getCurrentScenarioDetails().getControllerHost());
		}

		view.getTbHostname().setText(Manager.get().getCurrentScenarioDetails().getControllerHost());
		view.getTbPort().setText("" + Manager.get().getCurrentScenarioDetails().getControllerPort());
		view.getCbController().setText(Manager.get().getCurrentScenarioDetails().getControllerName());

	}
}
