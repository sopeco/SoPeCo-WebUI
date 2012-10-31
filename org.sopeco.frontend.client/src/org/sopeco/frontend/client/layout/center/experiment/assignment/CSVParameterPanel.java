package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.Map;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CSVParameterPanel extends ParameterPanel implements ClickHandler, ValueChangeHandler<String> {

	private static final int LABEL_MARGIN_RIGHT = 24;
	private static final int MAX_CSV_LENGTH = 70;
	
	private HTML label, valueHtml;
	private Map<String, String> configMap;
	private String firstKey;

	private CSVEditor csvEditor;

	public CSVParameterPanel(AssignmentItem item, Map<String, String> config) {
		super(item);

		configMap = config;

		if (configMap.size() != 1) {
			throw new IllegalStateException();
		}
		firstKey = configMap.keySet().iterator().next();

		initialize();
	}

	private void initialize() {
		addStyleName(EA_CONFIG_MAP_CSS_CLASS);
		getElement().getStyle().setDisplay(Display.BLOCK);

		label = new HTML(R.get("values") + ":");
		label.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		label.getElement().getStyle().setMarginRight(LABEL_MARGIN_RIGHT, Unit.PX);

		valueHtml = new HTML(shortenString(configMap.get(firstKey)));
		valueHtml.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		valueHtml.addClickHandler(this);
		valueHtml.getElement().getStyle().setCursor(Cursor.POINTER);
		valueHtml.getElement().getStyle().setVerticalAlign(VerticalAlign.TEXT_BOTTOM);
		valueHtml.addStyleName(EDITABLE_TEXT_CSS_CLASS);

		emptyValueHTML(valueHtml);

		add(label);
		add(valueHtml);
	}

	@Override
	public Map<String, String> getConfig() {
		return configMap;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void onClick(ClickEvent event) {
		showCSVEditor();
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		configMap.put(firstKey, event.getValue());
		valueHtml.setHTML(shortenString(configMap.get(firstKey)));
		emptyValueHTML(valueHtml);

		((ExperimentAssignmentItem) assignmentItem).storeAssignment();
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	private String shortenString(String s) {
		String result = s.replaceAll(",", ", ");

		if (result.length() > MAX_CSV_LENGTH) {
			String cut = result.substring(0, MAX_CSV_LENGTH);
			int sepCut = cut.lastIndexOf(",");

			String temp = result.substring(0, sepCut);
			result = temp + "<span style=\"color:#aaa;\"> ... [" + (result.substring(sepCut).split(",").length - 1)
					+ "]</span>";
		}

		return result;
	}

	/**
	 * 
	 */
	private void showCSVEditor() {
		if (csvEditor == null) {
			csvEditor = new CSVEditor();
			csvEditor.addValueChangeHandler(this);
		}

		csvEditor.setValue(configMap.get(firstKey));
		csvEditor.center();
	}
}