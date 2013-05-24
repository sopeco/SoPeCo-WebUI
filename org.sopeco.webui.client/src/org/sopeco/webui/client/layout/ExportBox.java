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
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExportBox extends DialogBox implements ClickHandler {

	private static ExportBox exportBox;

	private static final String EXPORT_XML_URL = "sopeco_frontend/export?scenario";
	private static final double BOX_MARGIN = 0.5D;

	private Button exportXML, btnClose;
	private TextArea textarea;

	private VerticalPanel panel;
	private FlowPanel btnPanel;

	private ExportBox() {
		setModal(true);
		setAutoHideEnabled(true);
		setGlassEnabled(true);

		panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(BOX_MARGIN, Unit.EM);

		HTML headline = new HTML("<h3 style=\"margin-top:0;\">Export</h3>");

		exportXML = new Button(R.lang.exportInFile());
		exportXML.getElement().getStyle().setFloat(Float.RIGHT);
		exportXML.addClickHandler(this);

		btnClose = new Button(R.lang.Close());
		btnClose.getElement().getStyle().setMarginLeft(1, Unit.EM);
		btnClose.getElement().getStyle().setFloat(Float.RIGHT);
		btnClose.addClickHandler(this);

		textarea = new TextArea();
		textarea.setWidth("700px");
		textarea.setHeight("200px");

		btnPanel = new FlowPanel();
		btnPanel.add(btnClose);
		btnPanel.add(exportXML);

		panel.add(headline);
		panel.add(textarea);
		panel.add(btnPanel);

		add(panel);
	}

	/**
	 * @return the textarea
	 */
	public TextArea getTextarea() {
		return textarea;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == exportXML) {
			Window.open(EXPORT_XML_URL, "_self", "");
			hide();
		} else if (event.getSource() == btnClose) {
			hide();
		}
	}

	public static void showExportBox() {
		if (exportBox == null) {
			exportBox = new ExportBox();
		}

		loadCurrentAsXML();

		exportBox.center();
	}

	private static void loadCurrentAsXML() {
		RPC.getScenarioManager().getScenarioAsXML(new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				exportBox.getTextarea().setText(result);
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	public static void closeExportBox() {
		if (exportBox == null) {
			return;
		}

		exportBox.hide();
	}
}
