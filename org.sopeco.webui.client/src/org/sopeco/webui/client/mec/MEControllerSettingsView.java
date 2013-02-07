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

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.WrappedTextBox;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SmallTableLabel;
import org.sopeco.webui.shared.helper.MEControllerProtocol;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerSettingsView extends FlexTable {

	private static final String CSS_CLASS = "mecSettingsView";

	public enum StatusImage {
		RED, YELLOW, GREEN, GRAY, LOADING
	}

	private ComboBox cbProtocol;
	private ComboBox cbController;
	private TextBox tbHost;
	private TextBox tbPort;
	private WrappedTextBox tbToken;
	private WrappedTextBox tbPassword;
	private HTML htmlDescription;
	private FlowPanel panelStatus;
	private HTML htmlStatus;
	private Image imgReload;
	private Image imgStatus;

	public MEControllerSettingsView() {
		R.resc.cssMECSettingsView().ensureInjected();
		init();
	}

	private void init() {
		setWidth("100%");
		addStyleName(CSS_CLASS);

		htmlDescription = new HTML(R.lang.mecSettingDescription());

		cbProtocol = new ComboBox();
		cbProtocol.setWidth(100 + "px");
		cbProtocol.setEditable(false);
		cbProtocol.addItem("rmi://");
		cbProtocol.addItem("http://");
		cbProtocol.addItem("socket://");

		cbController = new ComboBox();
		cbController.setWidth("");
		cbController.setEditable(false);

		tbHost = new TextBox();
		tbHost.setWidth("90%");

		tbPort = new TextBox();
		tbPort.setWidth("40px");

		tbPassword = new WrappedTextBox();
		tbPassword.setAsPasswordTextbox();

		tbToken = new WrappedTextBox();

		htmlStatus = new HTML("unknown");

		imgReload = new Image(R.resc.imgReload());
		imgReload.addStyleName("reloadImage");

		imgStatus = new Image(R.resc.imgStatusGray());

		panelStatus = new FlowPanel();
		panelStatus.addStyleName("panelStatus");
		panelStatus.add(new HTML(R.lang.status() + ":"));
		panelStatus.add(imgStatus);
		panelStatus.add(htmlStatus);
		panelStatus.add(imgReload);
		panelStatus.add(new ClearDiv());

		setWidget(0, 0, htmlDescription);

		setWidget(1, 0, new SmallTableLabel(R.lang.protocol()));
		setWidget(2, 0, cbProtocol);
		setWidget(5, 0, new SmallTableLabel(R.lang.controller()));
		setWidget(6, 0, cbController);

		setWidget(7, 0, panelStatus);

		getFlexCellFormatter().setWidth(2, 0, "115px");

		getFlexCellFormatter().setColSpan(0, 0, 3);
		getFlexCellFormatter().setColSpan(6, 0, 3);
		getFlexCellFormatter().setColSpan(7, 0, 3);

		setStyleHostPort();
	}

	public MEControllerProtocol getSelectedProtocol() {
		switch (cbProtocol.getSelectedIndex()) {
		case 0:
			return MEControllerProtocol.RMI;
		case 1:
			return MEControllerProtocol.REST_HTTP;
		case 2:
			return MEControllerProtocol.SOCKET;
		default:
			throw new IllegalStateException("Illegal combobox selection.");
		}
	}

	public void setStyleHostPort() {
		setWidget(1, 1, new SmallTableLabel(R.lang.host() + " / " + R.lang.ipAddress()));
		setWidget(1, 2, new SmallTableLabel(R.lang.port()));

		setWidget(2, 1, tbHost);
		setWidget(2, 2, tbPort);

		getFlexCellFormatter().setWidth(2, 2, "1px");

		updatePasswordInput();
	}

	public void setStyleToken() {
		tbHost.removeFromParent();
		if (tbPort.isAttached()) {
			tbPort.removeFromParent();
			clearCell(1, 1);
			clearCell(1, 2);
		}

		// setWidget(1, 1, new SmallTableLabel(R.lang.mecAppToken()));
		setWidget(1, 1, new SmallTableLabel(R.lang.mecApplication() + " " + R.lang.ipAddress()));
		setWidget(2, 1, tbToken);

		updatePasswordInput();
	}

	public void updatePasswordInput() {
		boolean isProtected = false;
		if (isProtected) {
			setWidget(3, 0, new SmallTableLabel(R.lang.mecApplication() + " " + R.lang.password()));
			setWidget(4, 0, tbPassword);
			getFlexCellFormatter().setColSpan(3, 0, 3);
			getFlexCellFormatter().setColSpan(4, 0, 3);
		} else {
			if (isCellPresent(3, 0)) {
				clearCell(3, 0);
				clearCell(4, 0);
			}
		}
	}

	public void setStatusImage(StatusImage status) {
		switch (status) {
		case GRAY:
			imgStatus.setResource(R.resc.imgStatusGray());
			break;
		case GREEN:
			imgStatus.setResource(R.resc.imgStatusGreen());
			break;
		case RED:
			imgStatus.setResource(R.resc.imgStatusRed());
			break;
		case YELLOW:
			imgStatus.setResource(R.resc.imgStatusYellow());
			break;
		case LOADING:
			imgStatus.setResource(R.resc.imgLoadingIndicatorCircle());
			break;
		default:
			throw new IllegalStateException("No valid enum: " + status.toString());
		}
	}

	public ComboBox getComboBoxProtocol() {
		return cbProtocol;
	}

	public ComboBox getComboBoxController() {
		return cbController;
	}

	public TextBox getTextboxHost() {
		return tbHost;
	}

	public TextBox getTextboxPort() {
		return tbPort;
	}

	public WrappedTextBox getTextboxPassword() {
		return tbPassword;
	}

	public WrappedTextBox getTextboxToken() {
		return tbToken;
	}

	public Image getImgReload() {
		return imgReload;
	}

	public HTML getHtmlStatus() {
		return htmlStatus;
	}

}
