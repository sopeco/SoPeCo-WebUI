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
	 * @return
	 */
	public Condition addCondition(ExperimentTerminationCondition termination) {
		Condition condition = new Condition(termination);

		conditionMap.put(termination.getName(), condition);
		add(condition);

		return condition;
	}

	/**
	 * @return the conditionPanels
	 */
	public Map<String, Condition> getConditionMap() {
		return conditionMap;
	}

	@Override
	public void clear() {
		for (Condition c : conditionMap.values()) {
			c.removeFromParent();
		}

		conditionMap.clear();
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
			ExperimentTerminationCondition temp = new ExperimentTerminationCondition();
			temp.setName(condition.getName());
			temp.setDescription(condition.getDescription());
			temp.getParametersDefaultValues().putAll(condition.getParametersDefaultValues());
			temp.getParametersValues().putAll(getConfig());

			return temp;
		}

		public void setConfiguration(Map<String, String> config) {
			for (String key : config.keySet()) {
				textboxes.get(key).setText(config.get(key));
			}
		}

		public void setConditionVisibility(boolean show) {
			if (show) {
				table.setVisible(true);
				nameHTML.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				conditionCheckbox.setValue(true);
			} else {
				table.setVisible(false);
				nameHTML.getElement().getStyle().clearFontWeight();
				conditionCheckbox.setValue(false);
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
				textbox.setText(condition.getParametersDefaultValues().get(key));
				textbox.addValueChangeHandler(textboxValueChangeHandler());
				textbox.setTitle("Default: " + condition.getParametersDefaultValues().get(key));

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
