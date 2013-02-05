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
import java.util.Collections;
import java.util.List;

import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.helper.Utilities;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ColumnSelectionPanel extends Grid {
	private ListBox input;
	private ListBox output;
	private List<ChartParameter> inputParameter;
	private List<ChartParameter> outputParameter;

	public ColumnSelectionPanel() {
		super(2, 2);
		this.getElement().getStyle().setWidth(100, Unit.PCT);
		inputParameter = new ArrayList<ChartParameter>();
		outputParameter = new ArrayList<ChartParameter>();
		showColumnSelection();
	}

	public void setChartParameter(List<ChartParameter> inputParameter, List<ChartParameter> outputParameter) {
		this.inputParameter = inputParameter;
		this.outputParameter = outputParameter;
		showColumnSelection();
	}


	public void showColumnSelection() {
		this.clear();
		createChartInputWidget();
		createChartOutputWidget();
	}
	
	private void createChartInputWidget(){
		Collections.sort(inputParameter);
		this.setWidget(0, 0, new Label("Input "));
		input = new ListBox();
		for (ChartParameter p : inputParameter){
			input.addItem(Utilities.trimParameter(p.getParameterName()));
		}
		this.setWidget(0, 1, input); 
		return;
	}
	
	private void createChartOutputWidget(){
		Collections.sort(outputParameter);
		this.setWidget(1, 0, new Label("Observation Value "));
		output = new ListBox();
		for (ChartParameter p : outputParameter){
			output.addItem(Utilities.trimParameter(p.getParameterName()));
		}
		this.setWidget(1, 1, output);
		
		return;
	}
	
	public ChartParameter getSelectedOutput(){
		for (ChartParameter cp : outputParameter){
			if (Utilities.trimParameter(cp.getParameterName()).equals(output.getItemText(output.getSelectedIndex()))){
				return cp;
			}
		}
		return null;
	}
	
	public ChartParameter getSelectedInput(){
		for (int i = 0; i < inputParameter.size(); i++){
			if (Utilities.trimParameter(inputParameter.get(i).getParameterName()).equals(input.getValue(input.getSelectedIndex()))){
				return inputParameter.get(i);
			}
		}
		return null;
	}
}
