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

import org.sopeco.gwt.widgets.ExtendedTextBox;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SmallTableLabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExportCsvDialog extends DialogBox implements ClickHandler {

	private static final String DOWNLOAD_DATASET_URL = "dataset";
	private static ExportCsvDialog dialog;

	private FlowPanel contentWrapper;
	private Headline headline;
	private Paragraph infoText;
	private ExtendedTextBox tbValueSeparator, tbQuoteChar, tbDecimalDeimiter;
	private Button btnExport, btnCancel;

	private String parameter;

	private ExportCsvDialog() {
		super(false, true);
		init();

	}

	/**
	 * @param pParameter
	 *            the parameter to set
	 */
	private void setParameter(String pParameter) {
		parameter = pParameter;
	}

	/**
	 * 
	 */
	private void init() {
		setGlassEnabled(true);

		contentWrapper = new FlowPanel();
		contentWrapper.getElement().setId("exportCsvDialog");

		headline = new Headline(R.get("exportToCsv"));
		infoText = new Paragraph(R.get("csvExportDesc"));

		tbValueSeparator = new ExtendedTextBox(";", false);
		tbQuoteChar = new ExtendedTextBox("'", false);
		tbDecimalDeimiter = new ExtendedTextBox(".", false);

		tbValueSeparator.setMaxLength(1);
		tbQuoteChar.setMaxLength(1);
		tbDecimalDeimiter.setMaxLength(1);

		loadSavedSeparator();

		FlexTable table = new FlexTable();

		table.setWidget(0, 0, new SmallTableLabel("Value Separator"));
		table.setWidget(0, 1, new SmallTableLabel("Quote Character"));
		table.setWidget(0, 2, new SmallTableLabel("Decimal Delimiter"));
		table.setWidget(1, 0, tbValueSeparator);
		table.setWidget(1, 1, tbQuoteChar);
		table.setWidget(1, 2, tbDecimalDeimiter);

		btnExport = new Button("Export");
		btnCancel = new Button(R.get("Cancel"));
		btnExport.addClickHandler(this);
		btnCancel.addClickHandler(this);

		FlowPanel btnWrapper = new FlowPanel();

		btnWrapper.add(btnExport);
		btnWrapper.add(btnCancel);

		contentWrapper.add(headline);
		contentWrapper.add(infoText);
		contentWrapper.add(table);
		contentWrapper.add(btnWrapper);

		add(contentWrapper);
	}

	/**
	 * Sets the, in the account stored, delimiters in the appropriate text
	 * boxes.
	 */
	private void loadSavedSeparator() {
		if (Manager.get().getAccountDetails().getCsvValueSeparator() != null) {
			tbValueSeparator.setText("" + Manager.get().getAccountDetails().getCsvValueSeparator());
		}
		if (Manager.get().getAccountDetails().getCsvQuoteChar() != null) {
			tbQuoteChar.setText("" + Manager.get().getAccountDetails().getCsvQuoteChar());
		}
		if (Manager.get().getAccountDetails().getCsvDecimalDelimiter() != null) {
			tbDecimalDeimiter.setText("" + Manager.get().getAccountDetails().getCsvDecimalDelimiter());
		}
	}

	/**
	 * Stores the entered separators in the account details.
	 */
	private void saveSeparator() {
		Manager.get().getAccountDetails().setCsvValueSeparator(tbValueSeparator.getText());
		Manager.get().getAccountDetails().setCsvCommentSeparator(tbQuoteChar.getText());
		Manager.get().getAccountDetails().setCsvDecimalDelimiter(tbDecimalDeimiter.getText());
		Manager.get().storeAccountDetails();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnCancel) {
			hide();
		} else if (event.getSource() == btnExport) {
			StringBuffer separator = new StringBuffer();
			separator.append(tbValueSeparator.getText());
			separator.append(tbQuoteChar.getText());
			separator.append(tbDecimalDeimiter.getText());

			String url = GWT.getModuleBaseURL() + DOWNLOAD_DATASET_URL;

			StringBuilder sb = new StringBuilder();
			sb.append("seperator").append("=").append(URL.encodeQueryString(separator.toString())).append("&");
			sb.append("timestamp").append("=").append(URL.encodeQueryString(timestamp)).append("&");
			sb.append("experimentName").append("=").append(URL.encodeQueryString(experimentName)).append("&");
			sb.append("controllerURL").append("=").append(URL.encodeQueryString(controllerURL)).append("&");
			sb.append("scenarioName").append("=").append(URL.encodeQueryString(scenarioName));

			Window.open(url + "?" + sb.toString(), "_blank", "");

			hide();

			saveSeparator();
		}
	}

	private String timestamp;
	private String experimentName;
	private String controllerURL;
	private String scenarioName;

	public static void show(String pTimestamp, String pExperimentName, String pControllerUrl, String pScenarioName) {
		if (dialog == null) {
			dialog = new ExportCsvDialog();
		}

		dialog.timestamp = pTimestamp;
		dialog.experimentName = pExperimentName;
		dialog.controllerURL = pControllerUrl;
		dialog.scenarioName = pScenarioName;

		dialog.center();
	}

	/**
	 * 
	 * @param downloadParameter
	 */
	// public static void show(String downloadParameter) {
	// if (dialog == null) {
	// dialog = new ExportCsvDialog();
	// }
	// dialog.setParameter(downloadParameter);
	// dialog.center();
	// }
}
