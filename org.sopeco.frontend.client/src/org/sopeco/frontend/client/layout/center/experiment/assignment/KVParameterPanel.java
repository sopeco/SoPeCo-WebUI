package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class KVParameterPanel extends ParameterPanel implements ValueChangeHandler<String>, ClickHandler, BlurHandler,
		MouseOutHandler, MouseOverHandler, FocusHandler {

	private Map<String, String> configMap;
	private Map<String, Pair> valuePairs;

	private boolean isOverTextbox = false;

	public KVParameterPanel(AssignmentItem item, Map<String, String> config) {
		super(item);

		configMap = config;

		initialize();
	}

	/**
	 * 
	 */
	private void initialize() {
		addStyleName(EA_CONFIG_MAP_CSS_CLASS);
		getElement().getStyle().setDisplay(Display.BLOCK);

		valuePairs = new HashMap<String, Pair>();

		FlexTable configTable = new FlexTable();

		int row = 0;
		for (String key : configMap.keySet()) {
			Pair pair = new Pair(configMap.get(key));
			pair.getTextbox().addValueChangeHandler(this);
			pair.getTextbox().setVisible(false);
			pair.getTextbox().addBlurHandler(this);
			pair.getTextbox().addMouseOutHandler(this);
			pair.getTextbox().addMouseOverHandler(this);
			pair.getTextbox().addFocusHandler(this);
			pair.getHtml().addClickHandler(this);
			pair.getHtml().getElement().getStyle().setCursor(Cursor.POINTER);
			pair.getHtml().addStyleName(EDITABLE_TEXT_CSS_CLASS);

			valuePairs.put(key, pair);

			FlowPanel valueWrapper = new FlowPanel();
			valueWrapper.add(pair.getTextbox());
			valueWrapper.add(pair.getHtml());

			configTable.setWidget(row, 0, new HTML(key + ":"));
			configTable.setWidget(row, 1, valueWrapper);

			row++;
		}

		add(configTable);
	}

	@Override
	public String getValue() {
		return "";
	}

	@Override
	public void onFocus(FocusEvent event) {
		if (!((TextBox) event.getSource()).isVisible()) {
			for (Pair pair : valuePairs.values()) {
				pair.getHtml().setVisible(false);
				pair.getTextbox().setVisible(true);
			}
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		for (Pair pair : valuePairs.values()) {
			pair.getHtml().setVisible(false);
			pair.getTextbox().setVisible(true);
		}

		((HTMLExt) event.getSource()).getPair().getTextbox().setFocus(true);
	}

	@Override
	public void onBlur(BlurEvent event) {
		if (!isOverTextbox) {
			for (Pair pair : valuePairs.values()) {
				pair.getHtml().setHTML(pair.getTextbox().getText());
				emptyValueHTML(pair.getHtml());
				pair.getTextbox().setVisible(false);
				pair.getHtml().setVisible(true);
			}
		}
	}

	@Override
	public Map<String, String> getConfig() {
		Map<String, String> returnMap = new HashMap<String, String>();

		for (String key : valuePairs.keySet()) {
			returnMap.put(key, valuePairs.get(key).getTextbox().getText());
		}

		return returnMap;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		GWT.log("val change");
		((ExperimentAssignmentItem) assignmentItem).storeAssignment();
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		isOverTextbox = false;
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		isOverTextbox = true;
	}

	private class Pair {
		private HTMLExt html;
		private TextBox textbox;

		public Pair(String value) {
			html = new HTMLExt(value, this);
			emptyValueHTML(html);
			textbox = new TextBox();
			textbox.setText(value);
		}

		/**
		 * @return the html
		 */
		public HTML getHtml() {
			return html;
		}

		/**
		 * @return the textbox
		 */
		public TextBox getTextbox() {
			return textbox;
		}
	}

	private class HTMLExt extends HTML {
		private Pair pair;

		public HTMLExt(String value, Pair parent) {
			super(value);
			pair = parent;
		}

		/**
		 * @return the pair
		 */
		public Pair getPair() {
			return pair;
		}
	}
}
