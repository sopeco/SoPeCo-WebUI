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
import java.util.List;

import org.sopeco.frontend.shared.entities.ChartParameter;

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
	private List<ListBox> columnSelector = new ArrayList<ListBox>();
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
		columnSelector.clear();
		
		verticalPanel1.add(createChartSelectorWidget("x-Axis ", inputParameter));
		for (int i = 0; i < maxColumns; i++) {
			verticalPanel2.add(createChartSelectorWidget("Output " + i + " ", outputParameter));
		}
	}
	
	private Widget createChartSelectorWidget(String name, List<ChartParameter> params){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		hp.add(new Label(name));
		final ListBox lb = new ListBox();
		columnSelector.add(lb);
		hp.add(lb);
		for (int j = 0; j < params.size(); j++) {
			lb.addItem(params.get(j).getParameterName());
		}
		return hp;
	}

	public List<ChartParameter> getSelectedColumns() {
		List<ChartParameter> sel = new ArrayList<ChartParameter>();
		sel.add(inputParameter.get(columnSelector.get(0).getSelectedIndex()));
		for (int i = 1; i < columnSelector.size(); i++){
			sel.add(outputParameter.get(columnSelector.get(i).getSelectedIndex()));
		}
		return sel;
	}
}
