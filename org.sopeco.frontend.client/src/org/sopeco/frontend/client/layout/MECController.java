package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.mec.ControllerInteraction;
import org.sopeco.frontend.client.mec.ControllerView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MECController extends FlowPanel implements ValueChangeHandler<String>, ClickHandler,
		INotifyHandler<Boolean> {

	private static final int[] DEFAULT_PORTS = new int[] { 1099, 80, 443 };

	private long latestCheckRun;
	private boolean isChecking = false;

	private ControllerView view;

	public MECController() {
		view = new ControllerView(true, false);
		view.addAllHandler(this, this);
	}

	public Widget getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == view.getCbProtocol()) {
			view.getTbPort().setText("" + DEFAULT_PORTS[view.getCbProtocol().getSelectedIndex()]);
		} else if (event.getSource() == view.getTbHostname()) {
			checkEnteredHost();
		} else if (event.getSource() == view.getTbPort()) {
			checkEnteredHost();
		}
	}

	private void checkEnteredHost() {
		String host = view.getTbHostname().getText();
		int port = Integer.parseInt(view.getTbPort().getText());
		ControllerInteraction.isPortReachable(host, port, this);
	}

	@Override
	public void call(boolean success, Boolean result) {
		GWT.log("" + result);
	}

	// /**
	// * Initialize all necessary elements..
	// */
	// private void init() {
	//
	// Headline headline = new Headline(R.get("meController"));
	// FlexTable table = new FlexTable();
	// FlowPanel panelButtons = new FlowPanel();
	//
	// panelStatus = new FlowPanel();
	// cbProtocol = new ComboBox();
	// cbController = new ComboBox();
	// tbHostname = new TextBox();
	// tbPort = new TextBox();
	// btnOk = new Button(R.get("Ok"));
	// btnCancel = new Button(R.get("Cancel"));
	// imgRecheck = new Image(IMG_RECHECK);
	//
	// getElement().getStyle().setPadding(1, Unit.EM);
	//
	// headline.getElement().getStyle().setMargin(0, Unit.PX);
	//
	// infoText = new
	// Paragraph("asd adfk jdsakfj askjf adkj fsk jfsak jsaklj askjf adkj fsk jfsak jsaklj dfaka sj");
	// infoText.getElement().getStyle().setProperty("textAlign", "justify");
	// infoText.getElement().getStyle().setColor("#633");
	// infoText.getElement().getStyle().setFontWeight(FontWeight.NORMAL);
	// infoText.getElement().getStyle().setMarginBottom(6, Unit.PX);
	//
	// cbProtocol.setWidth(100);
	// cbProtocol.setEditable(false);
	// cbProtocol.addItem("rmi://");
	// cbProtocol.addItem("http://");
	// cbProtocol.addItem("https://");
	// cbProtocol.addValueChangeHandler(this);
	//
	// tbHostname.setWidth("175px");
	// tbHostname.getElement().getStyle().setMargin(0, Unit.PX);
	// tbHostname.addValueChangeHandler(this);
	// tbHostname.setText("localhost");
	//
	// tbPort.setWidth("40px");
	// tbPort.getElement().getStyle().setMargin(0, Unit.PX);
	// tbPort.addValueChangeHandler(this);
	// tbPort.setText("1099");
	//
	// cbController.setWidth(380);
	// cbController.setEditable(false);
	//
	// btnCancel.getElement().getStyle().setMarginLeft(6, Unit.PX);
	// btnCancel.addClickHandler(this);
	// btnCancel.setWidth("80px");
	//
	// btnOk.setWidth("80px");
	// btnOk.addClickHandler(this);
	//
	// HTML htmlStatus = new HTML(R.get("Status") + ":");
	// htmlStatus.getElement().getStyle().setColor("#555");
	// htmlStatus.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
	//
	// imgStatus = new Image();
	// imgStatus.getElement().getStyle().setMarginLeft(0.5, Unit.EM);
	// imgStatus.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
	// imgStatus.getElement().getStyle().setPaddingBottom(2, Unit.PX);
	//
	// imgRecheck.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
	// imgRecheck.getElement().getStyle().setPaddingBottom(2, Unit.PX);
	// imgRecheck.getElement().getStyle().setMarginLeft(1, Unit.EM);
	// imgRecheck.getElement().getStyle().setCursor(Cursor.POINTER);
	// imgRecheck.getElement().getStyle().setOpacity(0.8);
	// imgRecheck.setHeight("14px");
	// imgRecheck.setWidth("14px");
	// imgRecheck.setTitle(R.get("checkAgain"));
	// imgRecheck.addClickHandler(this);
	//
	// panelStatus.getElement().getStyle().setMarginTop(6, Unit.PX);
	// panelStatus.add(htmlStatus);
	// panelStatus.add(imgStatus);
	// panelStatus.add(imgRecheck);
	//
	// panelButtons.getElement().getStyle().setProperty("textAlign", "right");
	// panelButtons.getElement().getStyle().setMarginTop(6, Unit.PX);
	// if (hasButtons) {
	// panelButtons.add(btnOk);
	// panelButtons.add(btnCancel);
	//
	// }
	//
	// table.getElement().getStyle().setProperty("borderSpacing", "0");
	//
	// table.setWidget(0, 0, new SmallTableLabel("Protocoll"));
	// table.setWidget(0, 1, new SmallTableLabel("Host"));
	// table.setWidget(0, 2, new SmallTableLabel("Port"));
	// table.setWidget(2, 0, new SmallTableLabel("Controller"));
	//
	// table.setWidget(1, 0, cbProtocol);
	// table.setWidget(1, 1, tbHostname);
	// table.setWidget(1, 2, tbPort);
	// table.setWidget(3, 0, cbController);
	// table.setWidget(4, 0, panelStatus);
	// table.setWidget(4, 1, panelButtons);
	//
	// table.getFlexCellFormatter().setColSpan(3, 0, 3);
	// table.getFlexCellFormatter().setColSpan(4, 1, 2);
	//
	// table.getFlexCellFormatter().setWidth(1, 2, "1px");
	//
	// add(headline);
	// if (hasInfoText) {
	// add(infoText);
	// }
	// add(table);
	//
	// setBoxStatus(BoxStatus.UNKNOWN);
	// }

	// private void refreshUI() {
	// String protocol =
	// Manager.get().getCurrentScenarioDetails().getControllerProtocol();
	// if (protocol.equals("rmi://")) {
	// cbProtocol.setSelectedIndex(0);
	// } else if (protocol.equals("http://")) {
	// cbProtocol.setSelectedIndex(1);
	// } else if (protocol.equals("https://")) {
	// cbProtocol.setSelectedIndex(2);
	// }
	//
	// tbHostname.setText(Manager.get().getCurrentScenarioDetails().getControllerHost());
	// tbPort.setText("" +
	// Manager.get().getCurrentScenarioDetails().getControllerPort());
	// cbController.setText(Manager.get().getCurrentScenarioDetails().getControllerName());
	//
	// }

	// @Override
	// public void onValueChange(ValueChangeEvent<String> event) {
	// if (event.getSource() == cbProtocol) {
	// tbPort.setText("" + DEFAULT_PORTS[cbProtocol.getSelectedIndex()]);
	// } else if (event.getSource() == tbHostname) {
	// isPortReachable(true);
	// } else if (event.getSource() == tbPort) {
	// isPortReachable(true);
	// }
	// }

	// @Override
	// public void onClick(ClickEvent event) {
	// if (event.getSource() == btnCancel) {
	// // hide();
	// } else if (event.getSource() == btnOk) {
	// if (!Manager.get().getControllerUrl().equals(getUrl())) {
	// Manager.get().getCurrentScenarioDetails().setControllerHost(tbHostname.getText());
	// Manager.get().getCurrentScenarioDetails().setControllerProtocol(cbProtocol.getText());
	// Manager.get().getCurrentScenarioDetails().setControllerPort(Integer.parseInt(tbPort.getText()));
	// Manager.get().getCurrentScenarioDetails().setControllerName(cbController.getText());
	// Manager.get().storeAccountDetails();
	//
	// EventControl.get().fireEvent(new
	// MEControllerEvent(EventType.CONTROLLER_CHANGED));
	//
	// ScenarioManager.get().loadDefinitionFromCurrentController();
	// }
	// Manager.get().setControllerLastCheck(latestCheckRun);
	// Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);
	//
	// // hide();
	// } else if (event.getSource() == imgRecheck && !isChecking) {
	// isPortReachable(true);
	// }
	// }

	// private void isPortReachable(final boolean retrieveControllerIfAvailable)
	// {
	// setBoxStatus(BoxStatus.CHECKING);
	//
	// final long startTime = System.currentTimeMillis();
	// latestCheckRun = startTime;
	// final String host = tbHostname.getText();
	// final int port = Integer.parseInt(tbPort.getText());
	// RPC.getMEControllerRPC().isPortReachable(host, port, new
	// AsyncCallback<Boolean>() {
	// @Override
	// public void onSuccess(Boolean result) {
	// if (startTime != latestCheckRun) {
	// GWT.log("there is a more recent check running.");
	// return;
	// }
	//
	// if (result) {
	// setBoxStatus(BoxStatus.ONLINE);
	// if (retrieveControllerIfAvailable) {
	// retrieveController();
	// }
	// } else {
	// setBoxStatus(BoxStatus.OFFLINE);
	// }
	//
	// if
	// (host.equals(Manager.get().getCurrentScenarioDetails().getControllerHost())
	// && port == Manager.get().getCurrentScenarioDetails().getControllerPort())
	// {
	// if (result) {
	// Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);
	// } else {
	// Manager.get().setControllerLastStatus(ControllerStatus.OFFLINE);
	// }
	// Manager.get().setControllerLastCheck(startTime);
	//
	// EventControl.get().fireEvent(new
	// MEControllerEvent(EventType.STATUS_UPDATED));
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// setBoxStatus(BoxStatus.UNKNOWN);
	// }
	// });
	// }

	// private void retrieveController() {
	// final String tempSelectedController = cbController.getText();
	//
	// cbController.clear();
	//
	// if (cbProtocol.getText().equals("rmi://")) {
	// RPC.getMEControllerRPC().getRMIController(tbHostname.getText(),
	// Integer.parseInt(tbPort.getText()),
	// new AsyncCallback<List<String>>() {
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// }
	//
	// @Override
	// public void onSuccess(List<String> result) {
	// int count = 0;
	// for (String name : result) {
	// cbController.addItem(name);
	//
	// if (name.equals(tempSelectedController)) {
	// cbController.setSelectedIndex(count);
	// }
	//
	// count++;
	// }
	// }
	// });
	// }
	// }

}
