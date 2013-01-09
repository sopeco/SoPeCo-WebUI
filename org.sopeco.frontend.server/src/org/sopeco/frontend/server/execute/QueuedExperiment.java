package org.sopeco.frontend.server.execute;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.helper.EventLogLite;

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
	private List<StatusMessage> eventLogList;

	private ProgressInfo lastProgressInfo;

	public QueuedExperiment(ScheduledExperiment pScheduledExperiment) {
		scheduledExperiment = pScheduledExperiment;
		eventLogList = new ArrayList<StatusMessage>();
	}

	public ProgressInfo getLastProgressInfo() {
		return lastProgressInfo;
	}

	public void setLastProgressInfo(ProgressInfo pLastProgressInfo) {
		this.lastProgressInfo = pLastProgressInfo;
	}

	public List<StatusMessage> getEventLogList() {
		return eventLogList;
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

	//TODO
	public List<EventLogLite> getEventLogLiteList() {
		List<EventLogLite> list = new ArrayList<EventLogLite>();
		for (StatusMessage log : eventLogList) {

			EventLogLite logLite = new EventLogLite();
			logLite.setTime(log.getTime());
			if (log.getEventType() == EventType.EXECUTE_EXPERIMENTRUN && log.getStatusInfo() != null) {
				ProgressInfo info = (ProgressInfo) log.getStatusInfo();
				logLite.setMessage(getStatusString(log.getEventType()) + " " + info.getRepetition() + " of "
						+ info.getNumberOfRepetition());
			} else {
				logLite.setMessage(log.getEventType().toString());
			}
			logLite.setError(false);

			list.add(logLite);
		}
		return list;
	}

	// TODO
	public String getStatusString(EventType type) {
		switch (type) {
		case ACQUIRE_MEC:
			return "Acquiring MeasurementEnvironmentController";
		case ACQUIRE_MEC_FAILED:
			return "Acquiring MeasurementEnvironmentController";
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
		case ERROR:
			return "Error";
		default:
			throw new IllegalStateException();
		}
	}
}
