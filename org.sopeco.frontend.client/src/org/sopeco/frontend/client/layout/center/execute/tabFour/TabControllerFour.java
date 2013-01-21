package org.sopeco.frontend.client.layout.center.execute.tabFour;

import java.util.Date;
import java.util.List;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.TabController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.resources.FrontEndImages;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.ExecutedExperimentDetails;
import org.sopeco.frontend.shared.helper.MECLogEntry;

import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerFour extends TabController {

	private TabView tabView;
	private DateTimeFormat dtf = DateTimeFormat.getFormat("dd.MM.yyyy hh:mm aa");

	public TabControllerFour(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	private void initialize() {
		tabView = new TabView();

		buildTable();
	}

	private void buildTable() {
		if (Manager.get().getCurrentScenarioDetails() == null) {
			return;
		}

		DataGrid<ExecutedExperimentDetails> dataGrid = new DataGrid<ExecutedExperimentDetails>();

		TextColumn<ExecutedExperimentDetails> nameColumn = new TextColumn<ExecutedExperimentDetails>() {
			@Override
			public String getValue(ExecutedExperimentDetails object) {
				return object.getName();
			}
		};

		TextColumn<ExecutedExperimentDetails> timeStartColumn = new TextColumn<ExecutedExperimentDetails>() {
			@Override
			public String getValue(ExecutedExperimentDetails object) {
				return dtf.format(new Date(object.getTimeStarted()));
			}
		};

		TextColumn<ExecutedExperimentDetails> timeEndColumn = new TextColumn<ExecutedExperimentDetails>() {
			@Override
			public String getValue(ExecutedExperimentDetails object) {
				return dtf.format(new Date(object.getTimeFinished()));
			}
		};

		TextColumn<ExecutedExperimentDetails> controllerColumn = new TextColumn<ExecutedExperimentDetails>() {
			@Override
			public String getValue(ExecutedExperimentDetails object) {
				return object.getControllerURL();
			}
		};

		Column<ExecutedExperimentDetails, ImageResource> imageColumn = new Column<ExecutedExperimentDetails, ImageResource>(
				new ImageResourceCell()) {
			@Override
			public ImageResource getValue(ExecutedExperimentDetails object) {
				if (object.isSuccessful()) {
					return FrontEndImages.images().success();
				} else {
					return FrontEndImages.images().error();
				}
			}
		};

		imageColumn.setCellStyleNames("noBorder");
		nameColumn.setCellStyleNames("noBorder");
		timeStartColumn.setCellStyleNames("noBorder");
		timeEndColumn.setCellStyleNames("noBorder");
		controllerColumn.setCellStyleNames("noBorder");

		dataGrid.addColumn(imageColumn);
		dataGrid.addColumn(nameColumn, "Label");
		dataGrid.addColumn(timeStartColumn, "Started");
		dataGrid.addColumn(timeEndColumn, "Finished");
		dataGrid.addColumn(controllerColumn, "Controller");

		dataGrid.setColumnWidth(imageColumn, "40px");

		final SingleSelectionModel<ExecutedExperimentDetails> selectionModel = new SingleSelectionModel<ExecutedExperimentDetails>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				ExecutedExperimentDetails selectedExperiment = selectionModel.getSelectedObject();
				if (selectedExperiment != null) {
					setDetailLog(selectedExperiment.getEventLog());
				}
			}
		});

		tabView.setDataGrid(dataGrid);

		updateGrid(dataGrid);
	}

	public void updateGrid(final DataGrid<ExecutedExperimentDetails> dataGrid) {
		if (dataGrid == null) {
			return;
		}
		// List<ExecutedExperimentDetails> details =
		// Manager.get().getCurrentScenarioDetails()
		// .getExecutedExperimentsHistory();
		// Collections.reverse(new
		// ArrayList<ExecutedExperimentDetails>(details));

		RPC.getExecuteRPC().getExecutedExperimentDetails(new AsyncCallback<List<ExecutedExperimentDetails>>() {
			@Override
			public void onSuccess(List<ExecutedExperimentDetails> result) {
				dataGrid.setRowCount(result.size(), true);
				dataGrid.setRowData(0, result);
				dataGrid.redraw();
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getLocalizedMessage());
			}
		});
	}

	@Override
	public Panel getView() {
		return tabView;
	}

	@Override
	public void onSelection() {
		updateGrid(tabView.getDataGrid());
	}

	private void setDetailLog(List<MECLogEntry> logList) {
		tabView.getDetailPanel().clear();

		for (MECLogEntry log : logList) {
			HTML html = new HTML(dtf.format(new Date(log.getTime())) + ": " + log.getMessage());
			if (log.isError()) {
				html.addStyleName("errorMessage");
				html.setHTML("<b>" + html.getHTML() + "</b><br>" + log.getErrorMessage());
			}
			tabView.getDetailPanel().add(html);
		}
	}

}
