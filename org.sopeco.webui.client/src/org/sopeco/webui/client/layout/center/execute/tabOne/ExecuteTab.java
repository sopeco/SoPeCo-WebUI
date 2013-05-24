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
package org.sopeco.webui.client.layout.center.execute.tabOne;

import org.sopeco.gwt.widgets.EditableText;
import org.sopeco.gwt.widgets.Headline;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.resources.LanguageConstants;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteTab extends FlowPanel implements ValueChangeHandler<Boolean> {

	private static final String CONFIG_TABLE_CLASS = "executeConfigTable";

	private FlexTable configTable;
	private HTML htmlController, htmlLabel, htmlExecution, htmlRdioOnReady, htmlRdioSchedule;
	private Button btnExecute;
	private EditableText editController, editLabel;
	private RadioButton rdioOnReady, rdioSchedule;
	private HorizontalPanel hPanelExecution;

	private ScheduleConfigTable scheduleConfTable;
	private RepeatPanel repeatTable;
	private SelectionPanel selectionPanel;

	private LanguageConstants r = GWT.create(LanguageConstants.class);

	public ExecuteTab() {
		init();
	}

	private void init() {
		getElement().getStyle().setOverflowY(Overflow.AUTO);

		configTable = new FlexTable();
		configTable.addStyleName(CONFIG_TABLE_CLASS);

		htmlController = new HTML(R.lang.Controller() + ":");
		htmlLabel = new HTML(R.lang.Label() + ":");
		htmlExecution = new HTML(R.lang.Execution() + ":");
		htmlRdioOnReady = new HTML(R.lang.execOnReady());
		htmlRdioSchedule = new HTML(R.lang.execSchedule());

		editLabel = new EditableText("My Scheduling");
		btnExecute = new Button(R.lang.executeExperiment());

		editController = new EditableText(Manager.get().getControllerUrl());
		editController.setEditable(false);
		// Temporary
		editController.getElement().getFirstChildElement().getNextSiblingElement().getStyle().setBackgroundColor("white");
		editController.getElement().getFirstChildElement().getNextSiblingElement().getStyle().setBorderColor("white");

		rdioOnReady = new RadioButton("execution");
		rdioSchedule = new RadioButton("execution");
		// rdioSchedule.setEnabled(false);

		hPanelExecution = new HorizontalPanel();
		hPanelExecution.add(rdioOnReady);
		hPanelExecution.add(htmlRdioOnReady);
		hPanelExecution.add(rdioSchedule);
		hPanelExecution.add(htmlRdioSchedule);

		scheduleConfTable = new ScheduleConfigTable();
		repeatTable = new RepeatPanel(this);
		selectionPanel = new SelectionPanel();

		// #
		scheduleConfTable.setVisible(false);
		repeatTable.setVisible(false);

		scheduleConfTable.getCbRepeat().addValueChangeHandler(this);

		htmlRdioOnReady.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		htmlRdioSchedule.getElement().getStyle().setProperty("margin", "0 2em 0 0.5em");
		rdioOnReady.setValue(true);

		rdioOnReady.addValueChangeHandler(this);
		rdioSchedule.addValueChangeHandler(this);

		// #
		configTable.setWidget(0, 0, htmlLabel);
		configTable.setWidget(0, 1, editLabel);
		configTable.setWidget(1, 0, htmlController);
		configTable.setWidget(1, 1, editController);
		configTable.setWidget(2, 2, btnExecute);
		configTable.setWidget(2, 0, htmlExecution);
		configTable.setWidget(2, 1, hPanelExecution);

		configTable.getColumnFormatter().setWidth(0, "130px");

		configTable.getFlexCellFormatter().setColSpan(0, 1, 2);
		configTable.getFlexCellFormatter().setColSpan(1, 1, 2);
		configTable.getCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		add(new Headline("Execution Settings"));
		add(configTable);
		add(scheduleConfTable);
		add(repeatTable);
		add(selectionPanel);
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		if (event.getSource() == rdioOnReady || event.getSource() == rdioSchedule) {
			scheduleConfTable.setVisible(rdioSchedule.getValue());
			repeatTable.setVisible(rdioSchedule.getValue() && scheduleConfTable.getCbRepeat().getValue());
			if (rdioOnReady.getValue()) {
				btnExecute.setText(R.lang.executeExperiment());
			} else {
				btnExecute.setText(R.lang.scheduleExperiment());
			}
		} else if (event.getSource() == scheduleConfTable.getCbRepeat()) {
			repeatTable.setVisible(scheduleConfTable.getCbRepeat().getValue());
		}
	}

	public boolean isExecutingImmediately() {
		return rdioOnReady.getValue();
	}

	public void generateTree() {
		selectionPanel.generateTree();
	}

	public ScheduleConfigTable getScheduleConfTable() {
		return scheduleConfTable;
	}

	public RepeatPanel getRepeatTable() {
		return repeatTable;
	}

	public SelectionPanel getSelectionPanel() {
		return selectionPanel;
	}

	public Button getBtnExecute() {
		return btnExecute;
	}

	public EditableText getEditController() {
		return editController;
	}

	public EditableText getEditLabel() {
		return editLabel;
	}

}
