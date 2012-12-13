package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.Paragraph;

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
