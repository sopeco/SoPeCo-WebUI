package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MEControllerBox extends DialogBox implements ValueChangeHandler<String>, ClickHandler {

	private static final int[] DEFAULT_PORTS = new int[] { 1099, 80, 443 };
	private static MEControllerBox box;
	private static String controllerUrl = "rmi://localhost:1099/UIPriceTagsController";

	private ComboBox cbProtocol, cbController;
	private TextBox tbHostname, tbPort;
	private Button btnOk, btnCancel;

	private MEControllerBox() {
		init();
	}

	/**
	 * Initialize all necessary elements..
	 */
	private void init() {
		setModal(true);
		setGlassEnabled(true);

		FlowPanel wrapper = new FlowPanel();

		Headline headline = new Headline(R.get("meController"));
		FlexTable table = new FlexTable();
		FlowPanel panelButtons = new FlowPanel();

		cbProtocol = new ComboBox();
		cbController = new ComboBox();
		tbHostname = new TextBox();
		tbPort = new TextBox();
		btnOk = new Button(R.get("Ok"));
		btnCancel = new Button(R.get("Cancel"));

		wrapper.getElement().getStyle().setPadding(0.5, Unit.EM);
		wrapper.getElement().getStyle().setPaddingTop(0, Unit.EM);

		headline.getElement().getStyle().setMargin(0, Unit.PX);

		cbProtocol.setWidth(100);
		cbProtocol.setEditable(false);
		cbProtocol.addItem("rmi://");
		cbProtocol.addItem("http://");
		cbProtocol.addItem("https://");
		cbProtocol.addValueChangeHandler(this);

		tbHostname.setWidth("175px");
		tbHostname.getElement().getStyle().setMargin(0, Unit.PX);
		tbPort.setWidth("40px");
		tbPort.getElement().getStyle().setMargin(0, Unit.PX);
		cbController.setWidth(345);

		btnCancel.getElement().getStyle().setMarginLeft(6, Unit.PX);
		btnCancel.addClickHandler(this);
		btnCancel.setWidth("80px");

		btnOk.setWidth("80px");
		btnOk.addClickHandler(this);

		panelButtons.add(btnOk);
		panelButtons.add(btnCancel);
		panelButtons.getElement().getStyle().setProperty("textAlign", "right");
		panelButtons.getElement().getStyle().setMarginTop(6, Unit.PX);

		table.getElement().getStyle().setProperty("borderSpacing", "0");

		table.setWidget(0, 0, new SLabel("Protocoll"));
		table.setWidget(0, 1, new SLabel("Host"));
		table.setWidget(0, 2, new SLabel("Port"));
		table.setWidget(2, 0, new SLabel("Controller"));

		table.setWidget(1, 0, cbProtocol);
		table.setWidget(1, 1, tbHostname);
		table.setWidget(1, 2, tbPort);
		table.setWidget(3, 0, cbController);
		table.setWidget(4, 1, panelButtons);

		table.getFlexCellFormatter().setColSpan(3, 0, 3);
		table.getFlexCellFormatter().setColSpan(4, 1, 2);

		wrapper.add(headline);
		wrapper.add(table);

		add(wrapper);
	}

	private void refreshUI() {
		String pattern = "(rmi|http|https):\\/\\/([a-zA-Z0-9_-]+)(:(\\d{1,5}))?\\/([a-zA-Z0-9_-]+)";

		RegExp regExp = RegExp.compile(pattern);
		MatchResult matcher = regExp.exec(controllerUrl);
		boolean matchFound = (matcher != null);

		if (matchFound) {
			if (matcher.getGroupCount() == 6) {
				if (matcher.getGroup(1).equals("rmi")) {
					cbProtocol.setSelectedIndex(0);
				} else if (matcher.getGroup(1).equals("http")) {
					cbProtocol.setSelectedIndex(1);
				} else if (matcher.getGroup(1).equals("https")) {
					cbProtocol.setSelectedIndex(2);
				}

				tbHostname.setText(matcher.getGroup(2));
				tbPort.setText(matcher.getGroup(4));
				cbController.setText(matcher.getGroup(5));
			}
		}

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		tbPort.setText("" + DEFAULT_PORTS[cbProtocol.getSelectedIndex()]);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnCancel) {
			hide();
		} else if (event.getSource() == btnOk) {
			StringBuffer newControllerUrl = new StringBuffer(cbProtocol.getText());
			newControllerUrl.append(tbHostname.getText());
			newControllerUrl.append(":");
			newControllerUrl.append(tbPort.getText());
			newControllerUrl.append("/");
			newControllerUrl.append(cbController.getText());

			controllerUrl = newControllerUrl.toString();

			GWT.log(controllerUrl);
			
			hide();
		}
	}

	private static MEControllerBox getBox() {
		if (box == null) {
			box = new MEControllerBox();
		}
		return box;
	}

	public static void showBox() {
		getBox().refreshUI();
		getBox().center();
	}

	private class SLabel extends HTML {
		public SLabel(String text) {
			super(text);
			addStyleName("smallLabel");
		}
	}
}
