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
package org.sopeco.webui.client.layout.dialog;

import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExportToRDialog extends DialogBox implements ClickHandler {

	private static ExportToRDialog dialog;

	private FlowPanel contentWrapper;
	private Headline headline;
	private Paragraph infoText;
	private Button btnExport, btnCancel;
	private TextArea taValue;

	private String rValue;

	private ExportToRDialog() {
		super(false, true);
		rValue = "";
		init();
	}

	/**
	 * @param pParameter
	 *            the parameter to set
	 */
	private void setRValue(String pValue) {
		rValue = pValue;
		taValue.setText(rValue);
	}

	/**
	 * 
	 */
	private void init() {
		setGlassEnabled(true);

		contentWrapper = new FlowPanel();
		contentWrapper.getElement().setId("exportCsvDialog");

		headline = new Headline(R.get("exportToR"));
		infoText = new Paragraph(R.get("rExportDesc"));

		taValue = new TextArea();
		taValue.setSize("500px", "160px");
		taValue.getElement().getStyle().setProperty("whiteSpace", "nowrap");

		// btnExport = new Button("Export");
		btnCancel = new Button(R.get("Close"));
		// btnExport.addClickHandler(this);
		btnCancel.addClickHandler(this);

		FlowPanel btnWrapper = new FlowPanel();

		// btnWrapper.add(btnExport);
		btnWrapper.add(btnCancel);

		contentWrapper.add(headline);
		contentWrapper.add(infoText);
		contentWrapper.add(taValue);
		contentWrapper.add(btnWrapper);

		add(contentWrapper);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnCancel) {
			hide();
		} else if (event.getSource() == btnExport) {
		}
	}

	/**
	 * 
	 * @param pRValue
	 */
	public static void show(String pRValue) {
		if (dialog == null) {
			dialog = new ExportToRDialog();
		}
		dialog.setRValue(pRValue);
		dialog.center();
	}
}
