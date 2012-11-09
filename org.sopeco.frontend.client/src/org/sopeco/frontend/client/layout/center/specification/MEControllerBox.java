package org.sopeco.frontend.client.layout.center.specification;

import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.MEControllerEvent;
import org.sopeco.frontend.client.event.MEControllerEvent.EventType;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.Manager.ControllerStatus;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.Headline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
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
public final class MEControllerBox extends DialogBox implements ValueChangeHandler<String>, ClickHandler {

	private static final String IMG_RECHECK = "images/reload.png";
	private static final int[] DEFAULT_PORTS = new int[] { 1099, 80, 443 };
	private static MEControllerBox box;

	private FlowPanel panelStatus;
	private ComboBox cbProtocol, cbController;
	private TextBox tbHostname, tbPort;
	private Button btnOk, btnCancel;
	private Image imgStatus, imgRecheck;

	private BoxStatus boxStatus;

	// private String currentProtocol = "rmi://", currentHostname = "localhost",
	// currentPort = "1099",
	// currentController = "";

	private long latestCheckRun;
	private boolean isChecking = false;

	public enum BoxStatus {
		ONLINE, UNKNOWN, OFFLINE, CHECKING
	}

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

		panelStatus = new FlowPanel();
		cbProtocol = new ComboBox();
		cbController = new ComboBox();
		tbHostname = new TextBox();
		tbPort = new TextBox();
		btnOk = new Button(R.get("Ok"));
		btnCancel = new Button(R.get("Cancel"));
		imgRecheck = new Image(IMG_RECHECK);

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
		tbHostname.addValueChangeHandler(this);

		tbPort.setWidth("40px");
		tbPort.getElement().getStyle().setMargin(0, Unit.PX);
		tbPort.addValueChangeHandler(this);

		cbController.setWidth(345);
		cbController.setEditable(false);

		btnCancel.getElement().getStyle().setMarginLeft(6, Unit.PX);
		btnCancel.addClickHandler(this);
		btnCancel.setWidth("80px");

		btnOk.setWidth("80px");
		btnOk.addClickHandler(this);

