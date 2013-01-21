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
package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class KVParameterPanel extends ParameterPanel implements ValueChangeHandler<String> {

	private Map<String, EditableText> editableTextMap;

	public KVParameterPanel(AssignmentItem item) {
		super(item);
		initialize();
	}

	/**
	 * Initializes the UI and all necessary elements.
	 */
	private void initialize() {
		editableTextMap = new HashMap<String, EditableText>();

		updateMap(getDVAConfiguration());

		Grid grid = new Grid(editableTextMap.size(), 2);
		grid.setWidth("100%");
		grid.getColumnFormatter().setWidth(0, "1px");
		int row = 0;
		for (String key : editableTextMap.keySet()) {
			grid.setWidget(row, 0, new HTML(key + ":"));
			grid.setWidget(row, 1, editableTextMap.get(key));
			row++;
		}

		add(grid);
	}

	/**
	 * Creates the editable textfield and stores them in the editableTextMap.
	 * The key is the same as that of the configuration map.
	 * 
	 * @param config
	 */
	private void updateMap(Map<String, String> config) {
		for (String key : config.keySet()) {
			EditableText editText = new EditableText(config.get(key));
			editText.addValueChangeHandler(this);
			editableTextMap.put(key, editText);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		getAssignmentItem().storeAssignment();
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		DynamicValueAssignment dva = createDynamicValueAssignment();
		dva.getConfiguration().clear();
		for (String key : editableTextMap.keySet()) {
			dva.getConfiguration().put(key, editableTextMap.get(key).getValue());
		}
		return dva;
	}

}
