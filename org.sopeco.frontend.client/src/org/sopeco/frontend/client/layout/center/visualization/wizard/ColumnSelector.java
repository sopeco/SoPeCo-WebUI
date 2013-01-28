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
package org.sopeco.frontend.client.layout.center.visualization.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.helper.AggregationInputType;
import org.sopeco.frontend.shared.helper.AggregationOutputType;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ColumnSelector extends HorizontalPanel {
	private int maxColumns = 1;
	private FlowPanel verticalPanel1;
	private FlowPanel verticalPanel2;
	private Map<ChartParameter, ListBox> selectors = new HashMap<ChartParameter, ListBox>();
	private List<ChartParameter> inputParameter;
	private List<ChartParameter> outputParameter;

	public ColumnSelector() {
		inputParameter = new ArrayList<ChartParameter>();
		outputParameter = new ArrayList<ChartParameter>();
		verticalPanel1 = new FlowPanel();
		this.add(verticalPanel1);
		verticalPanel2 = new FlowPanel();
		this.add(verticalPanel2);
	}

	public void setChartParameter(List<ChartParameter> inputParameter, List<ChartParameter> outputParameter) {
		this.inputParameter = inputParameter;
		this.outputParameter = outputParameter;
	}


	public void showColumnSelection() {
		verticalPanel1.clear();
		verticalPanel2.clear();
		final ListBox nrPicker = new ListBox();
		for (int i = 0; i < outputParameter.size(); i++){
			nrPicker.addItem(""+(i+1));
		}
		nrPicker.setSelectedIndex(maxColumns-1);
		nrPicker.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				maxColumns = nrPicker.getSelectedIndex()+1;
				showColumnSelection();
			}
		});
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(4);
		horizontalPanel.add(new Label("No. of outputs"));
		horizontalPanel.add(nrPicker);
		verticalPanel2.add(horizontalPanel);
		createChartInputWidgets();
		createChartOutputWidgets();
	}
	
	private void createChartInputWidgets(){
		for (int i = 0; i < inputParameter.size(); i++) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(4);
			hp.add(new Label(inputParameter.get(i).getParameterName()));
			final ListBox lb = new ListBox();
			selectors.put(inputParameter.get(i), lb);
			hp.add(lb);
			for (AggregationInputType t : AggregationInputType.values()) {
				lb.addItem(t.name());
			}
			verticalPanel1.add(hp);
		}
		
		return;
	}
	
	private void createChartOutputWidgets(){
		for (int i = 0; i < outputParameter.size(); i++) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.setSpacing(4);
			hp.add(new Label(outputParameter.get(i).getParameterName()));
			final ListBox lb = new ListBox();
			selectors.put(outputParameter.get(i), lb);
			hp.add(lb);
			for (AggregationOutputType t : AggregationOutputType.values()) {
				lb.addItem(t.name());
			}
			verticalPanel1.add(hp);
		}
		
		return;
	}

	public List<ChartParameter> getSelectedColumns() {
		List<ChartParameter> sel = new ArrayList<ChartParameter>();
		for (int i = 0; i < inputParameter.size(); i++){
			ListBox lb = selectors.get(inputParameter.get(i));
			inputParameter.get(i).setAggregationInputType(AggregationInputType.valueOf(AggregationInputType.class, lb.getItemText(lb.getSelectedIndex())));
			sel.add(inputParameter.get(i));
		}
		for (int i = 0; i < outputParameter.size(); i++){
			ListBox lb = selectors.get(outputParameter.get(i));
			outputParameter.get(i).setAggregationOutputType(AggregationOutputType.valueOf(AggregationOutputType.class, lb.getItemText(lb.getSelectedIndex())));
			sel.add(outputParameter.get(i));
		}
		return sel;
	}
}
