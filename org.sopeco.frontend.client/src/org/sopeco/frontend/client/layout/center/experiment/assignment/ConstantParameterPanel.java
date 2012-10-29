package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.Map;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ConstantParameterPanel extends ParameterPanel implements ClickHandler, BlurHandler,
		ValueChangeHandler<String> {

	private static final int LABEL_MARGIN_RIGHT = 24;

	private HTML label, valueHtml;
	private TextBox textbox;

	public ConstantParameterPanel(AssignmentItem item, String value) {
		super(item);

		initialize(value);
	}

	/**
	 * 
	 */
	private void initialize(String value) {
		addStyleName(EA_CONFIG_MAP_CSS_CLASS);
		getElement().getStyle().setDisplay(Display.BLOCK);

		label = new HTML(R.get("constantValue") + ":");
		label.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getElement().getStyle().setMarginRight(LABEL_MARGIN_RIGHT, Unit.PX);

		textbox = new TextBox();
		textbox.setText(value);
		textbox.setVisible(false);
		textbox.addBlurHandler(this);
		textbox.addValueChangeHandler(this);

		valueHtml = new HTML(value);
		valueHtml.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		valueHtml.addClickHandler(this);
		valueHtml.getElement().getStyle().setCursor(Cursor.POINTER);
		valueHtml.getElement().getStyle().setVerticalAlign(VerticalAlign.TEXT_BOTTOM);

		emptyValueHTML(valueHtml);

		add(label);
		add(valueHtml);
		add(textbox);
	}

	@Override
	public Map<String, String> getConfig() {
		return null;
	}

	@Override
	public String getValue() {
		return textbox.getText();
	}

	public void setValue(String value) {
		textbox.setText(value);
	}

	@Override
	public void onBlur(BlurEvent event) {
		valueHtml.setHTML(textbox.getText());
		textbox.setVisible(false);
		valueHtml.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);

		emptyValueHTML(valueHtml);
	}

	@Override
	public void onClick(ClickEvent event) {
		valueHtml.setVisible(false);
		textbox.setVisible(true);
		textbox.setFocus(true);
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		((ExperimentAssignmentItem) assignmentItem).storeAssignment();
	}
}
