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
package org.sopeco.webui.server.execute;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.status.ErrorInfo;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.webui.server.persistence.entities.ScheduledExperiment;
import org.sopeco.webui.shared.helper.MECLogEntry;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class QueuedExperiment {

	private long timeStarted;
	private long timeEnded;
	private long timeQueued;

	private ScheduledExperiment scheduledExperiment;
	private List<StatusMessage> statusMessageList;

	private ProgressInfo lastProgressInfo;

	public QueuedExperiment(ScheduledExperiment pScheduledExperiment) {
		scheduledExperiment = pScheduledExperiment;
		statusMessageList = new ArrayList<StatusMessage>();
	}

	public ProgressInfo getLastProgressInfo() {
		return lastProgressInfo;
	}

	public void setLastProgressInfo(ProgressInfo pLastProgressInfo) {
		this.lastProgressInfo = pLastProgressInfo;
	}

	public List<StatusMessage> getStatusMessageList() {
		return statusMessageList;
	}

	public ScheduledExperiment getScheduledExperiment() {
		return scheduledExperiment;
	}

	public long getTimeEnded() {
		return timeEnded;
	}

	public void setTimeEnded(long pTimeEnded) {
		this.timeEnded = pTimeEnded;
	}

	public long getTimeQueued() {
		return timeQueued;
	}

	public void setTimeQueued(long pTimeQueued) {
		this.timeQueued = pTimeQueued;
	}

	public long getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(long pTimeStarted) {
		this.timeStarted = pTimeStarted;
	}

	// TODO
	public List<MECLogEntry> getEventLogLiteList() {
		List<MECLogEntry> list = new ArrayList<MECLogEntry>();
		for (StatusMessage log : statusMessageList) {

			MECLogEntry logLite = new MECLogEntry();
			logLite.setTime(log.getTimestamp());

			String message = getStatusString(log.getEventType());
			if (log.getDescription() != null && !log.getDescription().isEmpty()) {
				message += " - " + log.getDescription();
			}
			logLite.setMessage(message);

			if (log.getStatusInfo() != null && log.getStatusInfo() instanceof ErrorInfo) {
				logLite.setError(true);
				logLite.setErrorMessage(((ErrorInfo) log.getStatusInfo()).getThrowable().getMessage());

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				((ErrorInfo) log.getStatusInfo()).getThrowable().printStackTrace(pw);
				String errorStack = sw.toString().replaceAll("\n", "\n<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;");
				logLite.setErrorMessage(errorStack);
			} else {
				logLite.setError(false);
			}

			list.add(logLite);
		}
		return list;
	}

	// TODO
	public static String getStatusString(EventType type) {
		switch (type) {
		case ACQUIRE_MEC:
			return "Acquiring MeasurementEnvironmentController";
		case ACQUIRE_MEC_FAILED:
			return "Acquiring MeasurementEnvironmentController failed";
		case EXECUTE_EXPERIMENTRUN:
			return "Executing ExperimentRun";
		case FINALIZE_EXPERIMENTSERIES:
			return "Finalizing ExperimentSeries";
		case INIT_MEC:
			return "Initializing MeasurementEnvironmentController";
		case MEASUREMENT_FINISHED:
			return "Measurement finished";
		case PREPARE_EXPERIMENTSERIES:
			return "Prepare ExperimentRun";
		case RELEASE_MEC:
			return "Release MeasurementEnvironmentController";
		case EXECUTION_FAILED:
			return "Execution failed";
		case EXPERIMENT_EXECUTION:
			return "Experiment execution";
		case CONNECT_TO_MEC:
			return "Connect to MeasurementEnvironmentController";
		case ERROR:
			return "Error";
		case INFORMATION:
			return "Information";
		default:
			throw new IllegalStateException("No eventType " + type + " expected.");
		}
	}
}
