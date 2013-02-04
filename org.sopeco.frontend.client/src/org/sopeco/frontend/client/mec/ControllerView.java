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

import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.widget.SmallTableLabel;
import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
public class ControllerView extends FlowPanel {

	private static final String IMG_RECHECK = "images/reload.png";
	private static final String VIEW_CSS_CLASS = "controllerView";
	private static final String TB_HOST_CSS = "tbHost";
	private static final String TB_PORT_CSS = "tbPort";
	private static final String CB_PROTOCOL_CSS = "cbProtocol";
	private static final String HTML_STATUS_CSS = "htmlStatus";
	private static final String IMAGE_STATUS_CSS = "imageStatus";
	private static final String IMAGE_RECHECK_CSS = "imageReCheck";
	private static final String PANEL_STATUS_CSS = "panelStatus";
	private static final String PANEL_BUTTONS_CSS = "panelButtons";

	private boolean hasInfoText, hasButtons;

	private Paragraph infoText;
	private FlowPanel panelStatus;
	private ComboBox cbProtocol, cbController;
	private TextBox tbHostname, tbPort;
	private Button btnOk, btnCancel;
	private Image imgStatus, imgReCheck;
	private FlexTable table;

	private ComboBox cbSocketController;

	/** */
	public enum ViewStatus {
		/** */
		ONLINE, UNKNOWN, OFFLINE, CHECKING
	}

	public ControllerView(boolean pHasInfoText, boolean pHasButtons) {
		hasButtons = pHasButtons;
		hasInfoText = pHasInfoText;

		R.resc.cssControllerView().ensureInjected();

		init();
	}

	/**
	 * Initialize all necessary elements..
	 */
	private void init() {
		addStyleName(VIEW_CSS_CLASS);

		Headline headline = new Headline(R.get("meController"));
		table = new FlexTable();
		FlowPanel panelButtons = new FlowPanel();

		panelStatus = new FlowPanel();
		cbProtocol = new ComboBox();
		cbController = new ComboBox();
		cbSocketController = new ComboBox();
		tbHostname = new TextBox();
		tbPort = new TextBox();
		btnOk = new Button(R.get("Ok"));
		btnCancel = new Button(R.get("Cancel"));
		imgReCheck = new Image(IMG_RECHECK);
		imgStatus = new Image();

		infoText = new Paragraph(R.get("mecInformation"));

		cbProtocol.setWidth(100 + "px");
		cbProtocol.addStyleName(CB_PROTOCOL_CSS);
		cbProtocol.setEditable(false);
		cbProtocol.addItem("rmi://");
		cbProtocol.addItem("http://");
		cbProtocol.addItem("socket://");

		cbSocketController.setWidth("");
		cbSocketController.setEditable(false);

		tbHostname.setText("localhost");
		tbHostname.addStyleName(TB_HOST_CSS);

		tbPort.setText("1099");
		tbPort.addStyleName(TB_PORT_CSS);

		cbController.setEditable(false);
		cbController.setWidth("");

		HTML htmlStatus = new HTML(R.get("Status") + ":");
		htmlStatus.addStyleName(HTML_STATUS_CSS);

		imgStatus.addStyleName(IMAGE_STATUS_CSS);

		imgReCheck.addStyleName(IMAGE_RECHECK_CSS);
		imgReCheck.setTitle(R.get("checkAgain"));

		panelStatus.addStyleName(PANEL_STATUS_CSS);
		panelStatus.add(htmlStatus);
		panelStatus.add(imgStatus);
		panelStatus.add(imgReCheck);

		panelButtons.addStyleName(PANEL_BUTTONS_CSS);
		if (hasButtons) {
			panelButtons.add(btnOk);
			panelButtons.add(btnCancel);

		}

		table.setWidget(0, 0, new SmallTableLabel("Protocoll"));
		table.setWidget(0, 1, new SmallTableLabel("Host"));
		table.setWidget(0, 2, new SmallTableLabel("Port"));
		table.setWidget(2, 0, new SmallTableLabel("Controller"));

		table.setWidget(1, 0, cbProtocol);
		table.setWidget(1, 1, tbHostname);
		table.setWidget(1, 2, tbPort);
		table.setWidget(3, 0, cbController);
		table.setWidget(4, 0, panelStatus);
		table.setWidget(4, 1, panelButtons);

		table.getFlexCellFormatter().setColSpan(3, 0, 3);
		table.getFlexCellFormatter().setColSpan(4, 1, 2);

		table.getFlexCellFormatter().setWidth(1, 0, "125px");
		// table.getFlexCellFormatter().setWidth(1, 2, "1px");

		add(headline);
		if (hasInfoText) {
			add(infoText);
		}
		add(table);

		setViewStatus(ViewStatus.UNKNOWN);
	}

