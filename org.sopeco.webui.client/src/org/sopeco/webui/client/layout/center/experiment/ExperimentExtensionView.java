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
package org.sopeco.webui.client.layout.center.experiment;

import org.sopeco.gwt.widgets.ComboBox;
import org.sopeco.gwt.widgets.EditableText;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentExtensionView extends FlowPanel {

	private static final String EXTENSION_VIEW_CSS_CLASS = "experimentExtensionView";
	private static final int DEFAULT_MARGIN = 20;

	private ComboBox combobox;
	private Element headline;
	private FlexTable configTable;

	private int width;

	public ExperimentExtensionView(int pxWidth) {
		width = pxWidth;

		initialize();
	}

	/**
	 * Inits all necessary objects.
	 */
	private void initialize() {
		addStyleName(EXTENSION_VIEW_CSS_CLASS);

		headline = DOM.createElement("h3");
		combobox = new ComboBox();
		configTable = new FlexTable();

		combobox.setWidth(width - 2 * DEFAULT_MARGIN);
		combobox.getElement().getStyle().setMarginLeft(DEFAULT_MARGIN, Unit.PX);
		combobox.getElement().getStyle().setMarginRight(DEFAULT_MARGIN, Unit.PX);
		combobox.setEditable(false);

		getElement().appendChild(headline);
		add(combobox);
		add(configTable);
	}

	/**
	 * Returns the headline H3-Element.
	 * 
	 * @return headline
	 */
	public Element getHeadline() {
		return headline;
	}

	/**
	 * @return the combobox
	 */
	public ComboBox getCombobox() {
		return combobox;
	}

	/**
	 * @return the configTable
	 */
	public FlexTable getConfigTable() {
		return configTable;
	}

	/**
	 * 
	 * @param labelText
	 * @param key
	 * @param value
	 * @return
	 */
	public EditableText addConfigRow(String labelText, String key, String value) {
		HTML htmlLabel = new HTML(labelText);
		// TextBox textboxValue = new TextBox();

		// textboxValue.setName(key);
		// textboxValue.setText(value);
		htmlLabel.setTitle(labelText);
		htmlLabel.addStyleName("label");

		EditableText editText = new EditableText(value);

		int row = configTable.getRowCount();

		configTable.setWidget(row, 0, htmlLabel);
		configTable.setWidget(row, 1, editText);

		return editText;
	}
}
