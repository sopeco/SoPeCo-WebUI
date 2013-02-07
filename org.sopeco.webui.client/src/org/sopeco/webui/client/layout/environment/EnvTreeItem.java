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
package org.sopeco.webui.client.layout.environment;

import org.sopeco.gwt.widgets.tree.TreeItem;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.layout.environment.EnvironmentTree.ECheckBox;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvTreeItem extends TreeItem implements ValueChangeHandler<Boolean> {

	private static final String CSS_LEAF_CLASS = "envTreeItemLeaf";
	private Element textElement;
	private CheckBox firstCheckBox, secondCheckBox;
	private boolean hasSecondCheckbox;
	private EnvironmentTree environmentTree;
	private ParameterDefinition parameter;

	public EnvTreeItem(EnvironmentTree pEnvironmentTree, ParameterDefinition pPparameter, boolean pHasSecondCheckbox) {
		super(pPparameter.getName());
		hasSecondCheckbox = pHasSecondCheckbox;
		environmentTree = pEnvironmentTree;

		parameter = pPparameter;

		addCheckBoxes();
	}

	public void setCheckboxesEnable(boolean enable) {
		firstCheckBox.setEnabled(enable);
		if (hasSecondCheckbox) {
			secondCheckBox.setEnabled(enable);
		}
	}

	public void setFirstCheckboxValue(boolean value) {
		firstCheckBox.setValue(value);
	}

	public void setSecondCheckboxValue(boolean value) {
		if (hasSecondCheckbox) {
			secondCheckBox.setValue(value);
		}
	}

	@Override
	protected void initialize(boolean noContent) {
		addStyleName(CSS_LEAF_CLASS);
		setHeight("26px");

		textElement = DOM.createSpan();
		textElement.setInnerHTML(getText());
		textElement.setTitle(getText());
		textElement.addClassName("text");

		getElement().appendChild(textElement);
	}

	private void addCheckBoxes() {
		firstCheckBox = new CheckBox();
		firstCheckBox.addValueChangeHandler(this);
		add(firstCheckBox);

		if (hasSecondCheckbox) {
			secondCheckBox = new CheckBox();
			secondCheckBox.addValueChangeHandler(this);
			add(secondCheckBox);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		ECheckBox checkbox;
		if (event.getSource() == firstCheckBox) {
			checkbox = ECheckBox.FIRST;
		} else {
			checkbox = ECheckBox.SECOND;
		}
		environmentTree.onClick(checkbox, this, event.getValue());

	}

	/**
	 * @return the parameter
	 */
	public ParameterDefinition getParameter() {
		return parameter;
	}

}
