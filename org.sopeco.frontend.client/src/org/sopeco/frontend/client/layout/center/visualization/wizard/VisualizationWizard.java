package org.sopeco.frontend.client.layout.center.visualization.wizard;

import java.util.Arrays;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.definitions.result.SharedExperimentRuns;
import org.sopeco.frontend.shared.entities.ChartOptions;
import org.sopeco.frontend.shared.entities.ChartParameter;
import org.sopeco.frontend.shared.entities.Visualization;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VisualizationWizard extends DialogBox {
	private static final String LOADING_INDICATOR = "images/loading_indicator.gif";
	
	private VerticalPanel rootWidget;
	private Button next;
	private Screen currentScreen = Screen.CHART_SELECTION;
	private int maxColumns;
	private SharedExperimentRuns experimentRun;
	private HorizontalPanel buttonPanel;
	private ChartParameter[] chartParameter;
	private Image loadingIndicator;
	private final ChartSelectionPanel chartSelectionPanel = new ChartSelectionPanel();
	final ColumnSelector columnSelector = new ColumnSelector();

	public VisualizationWizard() {
		FrontEndResources.loadVisualizationWizardCSS();
	}

	public VisualizationWizard(final SharedExperimentRuns experimentRun) {
		this.experimentRun = experimentRun;
		FrontEndResources.loadVisualizationWizardCSS();
		rootWidget = new VerticalPanel();
		loadingIndicator = new Image(LOADING_INDICATOR);
		buttonPanel = new HorizontalPanel();
		Button close = new Button(R.get("close"));
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				VisualizationWizard.this.hide();
			}
		});
		buttonPanel.add(close);
		next = new Button(R.get("next"));
		next.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				nextScreen();
			}
		});
		buttonPanel.add(next);
		this.setWidget(rootWidget);
		RPC.getVisualizationRPC().getChartParameter(experimentRun, new AsyncCallback<ChartParameter[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to receive chart parameter");
			}

			@Override
			public void onSuccess(ChartParameter[] result) {
				chartParameter = result;
			}
		});
	}

	public void setScreen(Screen screen) {
		rootWidget.clear();
		currentScreen = screen;
		switch(screen){
		case CHART_SELECTION:
			rootWidget.add(chartSelectionPanel);
			break;
		case COLUMN_SELECTION:
			columnSelector.setChartParameter(chartParameter);
			columnSelector.setMaxColumns(maxColumns);
			columnSelector.showColumnSelection();
			rootWidget.add(columnSelector);
			break;
		case LOADING_COLUMNS:
			rootWidget.add(loadingIndicator);
			break;
		}
		rootWidget.add(buttonPanel);
		rootWidget.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		this.center();
	}

	private void nextScreen() {
		switch (currentScreen) {
		case CHART_SELECTION:
			if (chartParameter == null){
				setScreen(Screen.LOADING_COLUMNS);
			}
			else {
				showColumnSelector();
			}
			break;
		case COLUMN_SELECTION:
			VisualizationWizard.this.hide();
			ChartOptions options = new ChartOptions();
			options.setType(chartSelectionPanel.getSelectedType());
			for (ChartParameter p : columnSelector.getSelectedColumns()){
				System.out.println("cparam: " + p.getParameterName());
			}
			RPC.getVisualizationRPC().getChart(experimentRun, columnSelector.getSelectedColumns(), options, new AsyncCallback<Visualization>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Visualization result) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case LOADING_COLUMNS:
			if (chartParameter != null){
				showColumnSelector();
			}
			break;
		}
	}
	
	private void showColumnSelector(){
		maxColumns = 2;
		setScreen(Screen.COLUMN_SELECTION);
		next.setText(R.get("finish"));
	}

	@Override
	public void show() {
		super.show();
		setScreen(Screen.CHART_SELECTION);
		next.setText(R.get("next"));
	}

	private enum Screen {
		CHART_SELECTION, COLUMN_SELECTION, LOADING_COLUMNS;
	}
}
