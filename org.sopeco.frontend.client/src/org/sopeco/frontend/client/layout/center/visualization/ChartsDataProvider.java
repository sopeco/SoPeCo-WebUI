package org.sopeco.frontend.client.layout.center.visualization;

import java.util.List;

import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

public class ChartsDataProvider extends AsyncDataProvider<Visualization> {

	@Override
	protected void onRangeChanged(HasData<Visualization> display) {
		final Range range = display.getVisibleRange();
		RPC.getVisualizationRPC().getVisualizations(range.getStart(), range.getLength(), new AsyncCallback<List<Visualization>>() {
			
			@Override
			public void onSuccess(List<Visualization> result) {
				updateRowData(range.getStart(), result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void addDataDisplay(CellList<Visualization> visualizationList) {
		super.addDataDisplay(visualizationList);
	}

}
