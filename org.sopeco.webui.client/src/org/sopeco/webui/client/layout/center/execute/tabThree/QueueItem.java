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
package org.sopeco.webui.client.layout.center.execute.tabThree;

import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class QueueItem extends FlowPanel {

	private static final String CONTROLLER_QUEUE_ITEM_CSS = "controllerQueueItem";

	private Image imgRepeat;

	private HTML htmlLabel, labelAccount, labelScenario, labelExperiments;
	private HTML valueAccount, valueScenario, valueExperiments;

	private FlexTable detailsTable;

	private FrontendScheduledExperiment experiment;

	/**
	 * Constructor.
	 */
	public QueueItem(FrontendScheduledExperiment pExperiment) {
		experiment = pExperiment;
		init();
	}

	/**
	 * Initialize all objects.
	 */
	private void init() {
		addStyleName(CONTROLLER_QUEUE_ITEM_CSS);

		htmlLabel = new HTML("<b>" + experiment.getLabel() + "</b>");
		labelAccount = new HTML(R.get("Account") + ":");
		labelScenario = new HTML(R.get("Scenario") + ":");
		labelExperiments = new HTML(R.get("Experiments") + ":");

		valueAccount = new HTML(experiment.getAccount());
		valueScenario = new HTML(experiment.getScenarioDefinition().getScenarioName());
		valueExperiments = new HTML("n/a");

		// imgRepeat = new Image("images/repeat.png");
		// imgRepeat.setWidth("21px");
		// imgRepeat.setHeight("14px");
		// imgRepeat.setTitle(R.get("ExperimentIsRepeat"));

		initTable();

		add(htmlLabel);
		add(detailsTable);
	}

	/**
	 * 
	 */
	private void initTable() {
		detailsTable = new FlexTable();

		detailsTable.setWidget(0, 0, labelAccount);
		detailsTable.setWidget(1, 0, labelScenario);

		detailsTable.setWidget(0, 1, valueAccount);
		detailsTable.setWidget(1, 1, valueScenario);

		detailsTable.setWidget(0, 2, labelExperiments);
		detailsTable.setWidget(0, 3, valueExperiments);

		detailsTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
		detailsTable.getFlexCellFormatter().setRowSpan(0, 3, 2);

		String width = "1px";
		detailsTable.getColumnFormatter().setWidth(0, width);
		detailsTable.getColumnFormatter().setWidth(2, width);

		width = "50%";
		detailsTable.getColumnFormatter().setWidth(1, width);
		detailsTable.getColumnFormatter().setWidth(3, width);

		detailsTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		detailsTable.getCellFormatter().setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_TOP);
	}
}
