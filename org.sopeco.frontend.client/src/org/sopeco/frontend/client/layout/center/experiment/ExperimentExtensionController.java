/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.frontend.client.layout.center.experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentExtensionController implements ValueChangeHandler<String> {

	private static final Logger LOGGER = Logger.getLogger(ExperimentExtensionController.class.getName());
	private static final String VALUE_CHANGED_CSS_CLASS = "valueChanged";

	private ExperimentController parentController;
	private ExperimentExtensionView view;
	// private ExtensionTypes extensionType;
	private Map<String, Map<String, String>> extensionMap;
	private Map<String, String> currentConfig;
	private Map<EditableText, String> editTextToKey;
	private String currentExtensionName;

	public ExperimentExtensionController(ExperimentController parent, int width) {
		view = new ExperimentExtensionView(width);
		currentConfig = new HashMap<String, String>();
		parentController = parent;
		currentExtensionName = "";

		view.getCombobox().addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				double metering = Metering.start();
				currentExtensionName = event.getValue();

				changeConfig();

				updateConfigTable();

				ScenarioManager.get().experiment().saveExperimentConfig(parentController);

				Metering.stop(metering);
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
		extensionMap = Extensions.get().getExtensions(newExtensionType);
		updateView();
	}

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

		Set<String> keySet = new TreeSet<String>(extensionMap.keySet());

		for (String name : keySet) {
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
		editTextToKey = new HashMap<EditableText, String>();

		RegExp regex = RegExp.compile("([A-Z])", "g");

		for (String key : currentConfig.keySet()) {
			// Puts an whitespace infront of every capital
			String text = regex.replace(key, " $1");
			String defaultValue = extensionMap.get(currentExtensionName).get(key);
			
			EditableText newTextbox = view.addConfigRow(text, key, currentConfig.get(key));
			newTextbox.setDefaultValue(defaultValue);

			// setTextboxHighligh(newTextbox, !valueIsDefault(key,
			// currentConfig.get(key)));
			newTextbox.setTitle(R.get("default") + ": " + defaultValue);

			editTextToKey.put(newTextbox, key);

			newTextbox.addValueChangeHandler(this);
		}

		view.getConfigTable().getColumnFormatter().setWidth(0, "1px");

		Metering.stop(metering);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		double metering = Metering.start();

		// String key = ((TextBox) event.getSource()).getName();
		String key = editTextToKey.get(event.getSource());

		// setTextboxHighligh((TextBox) event.getSource(), !valueIsDefault(key,
		// event.getValue()));

		currentConfig.put(key, event.getValue());

		ScenarioManager.get().experiment().saveExperimentConfig(parentController);

		Metering.stop(metering);
	}

	/**
	 * Checks whether the value is the default value for the given key.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	private boolean valueIsDefault(String key, String value) {
		// LOGGER.info("check: " + key + " = " + value + " [" +
		// currentExtensionName + "]");
		return extensionMap.get(currentExtensionName).get(key).equals(value);
	}

	/**
	 * 
	 * @param textbox
	 * @param isHighlighted
	 */
	private void setTextboxHighligh(TextBox textbox, boolean isHighlighted) {
		if (isHighlighted) {
			textbox.addStyleName(VALUE_CHANGED_CSS_CLASS);
		} else {
			textbox.removeStyleName(VALUE_CHANGED_CSS_CLASS);
		}
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

	/**
	 * Sets the value of the combobox to the given name.
	 * 
	 * @param name
	 */
	public void setExtension(String name) {
		Set<String> keySet = new TreeSet<String>(extensionMap.keySet());

		int i = 0;
		for (String key : keySet) {
			if (key.equals(name)) {
				LOGGER.fine("Set Extension to: " + name);

				currentExtensionName = name;
				view.getCombobox().setSelectedIndex(i);

				return;
			}
			i++;
		}
	}

	/**
	 * Sets the current configMap to the given Map.
	 * 
	 * @param newConfigMap
	 */
	public void setConfigMap(Map<String, String> newConfigMap) {
		currentConfig = newConfigMap;

		updateConfigTable();
	}

	/**
	 * @return the currentExtensionName
	 */
	public String getCurrentExtensionName() {
		return currentExtensionName;
	}
}