	boolean scVisible = false;

	public void switchToSocketCotnroller(boolean bool) {
		if (bool == scVisible) {
			return;
		}
		scVisible = bool;
		if (bool) {
			table.clearCell(0, 1);
			table.removeCell(0, 2);
			table.clearCell(1, 1);
			table.removeCell(1, 2);
			table.setWidget(0, 1, new SmallTableLabel("Connected MECApplications"));
			table.getFlexCellFormatter().setColSpan(0, 1, 2);
			table.getFlexCellFormatter().setColSpan(1, 1, 2);
			table.setWidget(1, 1, cbSocketController);
		} else {
			table.clearCell(0, 1);
			table.clearCell(1, 1);
			table.getFlexCellFormatter().setColSpan(0, 1, 1);
			table.getFlexCellFormatter().setColSpan(1, 1, 1);
			table.setWidget(0, 1, new SmallTableLabel("Host"));
			table.setWidget(0, 2, new SmallTableLabel("Port"));
			table.setWidget(1, 1, tbHostname);
			table.setWidget(1, 2, tbPort);
		}
	}

	/**
	 * 
	 * @param valueHandler
	 * @param clickHandler
	 */
	public void addAllHandler(ValueChangeHandler<String> valueHandler, ClickHandler clickHandler) {
		imgReCheck.addClickHandler(clickHandler);

		if (hasButtons) {
			btnOk.addClickHandler(clickHandler);
			btnCancel.addClickHandler(clickHandler);
		}

		cbProtocol.addValueChangeHandler(valueHandler);
		tbHostname.addValueChangeHandler(valueHandler);
		tbPort.addValueChangeHandler(valueHandler);
		cbProtocol.addValueChangeHandler(valueHandler);
		cbSocketController.addValueChangeHandler(valueHandler);
	}

	/**
	 * 
	 * @param status
	 */
	public void setViewStatus(ViewStatus status) {

		boolean btnOkEnabled = false;
		String btnOkText = R.get("Ok");
		String imgStatusUrl = "images/";
		String imgTitle = "";
		double imgRecheckOpacity = 0.8;

		switch (status) {
		case ONLINE:
			btnOkEnabled = true;
			imgStatusUrl += "status-green.png";
			imgTitle = "Controller online";
			break;
		case OFFLINE:
			imgStatusUrl += "status-red.png";
			imgTitle = "Controller offline";
			break;
		case CHECKING:
			btnOkText = R.get("checking");
			imgStatusUrl += "loader_circle.gif";
			imgTitle = "Checking controller..";
			imgRecheckOpacity = 0.2D;
			break;
		case UNKNOWN:
		default:
			imgStatusUrl += "status-gray.png";
			imgTitle = "Unknown state";
		}

		btnOk.setEnabled(btnOkEnabled);
		btnOk.setText(btnOkText);

		imgStatus.setUrl(imgStatusUrl);
		imgStatus.setTitle(imgTitle);

		imgReCheck.getElement().getStyle().setOpacity(imgRecheckOpacity);
	}

	/**
	 * Sets the given array as the items of the controller ComboBox.
	 * 
	 * @param controller
	 */
	public void setAvailableController(String[] controller) {
		String selectedItem = cbController.getText();
		cbController.clear();

		for (String item : controller) {
			cbController.addItem(item);
		}

		cbController.setSelectedText(selectedItem);
	}

	/**
	 * @return the cbProtocol
	 */
	public ComboBox getCbProtocol() {
		return cbProtocol;
	}

	public ComboBox getCbSocketController() {
		return cbSocketController;
	}

	/**
	 * @return the cbController
	 */
	public ComboBox getCbController() {
		return cbController;
	}

	/**
	 * @return the tbHostname
	 */
	public TextBox getTbHostname() {
		return tbHostname;
	}

	/**
	 * @return the tbPort
	 */
	public TextBox getTbPort() {
		return tbPort;
	}

	/**
	 * @return the btnOk
	 */
	public Button getBtnOk() {
		return btnOk;
	}

	/**
	 * @return the btnCancel
	 */
	public Button getBtnCancel() {
		return btnCancel;
	}

	/**
	 * @return the imgRecheck
	 */
	public Image getImgReCheck() {
		return imgReCheck;
	}

}
