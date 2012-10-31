package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.event.PreperationAssignmentRenderedEvent;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentItem extends AssignmentItem implements ValueChangeHandler<String>, ClickHandler,
		BlurHandler {

	private static final String EDITABLE_TEXT_CSS_CLASS = "editableText";

	private TextBox textboxValue;
	private HTML valueHTML;

	public PreperationAssignmentItem(ParameterValueAssignment valueAssignment) {
		super(valueAssignment);
	}

	private static final GwtEvent<?> LOADEVENT = new PreperationAssignmentRenderedEvent();

	@Override
	protected void initValueArea() {
		textboxValue = new TextBox();
		textboxValue.addValueChangeHandler(this);
		textboxValue.setVisible(false);
		textboxValue.addBlurHandler(this);

		valueHTML = new HTML();
		valueHTML.addStyleName(EDITABLE_TEXT_CSS_CLASS);
		valueHTML.addClickHandler(this);
		valueHTML.getElement().getStyle().setCursor(Cursor.POINTER);
		valueHTML.getElement().getStyle().setVerticalAlign(VerticalAlign.TEXT_BOTTOM);

		if (assignment instanceof ConstantValueAssignment) {
			textboxValue.setValue(((ConstantValueAssignment) assignment).getValue());
			valueHTML.setText(textboxValue.getValue());
		}

		emptyValueHTML(valueHTML);

		add(valueHTML);
		add(textboxValue);
	}

	private void emptyValueHTML(HTML htmlElement) {
		if (htmlElement.getText().isEmpty()) {
			htmlElement.getElement().getStyle().setWidth(2, Unit.EM);
			htmlElement.getElement().getStyle().setHeight(ParameterPanel.VALUE_HTML_HEIGHT, Unit.EM);
		} else {
			htmlElement.getElement().getStyle().clearBackgroundColor();
			htmlElement.getElement().getStyle().clearWidth();
			htmlElement.getElement().getStyle().clearHeight();
		}
	}

	@Override
	protected GwtEvent<?> getOnLoadEvent() {
		return LOADEVENT;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		ScenarioManager.get().experiment().setPreperationAssignmentValue(assignment.getParameter(), event.getValue());
	}

	@Override
	public void onClick(ClickEvent event) {
		valueHTML.setVisible(false);
		textboxValue.setVisible(true);
		textboxValue.setFocus(true);
	}

	public void onBlur(BlurEvent event) {
		textboxValue.setVisible(false);
		valueHTML.setText(textboxValue.getText());
		valueHTML.setVisible(true);
		emptyValueHTML(valueHTML);
	};
}
