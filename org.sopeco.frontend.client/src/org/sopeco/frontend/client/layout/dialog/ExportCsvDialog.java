package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.widget.SmallTableLabel;
import org.sopeco.frontend.shared.helper.Base64;
import org.sopeco.gwt.widgets.ExtendedTextBox;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

	private static final String DOWNLOAD_DATASET_URL = "sopeco_frontend/dataset";
	private static ExportCsvDialog dialog;

	private FlowPanel contentWrapper;
	private Headline headline;
	private Paragraph infoText;
	private ExtendedTextBox tbValueSeparator, tbCommentSeparator, tbDecimalDeimiter;
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
		tbCommentSeparator = new ExtendedTextBox("#", false);
		tbDecimalDeimiter = new ExtendedTextBox(".", false);

		tbValueSeparator.setMaxLength(1);
		tbCommentSeparator.setMaxLength(1);
		tbDecimalDeimiter.setMaxLength(1);

		loadSavedSeparator();

		FlexTable table = new FlexTable();

		table.setWidget(0, 0, new SmallTableLabel("Value Separator"));
		table.setWidget(0, 1, new SmallTableLabel("Comment Separator"));
		table.setWidget(0, 2, new SmallTableLabel("Decimal Delimiter"));
		table.setWidget(1, 0, tbValueSeparator);
		table.setWidget(1, 1, tbCommentSeparator);
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
		if (Manager.get().getAccountDetails().getCsvCommentSeparator() != null) {
			tbCommentSeparator.setText("" + Manager.get().getAccountDetails().getCsvCommentSeparator());
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
		Manager.get().getAccountDetails().setCsvCommentSeparator(tbCommentSeparator.getText());
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
			separator.append(tbCommentSeparator.getText());
			separator.append(tbDecimalDeimiter.getText());

			parameter += "|" + Base64.encodeString(separator.toString());

			String downloadUrl = DOWNLOAD_DATASET_URL + "?param=" + Base64.encodeString(parameter.toString());

			Window.open(downloadUrl, "_blank", "");
			hide();

			saveSeparator();
		}
	}

	/**
	 * 
	 * @param downloadParameter
	 */
	public static void show(String downloadParameter) {
		if (dialog == null) {
			dialog = new ExportCsvDialog();
		}
		dialog.setParameter(downloadParameter);
		dialog.center();
	}
}
