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
