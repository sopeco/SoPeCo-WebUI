package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

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

		label = new HTML(R.get("constantValue") + ":");
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
