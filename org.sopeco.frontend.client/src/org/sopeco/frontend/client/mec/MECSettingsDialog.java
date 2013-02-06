package org.sopeco.frontend.client.mec;

import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.client.widget.SoPeCoDialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class MECSettingsDialog extends SoPeCoDialog implements ValueChangeHandler<Boolean>, ClickHandler {

	private static MECSettingsDialog mecDialog;

	public static void showDialog() {
		if (mecDialog == null) {
			mecDialog = new MECSettingsDialog();
		}

		mecDialog.center();
	}

	private Button btnCancel, btnOk;

	private MEControllerSettings controllerSettings;

	private MECSettingsDialog() {
		super(false);

		controllerSettings = new MEControllerSettings();
		controllerSettings.addValueChangeHandler(this);

		setDraggable(true);
		setHeadline(R.lang.measurementEnvironmentController());
		setContentWidget(controllerSettings.getView());

		btnCancel = addButton(R.lang.cancel(), this);
		btnOk = addButton(R.lang.ok(), this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnOk) {
			saveControllerSettings();
		} else if (event.getSource() == btnCancel) {
			hide();
		}
	}

	private void saveControllerSettings() {
		controllerSettings.saveControllerSettings();
		hide();
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		btnOk.setEnabled(event.getValue());
	}

	@Override
	public void center() {
		super.center();
		controllerSettings.resetViewToCurrent();
		controllerSettings.loadController();
	}
}
