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
package org.sopeco.webui.client.layout.center.execute.tabTwo;

import java.util.Date;
import java.util.logging.Logger;

import org.sopeco.gwt.widgets.ClearDiv;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
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
public class ScheduleItem extends FlowPanel implements ClickHandler, Comparable<ScheduleItem> {

	private static final String IMAGE_PAUSE = "images/control_pause.png";
	private static final String IMAGE_RUNNING = "images/control_play.png";

	private static final Logger LOGGER = Logger.getLogger(ScheduleItem.class.getName());

	private static final String DISABLED_CSS = "disabled";

	private TabControllerTwo tabControllerTwo;

	private FrontendScheduledExperiment experiment;
	private HTML htmlLabel;
	private Image imgPause;
	private Image imgRemove;
	private FlowPanel headPanel;
	private FlexTable contentTable;
	private HTML htmlLabelExecute;
	private HTML htmlLabelStartTime;
	private HTML htmlLabelRepeat;
	private HTML htmlLabelNextExec;
	private HTML htmlLabelLastExec;
	private HTML htmlLabelAdded;
	private HTML htmlNextExec;
	private HTML htmlLastExec;
	private HTML htmlAdded;
	private HTML htmlRepeat;
	private HTML htmlStartTime;
	private HTML htmlExperiments;
	private HTML htmlLabelLastDuration;
	private HTML htmlLastDuration;
	private Anchor anchorPerformNow;

	private static final String[] DAY_STRINGS = new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

	public ScheduleItem(FrontendScheduledExperiment pExperiment) {
		experiment = pExperiment;
		init();
	}

	private void init() {
		DateTimeFormat dtf = DateTimeFormat.getFormat("hh:mm aa / dd.MM.yyyy");

		headPanel = new FlowPanel();

		contentTable = new FlexTable();

		htmlLabel = new HTML(experiment.getLabel());
		imgPause = new Image(IMAGE_PAUSE);
		imgPause.addClickHandler(this);
		imgRemove = new Image("images/trash.png");
		imgRemove.addClickHandler(this);

		htmlLabelExecute = new HTML(R.lang.Experiments() + ":");
		htmlLabelStartTime = new HTML(R.lang.StartTime() + ":");
		htmlLabelRepeat = new HTML(R.lang.Repeat() + ":");
		htmlLabelAdded = new HTML(R.lang.Added() + ":");
		htmlLabelLastExec = new HTML(R.lang.LastExecution() + ":");
		htmlLabelNextExec = new HTML(R.lang.NextExecution() + ":");
		htmlLabelLastDuration = new HTML(R.lang.LastDuration() + ":");

		anchorPerformNow = new Anchor(R.lang.PerformNow());

		htmlExperiments = new HTML(getExperimentString());

		htmlStartTime = new HTML(dtf.format(new Date(experiment.getStartTime())));
		htmlAdded = new HTML(dtf.format(new Date(experiment.getAddTime())));

		if (experiment.isEnabled()) {
			htmlNextExec = new HTML(dtf.format(new Date(experiment.getNextExecutionTime())));
		} else {
			htmlNextExec = new HTML("paused");
		}

		if (experiment.getDurations() != null && !experiment.getDurations().isEmpty()) {
			htmlLastDuration = new HTML(getDurationString());
		} else {
			htmlLastDuration = new HTML("-");
		}

		String lastExec = "";
		if (experiment.getLastExecutionTime() == -1) {
			lastExec = R.lang.NeverExecuted();
		} else {
			lastExec = dtf.format(new Date(experiment.getLastExecutionTime()));
		}
		htmlLastExec = new HTML(lastExec);

		String repeatText = "";
		if (experiment.isRepeating()) {
			repeatText = getDay(experiment.getRepeatDays()) + " " + experiment.getRepeatHours() + " "
					+ experiment.getRepeatMinutes();
		} else {
			repeatText = R.lang.UniqueExecution();
		}
		htmlRepeat = new HTML(repeatText);

		addObjects();
		style();
	}

	private String getDurationString() {
		String string = "";
		long duration = experiment.getDurations().get(experiment.getDurations().size() - 1);
		duration /= 1000;
		long hours = duration / 3600;
		long min = duration / 60;
		long sec = duration % 60;
		if (hours < 10) {
			string += 0;
		}
		string += hours + ":";
		if (min < 10) {
			string += 0;
		}
		string += min + ":";
		if (sec < 10) {
			string += 0;
		}
		string += sec;
		return string;
	}

	private String getExperimentString() {
		StringBuffer buffer = new StringBuffer();
		for (MeasurementSpecification ms : experiment.getScenarioDefinition().getMeasurementSpecifications()) {
			for (ExperimentSeriesDefinition esd : ms.getExperimentSeriesDefinitions()) {
				buffer.append(esd.getName());
				buffer.append("<br>");
			}
		}
		return buffer.toString();
	}

	public void setTabControllerTwo(TabControllerTwo pTabControllerTwo) {
		this.tabControllerTwo = pTabControllerTwo;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == imgRemove) {
			tabControllerTwo.removeScheduledExperiment(this);
		} else if (event.getSource() == imgPause) {
			tabControllerTwo.setExperimentEnable(experiment, !experiment.isEnabled());
		}
	}

	public String getDay(String days) {
		String ret = "";
		for (String s : days.split(",")) {
			int i = Integer.parseInt(s.trim()) - 1;
			if (!ret.isEmpty()) {
				ret += ",";
			}
			ret += DAY_STRINGS[i];
		}
		return ret;
	}

	/**
	 * 
	 */
	private void style() {
		addStyleName("scheduleItemPanel");
		headPanel.addStyleName("headPanel");

		updateEnabled();
	}

	private void updateEnabled() {
		if (experiment.isEnabled()) {
			removeStyleName(DISABLED_CSS);
			imgPause.setUrl(IMAGE_PAUSE);
		} else {
			addStyleName(DISABLED_CSS);
			imgPause.setUrl(IMAGE_RUNNING);
		}
	}

	/**
	 * 
	 */
	private void addObjects() {
		headPanel.add(htmlLabel);
		headPanel.add(imgPause);
		headPanel.add(imgRemove);
		headPanel.add(anchorPerformNow);
		headPanel.add(new ClearDiv());

		contentTable.setWidget(0, 0, htmlLabelExecute);
		contentTable.setWidget(0, 1, htmlExperiments);

		contentTable.setWidget(0, 2, htmlLabelStartTime);
		contentTable.setWidget(1, 1, htmlLabelAdded);
		contentTable.setWidget(2, 1, htmlLabelRepeat);

		contentTable.setWidget(0, 3, htmlStartTime);
		contentTable.setWidget(1, 2, htmlAdded);
		contentTable.setWidget(2, 2, htmlRepeat);

		contentTable.setWidget(0, 4, htmlLabelNextExec);
		contentTable.setWidget(1, 3, htmlLabelLastExec);
		contentTable.setWidget(2, 3, htmlLabelLastDuration);

		contentTable.setWidget(0, 5, htmlNextExec);
		contentTable.setWidget(1, 4, htmlLastExec);
		contentTable.setWidget(2, 4, htmlLastDuration);

		contentTable.getFlexCellFormatter().setRowSpan(0, 1, 3);
		contentTable.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		add(headPanel);
		add(contentTable);
	}

	public FrontendScheduledExperiment getExperiment() {
		return experiment;
	}

	@Override
	public int compareTo(ScheduleItem other) {
		return (int) (experiment.getNextExecutionTime() - other.getExperiment().getNextExecutionTime());
	}
}
