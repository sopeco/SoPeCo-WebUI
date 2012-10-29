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
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentItem extends AssignmentItem implements ValueChangeHandler<String> {

	private static final String EXT_BOOLEAN_VARIATION = "Boolean Variation";
	private static final String CONSTANT_VARIATION = "Constant Variation";

	private ComboBox combobox;
	private String currentVariationName;

	private static Map<String, String[]> allowedExtension;

	private ParameterPanel parameterPanel;

	public ExperimentAssignmentItem(ParameterValueAssignment valueAssignment) {
		super(valueAssignment);
	}

	private static final GwtEvent<?> LOADEVENT = new ExperimentAssignmentRenderedEvent();

	@Override
	protected void initValueArea() {
		combobox = new ComboBox();

		combobox.setEditable(false);
		combobox.addValueChangeHandler(this);
		initCombobox();

		add(combobox);

		setParameterPanel();
	}

	private void setParameterPanel() {
		if (parameterPanel != null && parameterPanel.isAttached()) {
			parameterPanel.removeFromParent();
		}

		if (assignment instanceof DynamicValueAssignment) {
			if (currentVariationName.equals(EXT_BOOLEAN_VARIATION)) {
				parameterPanel = new EmptyParameterPanel(this);
			} else {
				parameterPanel = new KVParameterPanel(this, ((DynamicValueAssignment) assignment).getConfiguration());
			}
		} else {
			parameterPanel = new ConstantParameterPanel(this, ((ConstantValueAssignment) assignment).getValue());
		}

		add(parameterPanel);
	}

	@Override
	protected GwtEvent<?> getOnLoadEvent() {
		return LOADEVENT;
	}

	/**
	 * Adds the values to the combobox.
	 */
	private void initCombobox() {
		Set<String> variationSet = new TreeSet<String>();

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
				if ((assignment instanceof DynamicValueAssignment && key.equals(((DynamicValueAssignment) assignment)
						.getName()))
						|| (assignment instanceof ConstantValueAssignment && key.equals(CONSTANT_VARIATION))) {
					combobox.setSelectedIndex(counter);
					currentVariationName = key;
				}
				counter++;
			}
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		currentVariationName = event.getValue();

		storeNewAssignment(event.getValue());

		setParameterPanel();
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

	private ParameterValueAssignment getConstantValueAssignment() {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(assignment.getParameter());
		cva.setValue(parameterPanel.getValue());
		return cva;
	}

	/**
	 * 
	 * @return
	 */
	private DynamicValueAssignment getDynamicValueAssignment() {
		DynamicValueAssignment dva = new DynamicValueAssignment();

		dva.setName(currentVariationName);
		dva.setParameter(assignment.getParameter());
		dva.getConfiguration().clear();
		dva.getConfiguration().putAll(parameterPanel.getConfig());

		return dva;
	}

	private ParameterValueAssignment getValueAssignment() {
		if (currentVariationName.equals(CONSTANT_VARIATION)) {
			return getConstantValueAssignment();
		} else {
			return getDynamicValueAssignment();
		}
	}

	private ParameterValueAssignment createValueAssignment(String variationName) {
		if (variationName.equals(CONSTANT_VARIATION)) {
			return createConstantValueAssignment();
		} else {
			return createDynamicValueAssignment(variationName);
		}
	}

	/**
	 * 
	 * @return
	 */
	private DynamicValueAssignment createDynamicValueAssignment(String variationName) {
		DynamicValueAssignment dva = new DynamicValueAssignment();

		dva.setName(variationName);
		dva.setParameter(assignment.getParameter());
		dva.getConfiguration().putAll(
				Extensions.get().getExtensions(ExtensionTypes.PARAMETERVARIATION).get(variationName));

		return dva;
	}

	/**
	 * 
	 * @return
	 */
	private ParameterValueAssignment createConstantValueAssignment() {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(assignment.getParameter());
		cva.setValue("");
		return cva;
	}

	/**
	 * Saves the current config of this ExperimentAssignment.
	 */
	private void storeNewAssignment(String variationName) {
		ParameterValueAssignment newValueAssignment = createValueAssignment(variationName);
		ScenarioManager.get().experiment().setExperimentAssignment(newValueAssignment);
		assignment = newValueAssignment;
	}

	public void storeAssignment() {
		ScenarioManager.get().experiment().setExperimentAssignment(getValueAssignment());
	}
}
