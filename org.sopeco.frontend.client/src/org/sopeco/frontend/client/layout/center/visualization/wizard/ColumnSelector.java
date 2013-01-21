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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ColumnSelector extends VerticalPanel {
	private int maxColumns;
	private List<ListBox> columnSelector = new ArrayList<ListBox>();
	private ChartParameter[] chartParameter;

	public ColumnSelector() {
	}

	public int getMaxColumns() {
		return maxColumns;
	}


	public void setMaxColumns(int maxColumns) {
		this.maxColumns = maxColumns;
	}


	public ChartParameter[] getChartParameter() {
		return chartParameter;
	}


	public void setChartParameter(ChartParameter[] chartParameter) {
		this.chartParameter = chartParameter;
	}


	public void showColumnSelection() {
		this.clear();
		final ListBox nrPicker = new ListBox();
		nrPicker.addItem("2");
		nrPicker.addItem("3");
		nrPicker.addItem("4");
		nrPicker.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				maxColumns = Integer.parseInt(nrPicker.getValue(nrPicker.getSelectedIndex()));
				showColumnSelection();
			}
		});
		this.add(nrPicker);
		columnSelector.clear();
		for (int i = 0; i < maxColumns; i++) {
			HorizontalPanel hp = new HorizontalPanel();
			hp.add(new Label("Column " + i + ": "));
			final ListBox lb = new ListBox();
			columnSelector.add(lb);
			hp.add(lb);
			for (int j = 0; j < chartParameter.length; j++) {
				lb.addItem(chartParameter[j].getParameterName());
			}
			this.add(hp);
		}
	}

	public ChartParameter[] getSelectedColumns() {
		ChartParameter[] sel = new ChartParameter[maxColumns];
		System.out.println("getSelectedColumns() maxColumns: " + maxColumns);
		for (int i = 0; i < sel.length; i++){
			sel[i] = chartParameter[columnSelector.get(i).getSelectedIndex()];
			System.out.println("i: " + i + " seli: " + columnSelector.get(i).getSelectedIndex() + " sel[i]: " + sel[i]);
		}
		return sel;
	}
}
