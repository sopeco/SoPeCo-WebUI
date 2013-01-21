package org.sopeco.frontend.client.layout;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.MEControllerEvent;
import org.sopeco.frontend.client.event.MEControllerEvent.EventType;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.manager.Manager.ControllerStatus;
import org.sopeco.frontend.client.mec.ControllerView;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MEControllerBox extends DialogBox implements ValueChangeHandler<String>, ClickHandler {

	private static MEControllerBox box;

	private MECController mecController;
	private ControllerView controllerView;

	private MEControllerBox() {
		super(false, false);
		init();
	}

	private void init() {
		setGlassEnabled(true);

		mecController = new MECController(false, true);
		controllerView = (ControllerView) mecController.getView();

		controllerView.getBtnCancel().addClickHandler(this);
		controllerView.getBtnOk().addClickHandler(this);

		add(mecController.getView());
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == controllerView.getBtnCancel()) {
			hide();
		} else if (event.getSource() == controllerView.getBtnOk()) {
			save();
		}
	}

	private void save() {
//		if (!Manager.get().getControllerUrl().equals(mecController.getUrl())) {
			Manager.get().getCurrentScenarioDetails().setControllerHost(controllerView.getTbHostname().getText());
			Manager.get().getCurrentScenarioDetails().setControllerProtocol(controllerView.getCbProtocol().getText());
			Manager.get().getCurrentScenarioDetails()
					.setControllerPort(Integer.parseInt(controllerView.getTbPort().getText()));
			Manager.get().getCurrentScenarioDetails().setControllerName(controllerView.getCbController().getText());
			Manager.get().storeAccountDetails();

			EventControl.get().fireEvent(new MEControllerEvent(EventType.CONTROLLER_CHANGED));

			ScenarioManager.get().loadDefinitionFromCurrentController();
//		}
		// Manager.get().setControllerLastCheck(latestCheckRun);
		Manager.get().setControllerLastStatus(ControllerStatus.ONLINE);

		hide();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	private static MEControllerBox getBox() {
		if (box == null) {
			box = new MEControllerBox();
		}
		return box;
	}

	public static void showBox() {
		getBox().mecController.refreshUI();
		getBox().center();
	}
}
