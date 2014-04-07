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
package org.sopeco.webui.client.layout.center.execute.tabFour;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.sopeco.service.execute.MECLogEntry;
import org.sopeco.service.persistence.entities.ExecutedExperimentDetails;
import org.sopeco.service.persistence.entities.MECLog;
import org.sopeco.webui.client.layout.center.execute.ExecuteController;
import org.sopeco.webui.client.layout.center.execute.TabController;
import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
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

		Column<ExecutedExperimentDetails, Image> imageColumn = new Column<ExecutedExperimentDetails, Image>(
				new MyImageCell()) {
			@Override
			public Image getValue(ExecutedExperimentDetails object) {
				if (object.isSuccessful()) {
					return new Image(R.img.iconSet().getSafeUri(), 60, 120, 16, 13);
				} else {
					return new Image(R.img.iconSet().getSafeUri(), 30, 120, 13, 13);
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

		dataGrid.setColumnWidth(timeStartColumn, "180px");
		dataGrid.setColumnWidth(timeEndColumn, "180px");

		final SingleSelectionModel<ExecutedExperimentDetails> selectionModel = new SingleSelectionModel<ExecutedExperimentDetails>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				ExecutedExperimentDetails selectedExperiment = selectionModel.getSelectedObject();
				if (selectedExperiment != null) {
					setDetailLog(selectedExperiment.getId());
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

		RPC.getExecuteRPC().getExecutedExperimentDetails(new AsyncCallback<List<ExecutedExperimentDetails>>() {
			@Override
			public void onSuccess(List<ExecutedExperimentDetails> result) {
				List<ExecutedExperimentDetails> reverse = new ArrayList<ExecutedExperimentDetails>();

				for (ListIterator<ExecutedExperimentDetails> iterator = result.listIterator(result.size()); iterator
						.hasPrevious();) {
					reverse.add(iterator.previous());
				}

				dataGrid.setRowCount(reverse.size(), true);
				dataGrid.setRowData(0, reverse);
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
		if (tabView.getDataGrid() == null) {
			buildTable();
		} else {
			updateGrid(tabView.getDataGrid());
		}
	}

	private void setDetailLog(long logList) {
		RPC.getExecuteRPC().getMECLog(logList, new AsyncCallback<MECLog>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO
				Message.error(caught.getLocalizedMessage());
			}

			public void onSuccess(MECLog result) {
				if (result == null) {
					return;
				}
				tabView.getDetailPanel().clear();

				for (MECLogEntry log : result.getEntries()) {
					HTML html = new HTML(dtf.format(new Date(log.getTime())) + ": " + log.getMessage());
					if (log.isException()) {
						html.addStyleName("errorMessage");
						html.setHTML("<b>" + html.getHTML() + "</b><br>" + log.getErrorMessage());
					} else if (log.isError()) {
						html.addStyleName("errorMessage");
					}
					tabView.getDetailPanel().add(html);
				}
			};
		});
	}

}
