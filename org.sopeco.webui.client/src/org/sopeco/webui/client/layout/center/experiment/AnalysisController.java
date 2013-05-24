package org.sopeco.webui.client.layout.center.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.extensions.Extensions;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.helper.ExtensionTypes;
import org.sopeco.webui.shared.helper.Metering;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.ui.TextBox;

public class AnalysisController implements ValueChangeHandler<String> {
	private static final Logger LOGGER = Logger.getLogger(ExtensionController.class.getName());
	private static final String VALUE_CHANGED_CSS_CLASS = "valueChanged";

	private ExperimentController parentController;
	private AnalysisView view;
	// private ExtensionTypes extensionType;
	private Map<String, Map<String, String>> explorationExtensionMap;
	private Map<String, String> currentConfig;
	private Map<EditableText, String> editTextToKey;
	private String currentExtensionName;

	private ParameterDefinition dependentParameter;
	private final List<ParameterDefinition> independentParameters = new ArrayList<ParameterDefinition>();

	public AnalysisController(ExperimentController parent, int width) {
		view = new AnalysisView(width);
		currentConfig = new HashMap<String, String>();
		setParentController(parent);
		currentExtensionName = "";
		view.getParameterTree().setAnalysisController(this);
		view.getCombobox().addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				double metering = Metering.start();
				currentExtensionName = event.getValue();

				changeConfig();

				updateConfigTable();
				ScenarioManager.get().experiment().saveExperimentConfig(getParentController());

				Metering.stop(metering);
			}
		});

		view.getCbDependentParameter().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				for (ParameterDefinition pDef : ScenarioManager.get().getBuilder().getMEDefinition().getRoot()
						.getAllParameters()) {
					if (view.getCbDependentParameter().getText().equalsIgnoreCase(pDef.getFullName())) {
						dependentParameter = pDef;
						break;
					}
				}

				ScenarioManager.get().experiment().saveExperimentConfig(getParentController());

			}
		});
	}

	/**
	 * Sets the current config to the current selected default configuration.
	 */
	private void changeConfig() {
		currentConfig.clear();
		currentConfig.putAll(explorationExtensionMap.get(getSelectedExtensionName()));
	}

	/**
	 * @return the view
	 */
	public ExtensionView getView() {
		return view;
	}

	/**
	 * Setting the headline text.
	 */
	public void setHeadline(String text) {
		view.getHeadline().setInnerHTML(text);
	}

	public void setExtensionType(ExtensionTypes extensionType) {
		explorationExtensionMap = Extensions.get().getExtensions(extensionType);
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

		Set<String> keySet = new TreeSet<String>(explorationExtensionMap.keySet());

		for (String name : keySet) {
			view.getCombobox().addItem(name);
		}

		updateConfigTable();
		Metering.stop(metering);
	}

	protected void updateParameterSelectionWidgets() {
		view.getCbDependentParameter().clear();
		for (ParameterDefinition parDef : ScenarioManager.get().getBuilder().getMEDefinition().getRoot()
				.getObservationParameters()) {
			view.getCbDependentParameter().addItem(parDef.getFullName());

		}

		for (ParameterDefinition pDef : ScenarioManager.get().getBuilder().getMEDefinition().getRoot()
				.getAllParameters()) {
			if (view.getCbDependentParameter().getText().equalsIgnoreCase(pDef.getFullName())) {
				dependentParameter = pDef;
				break;
			}
		}

		view.getParameterTree().generateTree();
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
			String defaultValue = explorationExtensionMap.get(currentExtensionName).get(key);

			EditableText newTextbox = view.addConfigRow(text, key, currentConfig.get(key));
			newTextbox.setDefaultValue(defaultValue);

			// setTextboxHighligh(newTextbox, !valueIsDefault(key,
			// currentConfig.get(key)));
			newTextbox.setTitle(R.lang.defaultString() + ": " + defaultValue);

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

		ScenarioManager.get().experiment().saveExperimentConfig(getParentController());

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
		return explorationExtensionMap.get(currentExtensionName).get(key).equals(value);
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
		Set<String> keySet = new TreeSet<String>(explorationExtensionMap.keySet());

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

	/**
	 * @return the dependentParameter
	 */
	public ParameterDefinition getDependentParameter() {
		return dependentParameter;
	}

	/**
	 * @param dependentParameter
	 *            the dependentParameter to set
	 */
	public void setDependentParameter(ParameterDefinition dependentParameter) {
		this.dependentParameter = dependentParameter;

		view.getCbDependentParameter().setSelectedText(dependentParameter.getFullName());

	}

	/**
	 * @return the independentParameters
	 */
	public List<ParameterDefinition> getIndependentParameters() {
		return independentParameters;
	}

	public void setIndependentParameters(List<ParameterDefinition> independentParameters) {
		this.independentParameters.clear();
		this.independentParameters.addAll(independentParameters);
	}

	/**
	 * @return the parentController
	 */
	public ExperimentController getParentController() {
		return parentController;
	}

	/**
	 * @param parentController
	 *            the parentController to set
	 */
	public void setParentController(ExperimentController parentController) {
		this.parentController = parentController;
	}

}
