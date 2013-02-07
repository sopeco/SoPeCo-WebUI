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
package org.sopeco.webui.client.layout.center.experiment.assignment.items;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.webui.client.extensions.Extensions;
import org.sopeco.webui.client.layout.center.experiment.assignment.AssignmentController;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.shared.helper.ExtensionTypes;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentItem implements ValueChangeHandler<String> {

	private static Map<String, String[]> allowedExtension;

	private static final String EXT_BOOLEAN_VARIATION = "Boolean Variation";
	private static final String EXT_CSV_VARIATION = "CSV";
	private static final String CONSTANT_VARIATION = "Constant Variation";

	private ParameterValueAssignment assignment;
	private ComboBox combobox;
	private String currentVariationName;

	private SimplePanel parameterWrapper;
	private ParameterPanel parameterPanel;

	private AssignmentController controller;

	public AssignmentItem(ParameterValueAssignment pAssignment) {
		this.assignment = pAssignment;
		parameterWrapper = new SimplePanel();

		initCombobox();
		initParameterPanel();
	}

	public void initParameterPanel() {
		if (assignment instanceof DynamicValueAssignment) {
			if (currentVariationName.equals(EXT_BOOLEAN_VARIATION)) {
				parameterPanel = new EmptyParameterPanel(this);
			} else if (currentVariationName.equals(EXT_CSV_VARIATION)) {
				parameterPanel = new CSVParameterPanel(this);
			} else {
				parameterPanel = new KVParameterPanel(this);
			}
		} else {
			parameterPanel = new ConstantParameterPanel(this, ((ConstantValueAssignment) assignment).getValue());
		}
		parameterWrapper.clear();
		parameterWrapper.add(parameterPanel);
	}

	public String getCurrentVariationName() {
		return currentVariationName;
	}

	private ParameterValueAssignment createValueAssignment(String variationName) {
		if (variationName.equals(CONSTANT_VARIATION)) {
			return createConstantValueAssignment();
		} else {
			return createDynamicValueAssignment(variationName);
		}
	}

	private DynamicValueAssignment createDynamicValueAssignment(String variationName) {
		DynamicValueAssignment dva = new DynamicValueAssignment();

		dva.setName(variationName);
		dva.setParameter(assignment.getParameter());
		dva.getConfiguration().putAll(
				Extensions.get().getExtensions(ExtensionTypes.PARAMETERVARIATION).get(variationName));

		return dva;
	}

	private ParameterValueAssignment createConstantValueAssignment() {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(assignment.getParameter());
		cva.setValue("");
		return cva;
	}

	public SimplePanel getParameterWrapper() {
		return parameterWrapper;
	}

	public ParameterValueAssignment getAssignment() {
		return assignment;
	}

	public void setController(AssignmentController pController) {
		this.controller = pController;
	}

	private void initCombobox() {
		combobox = new ComboBox();
		combobox.setEditable(false);
		combobox.addValueChangeHandler(this);

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
		if (currentVariationName == null) {
			currentVariationName = combobox.getText();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (controller != null) {
			controller.onValueChange(this, event.getValue());
		}
		currentVariationName = event.getValue();
		assignment = createValueAssignment(event.getValue());
		initParameterPanel();

		storeAssignment();
	}

	public ComboBox getCombobox() {
		return combobox;
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

	public void storeAssignment() {
		ScenarioManager.get().experiment().setExperimentAssignment(parameterPanel.getValueAssignment());
	}
}
