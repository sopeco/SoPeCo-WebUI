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
package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.MEControllerEvent;
import org.sopeco.frontend.client.event.MEControllerEvent.EventType;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.manager.Manager.ControllerStatus;
import org.sopeco.frontend.client.mec.ControllerView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MEControllerBox extends DialogBox implements ValueChangeHandler<String>, ClickHandler {

	private static MEControllerBox box;

	private MECController mecController;
	private ControllerView controllerView;

	private MEControllerBox() {
		super(false, false);
		init();
	}

	private void init() {
		setGlassEnabled(true);

		mecController = new MECController(false, true);
		controllerView = (ControllerView) mecController.getView();

		controllerView.getBtnCancel().addClickHandler(this);
		controllerView.getBtnOk().addClickHandler(this);

		add(mecController.getView());
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == controllerView.getBtnCancel()) {
			hide();
		} else if (event.getSource() == controllerView.getBtnOk()) {
			save();
		}
	}

	private void save() {
//		if (!Manager.get().getControllerUrl().equals(mecController.getUrl())) {
			Manager.get().getCurrentScenarioDetails().setControllerHost(controllerView.getTbHostname().getText());
			Manager.get().getCurrentScenarioDetails().setControllerProtocol(controllerView.getCbProtocol().getText());
			Manager.get().getCurrentScenarioDetails()
					.setControllerPort(Integer.parseInt(controllerView.getTbPort().getText()));
			Manager.get().getCurrentScenarioDetails().setControllerName(controllerView.getCbController().getText());
			Manager.get().storeAccountDetails();

			EventControl.get().fireEvent(new MEControllerEvent(EventType.CONTROLLER_CHANGED));

			ScenarioManager.get().loadDefinitionFromCurrentController();
//		}
		// Manager.get().setControllerLastCheck(latestCheckRun);
		Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);

		hide();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	private static MEControllerBox getBox() {
		if (box == null) {
			box = new MEControllerBox();
		}
		return box;
	}

	public static void showBox() {
		getBox().mecController.refreshUI();
		getBox().center();
	}
}
