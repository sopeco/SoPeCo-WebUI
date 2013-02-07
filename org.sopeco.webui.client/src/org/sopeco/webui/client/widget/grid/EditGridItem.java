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
package org.sopeco.webui.client.widget.grid;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EditGridItem implements ValueChangeHandler<String> {

	private String value;
	private EditableText editText;
	private ParameterDefinition parameter;

	private EditGridHandler handler;

	public EditGridItem(ConstantValueAssignment cva) {
		this(cva.getParameter(), cva.getValue());
	}

	public EditGridItem(ParameterDefinition pParameter, String pValue) {
		parameter = pParameter;
		this.value = pValue;

		editText = new EditableText(value);
		editText.addValueChangeHandler(this);

		if (pParameter.getName().toLowerCase().equals("password")
				|| pParameter.getName().toLowerCase().equals("passwd")) {
			editText.setAsPassword();
		}

		if (parameter.getType().toLowerCase().equals("integer")) {
			editText.setValidPattern(EditableText.PATTERN_INTEGER);
		} else if (parameter.getType().toLowerCase().equals("double")) {
			editText.setValidPattern(EditableText.PATTERN_DOUBLE);
		} else if (parameter.getType().toLowerCase().equals("boolean")) {
			editText.setValidPattern(EditableText.PATTERN_BOOLEAN);
		}
	}

	/**
	 * Returns the namespace path.
	 * 
	 * @return
	 */
	public String getNamespace() {
		return parameter.getNamespace().getFullName();
	}

	/**
	 * Returns namespace + name.
	 * 
	 * @return
	 */
	public String getFullName() {
		return parameter.getFullName();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return parameter.getName();
	}

	public String getType() {
		return parameter.getType();
	}

	public String getValue() {
		return value;
	}

	public EditableText getEditText() {
		return editText;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		value = event.getValue();
		if (handler != null) {
			handler.onValueChange(this);
		}
	}

	public void setHandler(EditGridHandler pHandler) {
		this.handler = pHandler;
	}

	public ParameterDefinition getParameter() {
		return parameter;
	}

}