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
package org.sopeco.webui.client.layout;

import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SoPeCoDialog;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExportBox extends SoPeCoDialog implements ClickHandler {

	private static final int WIDTH_PX = 700;
	private static final String EXPORT_XML_URL = "export?scenario";

	/**
	 * Displays this dialog.
	 */
	public static void showExportScenarioDialog() {
		new ExportBox().loadXMLandShow();
	}

	private Button btnExportXML, btnClose;
	private FlowPanel btnPanel;
	private VerticalPanel panel;
	private TextArea textarea;

	/**
	 * Constructor.
	 */
	private ExportBox() {
		super(false);

		panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(1, Unit.EM);

		setHeadline(R.lang.Export());
		setDraggable(true);
		setWidth(WIDTH_PX + "px");

		btnExportXML = new Button(R.lang.exportInFile());
		btnExportXML.getElement().getStyle().setFloat(Float.RIGHT);
		btnExportXML.addClickHandler(this);

		btnClose = new Button(R.lang.Close());
		btnClose.getElement().getStyle().setMarginLeft(1, Unit.EM);
		btnClose.getElement().getStyle().setFloat(Float.RIGHT);
		btnClose.addClickHandler(this);

		textarea = new TextArea();
		textarea.setWidth((WIDTH_PX - 10) + "px");
		textarea.setHeight("200px");

		btnPanel = new FlowPanel();
		btnPanel.add(btnClose);
		btnPanel.add(btnExportXML);

		panel.add(textarea);
		panel.add(btnPanel);

		setContentWidget(panel);
	}

	/**
	 * Loads a XML representation of the current scenario.
	 */
	private void loadCurrentAsXML() {
		RPC.getScenarioManager().getScenarioAsXML(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				throw new RuntimeException(caught);
			}

			@Override
			public void onSuccess(String result) {
				textarea.setText(result);
			}
		});
	}

	/**
	 * Calls the {@link #loadCurrentAsXML()} method to load the XML
	 * representation of the scenario and display the data in the textarea of
	 * this dialog.
	 */
	private void loadXMLandShow() {
		loadCurrentAsXML();
		center();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnExportXML) {
			String url = GWT.getModuleBaseURL() + EXPORT_XML_URL;
			Window.open(url, "_blank", "");
		}
		hide();
	}
}
