package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.Map;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class ParameterPanel extends FlowPanel {

	protected static final String EDITABLE_TEXT_CSS_CLASS = "editableText";
	protected static final String EA_CONFIG_MAP_CSS_CLASS = "editArea-configMap";
	public static final double VALUE_HTML_HEIGHT = 1.3;

	protected AssignmentItem assignmentItem;

	public ParameterPanel(AssignmentItem item) {
		assignmentItem = item;
	}

	public abstract Map<String, String> getConfig();

	public abstract String getValue();

	protected void emptyValueHTML(HTML htmlElement) {
		if (htmlElement.getText().isEmpty()) {
			htmlElement.getElement().getStyle().setWidth(2, Unit.EM);
			htmlElement.getElement().getStyle().setHeight(VALUE_HTML_HEIGHT, Unit.EM);
		} else {
			htmlElement.getElement().getStyle().clearBackgroundColor();
			htmlElement.getElement().getStyle().clearWidth();
			htmlElement.getElement().getStyle().clearHeight();
		}
	}
}
