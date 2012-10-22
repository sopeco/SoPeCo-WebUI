package org.sopeco.frontend.client.layout.center.experiment;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.ExtensionContainer;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentExtensionController implements ValueChangeHandler<String> {

	private static final String VALUE_CHANGED_CSS_CLASS = "valueChanged";

	private ExperimentExtensionView view;
	private ExtensionTypes extensionType;
	private Map<String, Map<String, String>> extensionMap;
	private Map<String, String> currentConfig;

	public ExperimentExtensionController(int width) {
		view = new ExperimentExtensionView(width);
		currentConfig = new HashMap<String, String>();

		view.getCombobox().addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				changeConfig();

				updateConfigTable();
			}
		});
	}

	/**
	 * Sets the current config to the current selected default configuration.
	 */
	private void changeConfig() {
		currentConfig.clear();
		currentConfig.putAll(extensionMap.get(getSelectedExtensionName()));
	}

	/**
	 * @return the view
	 */
	public ExperimentExtensionView getView() {
		return view;
	}

	/**
	 * Setting the headline text.
	 */
	public void setHeadline(String text) {
		view.getHeadline().setInnerHTML(text);
	}

	/**
	 * @param extensionMap
	 *            the extension to set
	 */
	public void setExtensionType(ExtensionTypes newExtensionType) {
		extensionType = newExtensionType;

		extensionMap = Extensions.get().getExtensions(newExtensionType);
		updateView();
		// retreiveExtension();
	}

	// /**
	// *
	// * @return
	// */
	// private void retreiveExtension() {
	// RPC.getExtensionRPC().getExtension(extensionType, new
	// AsyncCallback<ExtensionContainer>() {
	// @Override
	// public void onSuccess(ExtensionContainer result) {
	// extension = result;
	//
	// updateView();
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// }
	// });
	// }

	/**
	 * Returns the name of the selected extension. (Selected ComboBox Item).
	 * 
	 * @return extension name
	 */
	public String getSelectedExtensionName() {
		return view.getCombobox().getText();
	}

	/**
	 * Updates the Combobox and the Config-Table
	 */
	private void updateView() {
		double metering = Metering.start();

		view.getCombobox().clear();

		for (String name : extensionMap.keySet()) {
			view.getCombobox().addItem(name);
		}

		updateConfigTable();
		Metering.stop(metering);
	}

	/**
	 * Updates the configuration table.
	 */
	private void updateConfigTable() {
		double metering = Metering.start();

		view.getConfigTable().removeAllRows();

		RegExp regex = RegExp.compile("([A-Z])", "g");

		for (String key : currentConfig.keySet()) {
			String text = regex.replace(key, " $1");

			TextBox newTextbox = view.addConfigRow(text, key,
					extensionMap.get(view.getCombobox().getText()).get(key));

			newTextbox.addValueChangeHandler(this);
		}

		Metering.stop(metering);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		String key = ((TextBox) event.getSource()).getName();

		/** Mark textbox, that their value is not default. */
		if (!extensionMap.get(view.getCombobox().getText()).get(key).equals(event.getValue())) {
			((TextBox) event.getSource()).addStyleName(VALUE_CHANGED_CSS_CLASS);
		} else {
			((TextBox) event.getSource()).removeStyleName(VALUE_CHANGED_CSS_CLASS);
		}

		currentConfig.put(key, event.getValue());
	}

	/**
	 * Returns a map of the current configuration.
	 * 
	 * @return
	 */
	public Map<String, String> getConfigMap() {
		Map<String, String> copiedMap = new HashMap<String, String>();
		copiedMap.putAll(currentConfig);
		return copiedMap;
	}
}
