package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
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

	private Button exportXML;
	private TextArea textarea;

	private VerticalPanel panel;

	private ExportBox() {
		setModal(true);
		setAutoHideEnabled(true);
		setGlassEnabled(true);

		panel = new VerticalPanel();
		panel.getElement().getStyle().setMargin(BOX_MARGIN, Unit.EM);

		HTML headline = new HTML("<h3 style=\"margin-top:0;\">Export</h3>");

		exportXML = new Button(R.get("exportInFile"));
		exportXML.getElement().getStyle().setFloat(Float.RIGHT);
		exportXML.addClickHandler(this);

		textarea = new TextArea();
		textarea.setWidth("700px");
		textarea.setHeight("200px");

		panel.add(headline);
		panel.add(textarea);
		panel.add(exportXML);

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
		Window.open(EXPORT_XML_URL, "_self", "");
		hide();
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
