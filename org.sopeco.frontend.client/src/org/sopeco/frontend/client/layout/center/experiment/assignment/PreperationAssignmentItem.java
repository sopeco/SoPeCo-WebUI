package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.event.PreperationAssignmentRenderedEvent;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentItem extends AssignmentItem implements ValueChangeHandler<String> {

	private TextBox textboxValue;

	public PreperationAssignmentItem(ParameterValueAssignment valueAssignment) {
		super(valueAssignment);
	}

	private static final GwtEvent<?> LOADEVENT = new PreperationAssignmentRenderedEvent();

	@Override
	protected void initValueArea() {
		textboxValue = new TextBox();
		textboxValue.addValueChangeHandler(this);

		if (assignment instanceof ConstantValueAssignment) {
			textboxValue.setValue(((ConstantValueAssignment) assignment).getValue());
		}

		add(textboxValue);
	}

	@Override
	protected GwtEvent<?> getOnLoadEvent() {
		return LOADEVENT;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		ScenarioManager.get().experiment().setPreperationAssignmentValue(assignment.getParameter(), event.getValue());
	}
}
