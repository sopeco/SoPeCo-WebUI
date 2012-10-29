package org.sopeco.frontend.client.layout.center.experiment;

import java.util.HashMap;
import java.util.Map;

import org.sopeco.frontend.client.R;
import org.sopeco.persistence.entities.definition.ExperimentTerminationCondition;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TerminationView extends FlowPanel {

	private static final String TERMINATION_VIEW_ID = "terminationView";

	private Map<String, Condition> conditionMap = new HashMap<String, Condition>();

	public TerminationView() {
		getElement().setId(TERMINATION_VIEW_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("terminationCondition"));
		getElement().appendChild(headline);
	}

	/**
	 * 
	 * @param termination
	 */
	public void addCondition(ExperimentTerminationCondition termination) {
		Condition condition = new Condition(termination);

		conditionMap.put(termination.getName(), condition);
		add(condition);
	}

	/**
	 * @return the conditionPanels
	 */
	public Map<String, Condition> getConditionMap() {
		return conditionMap;
	}

	/**
	 * 
	 * @author Marius Oehler
	 * 
	 */
	class Condition extends FlowPanel implements HasValueChangeHandlers<Boolean>, ValueChangeHandler<Boolean> {
		private ExperimentTerminationCondition condition;
		private FlexTable table;
		private CheckBox conditionCheckbox;
		private HTML nameHTML;
		private Map<String, TextBox> textboxes;

		public Condition(ExperimentTerminationCondition terminationCondition) {
			condition = terminationCondition;

			init();
		}

		/**
		 * @return the condition
		 */
		public ExperimentTerminationCondition getCondition() {
			return condition;
		}

		public void setConditionVisibility(boolean show) {
			if (show) {
				table.setVisible(true);
				nameHTML.getElement().getStyle().setFontWeight(FontWeight.BOLD);
			} else {
				table.setVisible(false);
				nameHTML.getElement().getStyle().clearFontWeight();
			}
		}

		/**
		 * 
		 */
		private void init() {
			nameHTML = new HTML(condition.getName());
			nameHTML.getElement().setTitle(condition.getDescription());

			conditionCheckbox = new CheckBox();
			conditionCheckbox.getElement().getStyle().setFloat(Float.RIGHT);
			conditionCheckbox.addValueChangeHandler(this);

			textboxes = new HashMap<String, TextBox>();

			table = new FlexTable();

			int row = 0;
			for (String key : condition.getParametersDefaultValues().keySet()) {
				TextBox textbox = new TextBox();
				textbox.setText(condition.getParamValue(key));
				textbox.addValueChangeHandler(textboxValueChangeHandler());

				textboxes.put(key, textbox);

				table.setWidget(row, 0, new HTML(key));
				table.setWidget(row, 1, textbox);

				row++;
			}

			add(nameHTML);
			add(conditionCheckbox);
			add(table);

			setConditionVisibility(false);
		}

		/**
		 * 
		 * @return
		 */
		public Map<String, String> getConfig() {
			Map<String, String> config = new HashMap<String, String>();

			for (String key : textboxes.keySet()) {
				config.put(key, textboxes.get(key).getText());
			}

			return config;
		}

		/**
		 * 
		 * @return
		 */
		private ValueChangeHandler<String> textboxValueChangeHandler() {
			return new ValueChangeHandler<String>() {
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					ValueChangeEvent.fire(Condition.this, true);
				}
			};
		}

		@Override
		public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Boolean> handler) {
			return addHandler(handler, ValueChangeEvent.getType());
		}

		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			ValueChangeEvent.fire(this, event.getValue());
		}
	}
}
