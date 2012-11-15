package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.mec.ControllerInteraction;
import org.sopeco.frontend.client.mec.ControllerInteraction.Protocol;
import org.sopeco.frontend.client.mec.ControllerView;
import org.sopeco.frontend.client.mec.ControllerView.ViewStatus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
@SuppressWarnings("rawtypes")
public class MECController extends FlowPanel implements ValueChangeHandler<String>, ClickHandler, INotifyHandler,
		HasValueChangeHandlers<Boolean> {

	private static final int[] DEFAULT_PORTS = new int[] { 1099, 80, 443 };

	private boolean mecIsOnline = false;
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
		if (event.getSource() == view.getImgReCheck()) {
			checkEnteredHost();
		}
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

	@SuppressWarnings("unchecked")
	private void checkEnteredHost() {
		String host = view.getTbHostname().getText();
		int port = Integer.parseInt(view.getTbPort().getText());
		ControllerInteraction.isPortReachable(host, port, this);
		view.setViewStatus(ViewStatus.CHECKING);
		mecIsOnline = false;
		ValueChangeEvent.fire(this, mecIsOnline);
	}

	@SuppressWarnings("unchecked")
	private void retrieveController() {
		String host = view.getTbHostname().getText();
		int port = Integer.parseInt(view.getTbPort().getText());

		ControllerInteraction.retrieveController(Protocol.RMI, host, port, this);
	}

	@Override
	public void call(Result result) {
		if (result.getKey() == null) {
			return;
		} else if (result.getKey().equals(ControllerInteraction.KEY_PORT_REACHABLE)) {
			boolean value = (Boolean) result.getValue();
			if (result.wasSuccessful() && value) {
				mecIsOnline = true;
				view.setViewStatus(ViewStatus.ONLINE);
				retrieveController();
			} else {
				mecIsOnline = false;
				view.setViewStatus(ViewStatus.OFFLINE);
				view.getCbController().clear();
			}

			ValueChangeEvent.fire(this, mecIsOnline);
		} else if (result.getKey().equals(ControllerInteraction.KEY_RETRIEVE_MEC)) {
			String[] value = (String[]) result.getValue();
			view.setAvailableController(value);
		}
	}

	/**
	 * @return the mecIsOnline
	 */
	public boolean isMecOnline() {
		return mecIsOnline;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	/**
	 * 
	 * @return
	 */
	public String getUrl() {
		return view.getCbProtocol().getText() + view.getTbHostname().getText() + ":" + view.getTbPort().getText() + "/"
				+ view.getCbController().getText();
	}
}
