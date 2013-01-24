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

import org.sopeco.frontend.client.layout.center.experiment.assignment.CSVEditor;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CSVParameterPanel extends ParameterPanel implements ValueChangeHandler<String>, ClickHandler {

	private static final String IMAGE_EDIT = "images/list_white.png";
	private Image editImage;
	private HTML label;
	private EditableText editText;
	private String csvKey;
	private CSVEditor csvEditor;

	public CSVParameterPanel(AssignmentItem item) {
		super(item);

		if (getDVAConfiguration().size() != 1) {
			throw new IllegalStateException();
		}
		csvKey = getDVAConfiguration().keySet().iterator().next();

		initialize();
	}

	/**
	 * Initialize the UI and all necessary elements.
	 * 
	 * @param value
	 */
	private void initialize() {
		getElement().getStyle().setDisplay(Display.BLOCK);

		label = new HTML(R.get("values") + ":");
		label.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getElement().getStyle().setProperty("whiteSpace", "nowrap");

		editText = new EditableText(getDVAConfiguration().get(csvKey));
		editText.addValueChangeHandler(this);

		editImage = new Image(IMAGE_EDIT);
		editImage.getElement().getStyle().setPaddingBottom(1, Unit.PX);
		editImage.getElement().getStyle().setCursor(Cursor.POINTER);
		editImage.addClickHandler(this);
		editImage.setTitle(R.get("showCsvEditor"));

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.setWidth("100%");

		hPanel.add(label);
		hPanel.add(editText);
		hPanel.add(editImage);

		hPanel.setCellWidth(label, "1px");
		hPanel.setCellWidth(editImage, "1px");

		add(hPanel);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == editImage) {
			if (csvEditor == null) {
				csvEditor = new CSVEditor();
				csvEditor.addValueChangeHandler(this);
			}
			csvEditor.setValue(editText.getValue());

			csvEditor.center();
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		if (event.getSource() == editText) {
			getAssignmentItem().storeAssignment();
		} else if (event.getSource() == csvEditor) {
			if (event.getValue().equals(editText.getValue())) {
				return;
			}
			editText.setValue(event.getValue(), true);
		}
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		DynamicValueAssignment dva = createDynamicValueAssignment();
		dva.getConfiguration().put(csvKey, editText.getValue());
		return dva;
	}

}
