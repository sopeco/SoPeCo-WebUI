package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.sopeco.frontend.client.event.ExperimentAssignmentRenderedEvent;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.widget.ComboBox;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentItem extends AssignmentItem implements ValueChangeHandler<String> {

	private static final String EA_CONFIG_MAP_CSS_CLASS = "editArea-configMap";
	private static final String CONSTANT_VARIATION = "Constant Variation";

	private ComboBox combobox;
	private Set<String> variationSet;
	private FlowPanel editArea;

	private static Map<String, String[]> allowedExtension;

	private Map<String, TextBox> configTextboxes;

	public ExperimentAssignmentItem(ParameterValueAssignment valueAssignment) {
		super(valueAssignment);
	}

	private static final GwtEvent<?> LOADEVENT = new ExperimentAssignmentRenderedEvent();

	@Override
	protected void initValueArea() {
		combobox = new ComboBox();
		editArea = new FlowPanel();
		configTextboxes = new HashMap<String, TextBox>();

		combobox.setEditable(false);
		combobox.addValueChangeHandler(this);
		initCombobox();

		add(combobox);

		updateEditArea();
	}

	@Override
	protected GwtEvent<?> getOnLoadEvent() {
		return LOADEVENT;
	}

	/**
	 * Adds the values to the combobox.
	 */
	private void initCombobox() {
		variationSet = new TreeSet<String>();

		// variationSet.add(R.get("constantValue"));

		Set<String> keySetVar = new TreeSet<String>(Extensions.get().getExtensions(ExtensionTypes.PARAMETERVARIATION)
				.keySet());

		for (String key : keySetVar) {
			variationSet.add(key);
		}

		if (!keySetVar.contains(CONSTANT_VARIATION)) {
			keySetVar.add(CONSTANT_VARIATION);
		}

		int counter = 0;
		for (String key : variationSet) {
			if (isExtentionAllowed(key)) {
				combobox.addItem(key);
				if (assignment instanceof DynamicValueAssignment
						&& key.equals(((DynamicValueAssignment) assignment).getName())) {
					combobox.setSelectedIndex(counter);
				}
				counter++;
			}
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		updateEditArea();
	}

	/**
	 * Check whether the variation is allowed for the current assignment type.
	 * 
	 * @param name
	 * @return
	 */
	private boolean isExtentionAllowed(String name) {
		if (allowedExtension == null) {
			generateAllowedExtensionMap();
		}

		if (!allowedExtension.containsKey(name)) {
			return true;
		}

		for (String type : allowedExtension.get(name)) {
			if (type.toUpperCase().equals(assignment.getParameter().getType().toUpperCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Map which assigns the values that are allowed in which types.
	 */
	private static void generateAllowedExtensionMap() {
		allowedExtension = new HashMap<String, String[]>();

		allowedExtension.put("Linear Numeric Variation", new String[] { "INTEGER", "DOUBLE" });
		allowedExtension.put("Boolean Variation", new String[] { "BOOLEAN" });
		allowedExtension.put("CSV", new String[] { "STRING", "INTEGER", "DOUBLE" });
	}

	/**
	 * 
	 */
	private void updateEditArea() {
		double metering = Metering.start();
		if (editArea.isAttached()) {
			editArea.removeFromParent();
		}
		editArea.clear();
		editArea.setStyleName("");

		// build
		editAreaConfigMap();

		add(editArea);
		Metering.stop(metering);
	}

	/**
	 * 
	 */
	private void editAreaConfigMap() {
		Map<String, String> configMap = Extensions.get().getExtensions(ExtensionTypes.PARAMETERVARIATION)
				.get(combobox.getText());

		if (configMap.isEmpty()) {
			return;
		}

		FlexTable configTable = new FlexTable();

		int i = 0;
		for (String key : configMap.keySet()) {
			TextBox valueBox = new TextBox();
			valueBox.setText(configMap.get(key));
			valueBox.addValueChangeHandler(new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					storeAssignment();
				}
			});

			configTextboxes.put(key, valueBox);
			configTable.setWidget(i, 0, new HTML(key));
			configTable.setWidget(i, 1, valueBox);
			i++;
		}

		editArea.addStyleName(EA_CONFIG_MAP_CSS_CLASS);
		editArea.getElement().getStyle().setDisplay(Display.BLOCK);

		editArea.add(configTable);
	}

	/**
	 * 
	 * @return
	 */
	private Map<String, String> getConfigurtion() {
		Map<String, String> returnMap = new HashMap<String, String>();

		for (String key : configTextboxes.keySet()) {
			returnMap.put(key, configTextboxes.get(key).getText());
		}

		return returnMap;
	}

	private ParameterValueAssignment getValueAssignment() {
		if (combobox.getText().equals(CONSTANT_VARIATION)) {
			return createConstantValueAssignment();
		} else {
			return createDynamicValueAssignment();
		}
	}

	/**
	 * 
	 * @return
	 */
	private DynamicValueAssignment createDynamicValueAssignment() {
		DynamicValueAssignment dva = new DynamicValueAssignment();

		dva.setName(combobox.getText());
		dva.getConfiguration().putAll(getConfigurtion());
		dva.setParameter(assignment.getParameter());

		return dva;
	}

	/**
	 * 
	 * @return
	 */
	private ParameterValueAssignment createConstantValueAssignment() {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(assignment.getParameter());
		// TODO value
		cva.setValue("");
		return cva;
	}

	/**
	 * Saves the current config of this ExperimentAssignment.
	 */
	private void storeAssignment() {
		ScenarioManager.get().experiment().setExperimentAssignment(getValueAssignment());
	}
}
