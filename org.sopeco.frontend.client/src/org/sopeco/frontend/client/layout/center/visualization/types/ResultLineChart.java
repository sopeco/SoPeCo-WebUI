package org.sopeco.frontend.client.layout.center.visualization.types;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;;

public class ResultLineChart extends FlowPanel implements VisualizationType {
	private SharedExperimentRuns experitmentRun;

	public ResultLineChart(SharedExperimentRuns experitmentRun){
		this.experitmentRun = experitmentRun;
		Runnable onLoadCallback = new Runnable() {
		      public void run() {
		        Panel panel = ResultLineChart.this;
		 
		        // Create a pie chart visualization.
		        LineChart lineChart = new LineChart(createTable(), createOptions());

		        lineChart.addSelectHandler(createSelectHandler(lineChart));
		        panel.add(lineChart);
		      }
		    };

		    // Load the visualization api, passing the onLoadCallback to be called
		    // when loading is done.
		    VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);

	}
	
	private Options createOptions() {
	    Options options = Options.create();
	    options.setWidth(800);
	    options.setHeight(600);
	    options.setTitle(experitmentRun.getLabel());
	    return options;
	  }

	  private SelectHandler createSelectHandler(final LineChart chart) {
	    return new SelectHandler() {
	      @Override
	      public void onSelect(SelectEvent event) {
	        String message = "";
	        
	        // May be multiple selections.
	        JsArray<Selection> selections = chart.getSelections();

	        for (int i = 0; i < selections.length(); i++) {
	          // add a new line for each selection
	          message += i == 0 ? "" : "\n";
	          
	          Selection selection = selections.get(i);

	          if (selection.isCell()) {
	            // isCell() returns true if a cell has been selected.
	            
	            // getRow() returns the row number of the selected cell.
	            int row = selection.getRow();
	            // getColumn() returns the column number of the selected cell.
	            int column = selection.getColumn();
	            message += "cell " + row + ":" + column + " selected";
	          } else if (selection.isRow()) {
	            // isRow() returns true if an entire row has been selected.
	            
	            // getRow() returns the row number of the selected row.
	            int row = selection.getRow();
	            message += "row " + row + " selected";
	          } else {
	            // unreachable
	            message += "Pie chart selections should be either row selections or cell selections.";
	            message += "  Other visualizations support column selections as well.";
	          }
	        }
	        
	        Window.alert(message);
	      }
	    };
	  }

	  private AbstractDataTable createTable() {
	    DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Task");
	    data.addColumn(ColumnType.NUMBER, "Hours per Day");
	    data.addRows(2);
	    data.setValue(0, 0, "Work");
	    data.setValue(0, 1, 14);
	    data.setValue(1, 0, "Sleep");
	    data.setValue(1, 1, 10);
	    return data;
	  }

	@Override
	public Widget getWidget() {
		return this;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return R.get("Line Chart");
	}

	@Override
	public boolean isLoading() {
		// TODO Auto-generated method stub
		return false;
	}

}