		HTML htmlStatus = new HTML(R.get("Status") + ":");
		htmlStatus.getElement().getStyle().setColor("#555");
		htmlStatus.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);

		imgStatus = new Image();
		imgStatus.getElement().getStyle().setMarginLeft(0.5, Unit.EM);
		imgStatus.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		imgStatus.getElement().getStyle().setPaddingBottom(2, Unit.PX);

		imgRecheck.getElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
		imgRecheck.getElement().getStyle().setPaddingBottom(2, Unit.PX);
		imgRecheck.getElement().getStyle().setMarginLeft(1, Unit.EM);
		imgRecheck.getElement().getStyle().setCursor(Cursor.POINTER);
		imgRecheck.getElement().getStyle().setOpacity(0.8);
		imgRecheck.setHeight("14px");
		imgRecheck.setWidth("14px");
		imgRecheck.setTitle(R.get("checkAgain"));
		imgRecheck.addClickHandler(this);

		panelStatus.getElement().getStyle().setMarginTop(6, Unit.PX);
		panelStatus.add(htmlStatus);
		panelStatus.add(imgStatus);
		panelStatus.add(imgRecheck);

		panelButtons.getElement().getStyle().setProperty("textAlign", "right");
		panelButtons.getElement().getStyle().setMarginTop(6, Unit.PX);
		panelButtons.add(btnOk);
		panelButtons.add(btnCancel);

		table.getElement().getStyle().setProperty("borderSpacing", "0");

		table.setWidget(0, 0, new SLabel("Protocoll"));
		table.setWidget(0, 1, new SLabel("Host"));
		table.setWidget(0, 2, new SLabel("Port"));
		table.setWidget(2, 0, new SLabel("Controller"));

		table.setWidget(1, 0, cbProtocol);
		table.setWidget(1, 1, tbHostname);
		table.setWidget(1, 2, tbPort);
		table.setWidget(3, 0, cbController);
		table.setWidget(4, 0, panelStatus);
		table.setWidget(4, 1, panelButtons);

		table.getFlexCellFormatter().setColSpan(3, 0, 3);
		table.getFlexCellFormatter().setColSpan(4, 1, 2);

		wrapper.add(headline);
		wrapper.add(table);

		add(wrapper);

		setBoxStatus(BoxStatus.UNKNOWN);
	}

	private void refreshUI() {
		String protocol = Manager.get().getCurrentScenarioDetails().getControllerProtocol();
		if (protocol.equals("rmi://")) {
			cbProtocol.setSelectedIndex(0);
		} else if (protocol.equals("http://")) {
			cbProtocol.setSelectedIndex(1);
		} else if (protocol.equals("https://")) {
			cbProtocol.setSelectedIndex(2);
		}

		tbHostname.setText(Manager.get().getCurrentScenarioDetails().getControllerHost());
		tbPort.setText("" + Manager.get().getCurrentScenarioDetails().getControllerPort());
		cbController.setText(Manager.get().getCurrentScenarioDetails().getControllerName());

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == cbProtocol) {
			tbPort.setText("" + DEFAULT_PORTS[cbProtocol.getSelectedIndex()]);
		} else if (event.getSource() == tbHostname) {
			isPortReachable(true);
		} else if (event.getSource() == tbPort) {
			isPortReachable(true);
		}
	}

	private String getUrl() {
		return cbProtocol.getText() + tbHostname.getText() + ":" + tbPort.getText() + "/" + cbController.getText();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnCancel) {
			hide();
		} else if (event.getSource() == btnOk) {
			if (!Manager.get().getControllerUrl().equals(getUrl())) {
				Manager.get().getCurrentScenarioDetails().setControllerHost(tbHostname.getText());
				Manager.get().getCurrentScenarioDetails().setControllerProtocol(cbProtocol.getText());
				Manager.get().getCurrentScenarioDetails().setControllerPort(Integer.parseInt(tbPort.getText()));
				Manager.get().getCurrentScenarioDetails().setControllerName(cbController.getText());
				Manager.get().storeAccountDetails();

				EventControl.get().fireEvent(new MEControllerEvent(EventType.CONTROLLER_CHANGED));

				ScenarioManager.get().loadDefinitionFromCurrentController();
			}
			Manager.get().setControllerLastCheck(latestCheckRun);
			Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);

			hide();
		} else if (event.getSource() == imgRecheck && !isChecking) {
			isPortReachable(true);
		}
	}

	private void isPortReachable(final boolean retrieveControllerIfAvailable) {
		setBoxStatus(BoxStatus.CHECKING);

		final long startTime = System.currentTimeMillis();
		latestCheckRun = startTime;
		final String host = tbHostname.getText();
		final int port = Integer.parseInt(tbPort.getText());
		RPC.getMEControllerRPC().isPortReachable(host, port, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (startTime != latestCheckRun) {
					GWT.log("there is a more recent check running.");
					return;
				}

				if (result) {
					setBoxStatus(BoxStatus.ONLINE);
					if (retrieveControllerIfAvailable) {
						retrieveController();
					}
				} else {
					setBoxStatus(BoxStatus.OFFLINE);
				}

				if (host.equals(Manager.get().getCurrentScenarioDetails().getControllerHost())
						&& port == Manager.get().getCurrentScenarioDetails().getControllerPort()) {
					if (result) {
						Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);
					} else {
						Manager.get().setControllerLastStatus(ControllerStatus.OFFLINE);
					}
					Manager.get().setControllerLastCheck(startTime);

					EventControl.get().fireEvent(new MEControllerEvent(EventType.STATUS_UPDATED));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
				setBoxStatus(BoxStatus.UNKNOWN);
			}
		});
	}

	private void retrieveController() {
		final String tempSelectedController = cbController.getText();

		cbController.clear();

		if (cbProtocol.getText().equals("rmi://")) {
			RPC.getMEControllerRPC().getRMIController(tbHostname.getText(), Integer.parseInt(tbPort.getText()),
					new AsyncCallback<List<String>>() {
						@Override
						public void onFailure(Throwable caught) {
							Message.error(caught.getMessage());
						}

						@Override
						public void onSuccess(List<String> result) {
							int count = 0;
							for (String name : result) {
								cbController.addItem(name);

								if (name.equals(tempSelectedController)) {
									cbController.setSelectedIndex(count);
								}

								count++;
							}
						}
					});
		}
	}

	private void setBoxStatus(BoxStatus status) {
		boxStatus = status;

		boolean btnOkEnabled = false;
		String btnOkText = R.get("Ok");
		String imgStatusUrl = "images/";
		String imgTitle = "";
		double imgRecheckOpacity = 0.8;
		isChecking = false;

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
			isChecking = true;
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

		imgRecheck.getElement().getStyle().setOpacity(imgRecheckOpacity);
	}

	// public String getControllerUrl() {
	// return currentProtocol + currentHostname + ":" + currentPort + "/" +
	// currentController;
	// }
	//
	// /**
	// * @return the currentProtocol
	// */
	// public String getCurrentProtocol() {
	// return currentProtocol;
	// }
	//
	// /**
	// * @return the currentHostname
	// */
	// public String getCurrentHostname() {
	// return currentHostname;
	// }
	//
	// /**
	// * @return the currentPort
	// */
	// public int getCurrentPort() {
	// return Integer.parseInt(currentPort);
	// }
	//
	// /**
	// * @return the currentController
	// */
	// public String getCurrentController() {
	// return currentController;
	// }

	private static MEControllerBox getBox() {
		if (box == null) {
			box = new MEControllerBox();
		}
		return box;
	}

	public static void showBox() {
		if (getBox().isChecking) {
			getBox().latestCheckRun = System.currentTimeMillis();
			getBox().setBoxStatus(BoxStatus.UNKNOWN);
		}

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
