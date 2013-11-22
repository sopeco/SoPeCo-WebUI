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

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ConstantParameterPanel extends ParameterPanel implements ValueChangeHandler<String> {

	private HTML label;
	private EditableText editText;

	public ConstantParameterPanel(AssignmentItem item, String value) {
		super(item);

		initialize(value);
	}

	/**
	 * Initialize the UI and all necessary elements.
	 * 
	 * @param value
	 */
	private void initialize(String value) {
		getElement().getStyle().setDisplay(Display.BLOCK);

		label = new HTML(R.lang.constantValue() + ":");
		label.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getElement().getStyle().setProperty("whiteSpace", "nowrap");

		editText = new EditableText(value);
		editText.addValueChangeHandler(this);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.setWidth("100%");

		hPanel.add(label);
		hPanel.add(editText);

		hPanel.setCellWidth(label, "1px");

		add(hPanel);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		getAssignmentItem().storeAssignment();
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		ConstantValueAssignment cva = new ConstantValueAssignment();
		cva.setParameter(getAssignmentItem().getAssignment().getParameter());
		cva.setValue(editText.getValue());
		return cva;
	}
}
