package org.sopeco.webui.client.layout.dialog;

import org.sopeco.gwt.widgets.ExtendedTextBox;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.webui.client.helper.SimpleCallback;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.mec.MECSettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AddScenario extends Composite implements ValueChangeHandler<Boolean>, SimpleCallback<Object>, HasText {

	private static AddScenarioUiBinder uiBinder = GWT.create(AddScenarioUiBinder.class);

	interface AddScenarioUiBinder extends UiBinder<Widget, AddScenario> {
	}

	@UiField
	MECSettings mecSettings;
	@UiField
	ExtendedTextBox tbAccount;
	@UiField
	Label infoText;

	private SimpleCallback<Boolean> callbackState, callbackCreated;

	public AddScenario() {
		initWidget(uiBinder.createAndBindUi(this));

		mecSettings.addValueChangeHandler(this);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (callbackState != null)
			callbackState.callback(event.getValue());
	}

	@Override
	public String getText() {
		return infoText.getText();
	}

	@Override
	public void setText(String text) {
		infoText.setText(text);
	}

	public void setStateHandler(SimpleCallback<Boolean> pCallback) {
		callbackState = pCallback;
	}

	public void setCreatedHandler(SimpleCallback<Boolean> pCallback) {
		callbackCreated = pCallback;
	}

	public void setEnabled(boolean enabled) {
		tbAccount.setEnabled(enabled);
		mecSettings.setEnabled(enabled);
	}

	public void createScenario() {
		ExperimentSeriesDefinition experiment = ScenarioManager.get().getExperimentModul()
				.getNewExperimentSeries("MyExperiment");

		ScenarioManager.get().createScenario(tbAccount.getText(), "MySpecification", experiment, this);
	}

	@Override
	public void callback(Object object) {
		mecSettings.saveControllerSettings();

		MainLayoutPanel.get().getNorthPanel().updateScenarioList();
		ScenarioManager.get().switchScenario(Manager.get().getAccountDetails().getSelectedScenario());

		if (callbackCreated != null)
			callbackCreated.callback(true);
	}
}
