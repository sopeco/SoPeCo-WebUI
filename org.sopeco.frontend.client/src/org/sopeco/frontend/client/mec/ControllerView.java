package org.sopeco.frontend.client.mec;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.resources.FrontEndResources;
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

	/** */
	public enum ViewStatus {
		/** */
		ONLINE, UNKNOWN, OFFLINE, CHECKING
	}

	public ControllerView(boolean pHasInfoText, boolean pHasButtons) {
		hasButtons = pHasButtons;
		hasInfoText = pHasInfoText;

		FrontEndResources.loadControllerViewCSS();

		init();
	}

	/**
	 * Initialize all necessary elements..
	 */
	private void init() {
		addStyleName(VIEW_CSS_CLASS);

		Headline headline = new Headline(R.get("meController"));
		FlexTable table = new FlexTable();
		FlowPanel panelButtons = new FlowPanel();

		panelStatus = new FlowPanel();
		cbProtocol = new ComboBox();
		cbController = new ComboBox();
		tbHostname = new TextBox();
		tbPort = new TextBox();
		btnOk = new Button(R.get("Ok"));
		btnCancel = new Button(R.get("Cancel"));
		imgReCheck = new Image(IMG_RECHECK);
		imgStatus = new Image();

		infoText = new Paragraph("asd adfk jdsakfj askjf adkj fsk jfsak jsaklj askjf adkj fsk jfsak jsaklj dfaka sj");

		cbProtocol.setWidth(100);
		cbProtocol.addStyleName(CB_PROTOCOL_CSS);
		cbProtocol.setEditable(false);
		cbProtocol.addItem("rmi://");
		cbProtocol.addItem("http://");

		tbHostname.setText("localhost");
		tbHostname.addStyleName(TB_HOST_CSS);

		tbPort.setText("1099");
		tbPort.addStyleName(TB_PORT_CSS);

		cbController.setWidth(380);
		cbController.setEditable(false);

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

		table.getFlexCellFormatter().setWidth(1, 2, "1px");

		add(headline);
		if (hasInfoText) {
			add(infoText);
		}
		add(table);

		setViewStatus(ViewStatus.UNKNOWN);
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
