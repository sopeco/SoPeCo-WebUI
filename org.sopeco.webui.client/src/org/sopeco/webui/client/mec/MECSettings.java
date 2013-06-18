package org.sopeco.webui.client.mec;

import java.util.List;

import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.WrappedTextBox;
import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.event.EventControl;
import org.sopeco.webui.client.event.MEControllerEvent;
import org.sopeco.webui.client.event.MEControllerEvent.EventType;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.SmallTableLabel;
import org.sopeco.webui.shared.helper.MEControllerProtocol;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class MECSettings extends Composite implements ValueChangeHandler<String>, AsyncCallback<List<String>>,
		HasValueChangeHandlers<Boolean> {

	interface MECSettingsUiBinder extends UiBinder<Widget, MECSettings> {
	}

	private enum Status {
		CONTROLLER_AVAILABLE, EMPTY_HOST_PORT, EMPTY_TOKEN, INVALID_PORT, NO_CONTROLLER_RUNNING, NO_MEC_APPLCIATION, RECEIVING_CONTROLLER, UNCHECKED
	}

	private static MECSettingsUiBinder uiBinder = GWT.create(MECSettingsUiBinder.class);

	@UiField
	PushButton btnRefresh;
	@UiField
	ComboBox cbController;
	@UiField
	FlexTable connectionTable;
	@UiField
	FlowPanel flowController;
	@UiField
	FlowPanel flowControllerStatus;
	@UiField
	Label labelControllerStatus;
	@UiField
	Label headline;

	private ComboBox cbProtocol;
	private WrappedTextBox tbFirst;
	private WrappedTextBox tbTwo;

	private String tempTbFirstValue = "";
	private String tempTbTwoValue = "";

	private HandlerManager manager;

	private boolean layoutOne = false;

	/**
	 * Constructor. Calls the {@link #init()} method.
	 */
	public MECSettings() {
		initWidget(uiBinder.createAndBindUi(this));

		init();
	}

	@UiHandler("btnRefresh")
	public void clickStatusRefresh(ClickEvent e) {
		switch (getSelectedProtocol()) {
		case SOCKET:
			if (tbFirst.getTextbox().getValue().isEmpty()) {
				setStatus(Status.EMPTY_TOKEN);
				return;
			}
			break;
		case RMI:
		case REST_HTTP:
			if (tbFirst.getTextbox().getValue().isEmpty() || tbTwo.getTextbox().getValue().isEmpty()) {
				setStatus(Status.EMPTY_HOST_PORT);
				return;
			}
			if (!tbTwo.getTextbox().getValue().matches("^\\d+$")
					|| Integer.parseInt(tbTwo.getTextbox().getValue()) > 65535) {
				setStatus(Status.INVALID_PORT);
				return;
			}
			break;
		default:
			throw new IllegalArgumentException(cbProtocol.getSelectedIndex() + " is not a valid value.");
		}

		setStatus(Status.RECEIVING_CONTROLLER);
		receiveController();
	}

	/**
	 * Loads the controller settings of the current selected scenario.
	 */
	public void loadCurrentControllerSettings() {
		cbProtocol.setSelectedText(Manager.get().getCurrentScenarioDetails().getControllerProtocol(), true);
		tbFirst.getTextbox().setText(Manager.get().getCurrentScenarioDetails().getControllerHost());
		tbTwo.getTextbox().setText("" + Manager.get().getCurrentScenarioDetails().getControllerPort());
		if (!Manager.get().getCurrentScenarioDetails().getControllerName().isEmpty()) {
			setControllerBoxVisible(true);
		}
		cbController.setText(Manager.get().getCurrentScenarioDetails().getControllerName());
	}

	/**
	 * Returns the selected protocol.
	 * 
	 * @return the protocol which is selected
	 */
	private MEControllerProtocol getSelectedProtocol() {
		switch (cbProtocol.getSelectedIndex()) {
		case 0:
			return MEControllerProtocol.SOCKET;
		case 1:
			return MEControllerProtocol.RMI;
		case 2:
			return MEControllerProtocol.REST_HTTP;
		default:
			throw new IllegalStateException("Illegal combobox selection.");
		}
	}

	/**
	 * Initialization of the required members.
	 */
	private void init() {
		manager = new HandlerManager(this);

		cbProtocol = new ComboBox();
		cbProtocol.setWidth("100px");
		cbProtocol.addItem("socket://");
		cbProtocol.addItem("rmi://");
		cbProtocol.addItem("http://");
		cbProtocol.addValueChangeHandler(this);
		cbProtocol.setEditable(false);

		tbFirst = new WrappedTextBox();
		tbTwo = new WrappedTextBox();
		tbFirst.getTextbox().addValueChangeHandler(this);
		tbTwo.getTextbox().addValueChangeHandler(this);

		connectionTable.setWidget(0, 0, new SmallTableLabel(R.lang.protocol()));
		connectionTable.setWidget(1, 0, cbProtocol);

		setLayoutOne();

		connectionTable.getFlexCellFormatter().setWidth(0, 0, "115px");

	}

	public void removeHeadline() {
		headline.removeFromParent();
	}

	@Override
	public void onFailure(Throwable caught) {
		setStatus(Status.NO_MEC_APPLCIATION);
		SoPeCoUI.get().onUncaughtException(caught);
	}

	@Override
	public void onSuccess(List<String> result) {
		if (result == null) {
			setStatus(Status.NO_MEC_APPLCIATION);
		} else if (result.isEmpty()) {
			setStatus(Status.NO_CONTROLLER_RUNNING);
		} else {
			setStatus(Status.CONTROLLER_AVAILABLE);

			cbController.clear();
			for (String c : result) {
				cbController.addItem(c);
			}
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		setStatus(Status.UNCHECKED);

		if (event.getSource() == cbProtocol) {
			switch (cbProtocol.getSelectedIndex()) {
			case 0:
				setLayoutOne();
				break;
			case 1:
			case 2:
				setLayoutTwo();
			default:
				break;
			}
		}
	}

	/**
	 * Sets whether the elements are enabled.
	 * 
	 * @param enabled
	 *            true to enable the elements, false to disable them
	 */
	public void setEnabled(boolean enabled) {
		cbProtocol.setEnabled(enabled);
		tbFirst.getTextbox().setEnabled(enabled);
		tbTwo.getTextbox().setEnabled(enabled);
		cbController.setEnabled(enabled);
		btnRefresh.setEnabled(enabled);
	}

	/**
	 * Calls the RPC to receive the controller of the specified MEC application.
	 */
	private void receiveController() {
		MEControllerProtocol protocol = getSelectedProtocol();

		String host = tbFirst.getTextbox().getText();
		int port = 0;
		if (protocol != MEControllerProtocol.SOCKET) {
			port = Integer.parseInt(tbTwo.getTextbox().getText());
		}

		RPC.getGetRPC().getControllerFromMEC(protocol, host, port, this);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return manager.addHandler(ValueChangeEvent.getType(), handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		manager.fireEvent(event);
	}

	/**
	 * Sets whether the controller combobox or the status text is visible.
	 * 
	 * @param visible
	 */
	private void setControllerBoxVisible(boolean visible) {
		flowController.setVisible(visible);
		flowControllerStatus.setVisible(!visible);
	}

	/**
	 * Calls the {@link Label#setText(String)} method of the
	 * labelControllerStatus label.
	 * 
	 * @param statusText
	 *            the new text
	 */
	public void setControllerStatus(String statusText) {
		labelControllerStatus.setText(statusText);
	}

	/**
	 * Sets the layout "one text box". The text box is used for the token of the
	 * desired mec-application.
	 */
	private void setLayoutOne() {
		if (layoutOne) {
			return;
		}
		layoutOne = true;

		String tempValue = tempTbFirstValue;
		tempTbFirstValue = tbFirst.getTextbox().getValue();
		tempTbTwoValue = tbTwo.getTextbox().getValue();

		tbFirst.getTextbox().setValue(tempValue);

		tbFirst.setWidth("");
		connectionTable.setWidget(0, 1, new SmallTableLabel(R.lang.mecAppToken()));
		connectionTable.setWidget(1, 1, tbFirst);

		if (connectionTable.isCellPresent(0, 2)) {
			connectionTable.removeCell(0, 2);
			connectionTable.removeCell(1, 2);
		}
	}

	/**
	 * Sets the layout "two text boxes". The first text box is used for the host
	 * and the second for the port. This is used by RMI and HTTP.
	 */
	private void setLayoutTwo() {
		if (!layoutOne) {
			return;
		}
		layoutOne = false;

		String tempValueFirst = tempTbFirstValue;
		String tempValueTwo = tempTbTwoValue;
		tempTbFirstValue = tbFirst.getTextbox().getValue();

		tbFirst.getTextbox().setValue(tempValueFirst);
		tbTwo.getTextbox().setValue(tempValueTwo);

		tbFirst.setWidth("90%");
		connectionTable.setWidget(0, 1, new SmallTableLabel(R.lang.host() + " / " + R.lang.ipAddress()));
		connectionTable.setWidget(1, 1, tbFirst);
		connectionTable.setWidget(0, 2, new SmallTableLabel(R.lang.port()));
		connectionTable.setWidget(1, 2, tbTwo);

		connectionTable.getFlexCellFormatter().setWidth(0, 2, "50px");
	}

	/**
	 * Sets whether the reload button is enabled. In addition, the appropriate
	 * icon is set.
	 * 
	 * @param enabled
	 *            is the button enabled or disabled.
	 */
	private void setReloadButtonEnabled(boolean enabled) {
		btnRefresh.setEnabled(enabled);
		btnRefresh.setFocus(false);

		if (enabled) {
			btnRefresh.getUpFace().setImage(new Image(R.img.icoRefresh()));
		} else {
			btnRefresh.getUpFace().setImage(new Image(R.img.loadingIndicatorCircle()));
		}
	}

	/**
	 * Sets the properties of all widgets on the properties that correspond to
	 * the state.
	 * 
	 * @param status
	 *            status of the view
	 */
	private void setStatus(Status status) {
		ValueChangeEvent.fire(this, false);

		switch (status) {
		case CONTROLLER_AVAILABLE:
		case NO_CONTROLLER_RUNNING:
		case NO_MEC_APPLCIATION:
		case EMPTY_TOKEN:
		case EMPTY_HOST_PORT:
		case INVALID_PORT:
		case UNCHECKED:
			setReloadButtonEnabled(true);
			tbFirst.getTextbox().setEnabled(true);
			tbTwo.getTextbox().setEnabled(true);
			cbProtocol.setEnabled(true);
			cbController.setEnabled(true);
			break;
		case RECEIVING_CONTROLLER:
			setReloadButtonEnabled(false);
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.requestController());
			tbFirst.getTextbox().setEnabled(false);
			tbTwo.getTextbox().setEnabled(false);
			cbProtocol.setEnabled(false);
			cbController.setEnabled(false);
			return;
		default:
			throw new IllegalArgumentException();
		}

		switch (status) {
		case CONTROLLER_AVAILABLE:
			setControllerBoxVisible(true);
			ValueChangeEvent.fire(this, true);
			break;
		case NO_CONTROLLER_RUNNING:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.appHasNoRunningController());
			break;
		case NO_MEC_APPLCIATION:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.noMECAppAvailable());
			break;
		case EMPTY_TOKEN:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.emptyTokenNotSupported());
			break;
		case EMPTY_HOST_PORT:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.hostMustNotBeEmpty());
			break;
		case INVALID_PORT:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.enterValidPort());
			break;
		case UNCHECKED:
			setControllerBoxVisible(false);
			setControllerStatus(R.lang.controllerStateUnchecked());
			break;
		default:
			break;
		}
	}

	/**
	 * Saves the controller settings to the current scenario.
	 */
	public void saveControllerSettings() {
		MEControllerProtocol protocol = getSelectedProtocol();
		String controller = cbController.getText();
		String host = tbFirst.getTextbox().getText();
		int port = 0;

		if (protocol != MEControllerProtocol.SOCKET) {
			port = Integer.parseInt(tbTwo.getTextbox().getText());
		}

		// Set new settings
		Manager.get().getCurrentScenarioDetails().setControllerHost(host);
		Manager.get().getCurrentScenarioDetails().setControllerPort(port);
		Manager.get().getCurrentScenarioDetails().setControllerProtocol(getProtocolSchema(protocol));
		Manager.get().getCurrentScenarioDetails().setControllerName(controller);

		// Store new settings in the database
		Manager.get().storeAccountDetails();

		// Fire event controllerChanged and load new controller environment
		EventControl.get().fireEvent(new MEControllerEvent(EventType.CONTROLLER_CHANGED));
		ScenarioManager.get().loadDefinitionFromCurrentController();
	}

	private String getProtocolSchema(MEControllerProtocol protocol) {
		switch (protocol) {
		case REST_HTTP:
			return "http://";
		case RMI:
			return "rmi://";
		case SOCKET:
			return "socket://";
		default:
			throw new IllegalArgumentException();
		}
	}
}
