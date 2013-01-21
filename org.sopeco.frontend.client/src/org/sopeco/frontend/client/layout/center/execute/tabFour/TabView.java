package org.sopeco.frontend.client.layout.center.execute.tabFour;

import org.sopeco.frontend.shared.entities.ExecutedExperimentDetails;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabView extends FlowPanel {

	private DataGrid<ExecutedExperimentDetails> dataGrid;
	private FlowPanel detailPanel;

	private SplitLayoutPanel splitLayout;

	public TabView() {
		detailPanel = new FlowPanel();
		detailPanel.addStyleName("historyDetails");

		splitLayout = new SplitLayoutPanel() {
			@Override
			public void onResize() {
				super.onResize();
				dataGrid.onResize();
			}
		};
		splitLayout.setHeight("100%");
		splitLayout.add(detailPanel);

		add(splitLayout);
	}

	public void setDataGrid(DataGrid<ExecutedExperimentDetails> pDataGrid) {
		dataGrid = pDataGrid;

		splitLayout.clear();
		splitLayout.addNorth(dataGrid, 200);
		splitLayout.add(detailPanel);
	}

	public DataGrid<ExecutedExperimentDetails> getDataGrid() {
		return dataGrid;
	}

	public FlowPanel getDetailPanel() {
		return detailPanel;
	}
}
