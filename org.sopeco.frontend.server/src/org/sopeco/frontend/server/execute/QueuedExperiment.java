package org.sopeco.frontend.server.execute;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.engine.status.ErrorInfo;
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.shared.helper.MECLogEntry;

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
			// if (log.getEventType() == EventType.EXECUTE_EXPERIMENTRUN &&
			// log.getStatusInfo() != null) {
			// ProgressInfo info = (ProgressInfo) log.getStatusInfo();
			// logLite.setMessage(getStatusString(log.getEventType()) + " " +
			// info.getRepetition() + " of "
			// + info.getNumberOfRepetition());
			// } else {
			// logLite.setMessage(log.getEventType().toString());
			// }
			String message = getStatusString(log.getEventType());
			if (log.getDescription() != null && !log.getDescription().isEmpty()) {
				message += " - " + log.getDescription();
			}
			logLite.setMessage(message);

			if (log.getStatusInfo() != null && log.getStatusInfo() instanceof ErrorInfo) {
				logLite.setError(true);
				logLite.setErrorMessage(((ErrorInfo) log.getStatusInfo()).getThrowable().getMessage());
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
		case EXECUTION_FAILED:
			return "Execution failed";
		case EXPERIMENT_EXECUTION:
			return "Experiment execution";
		case CONNECT_TO_MEC:
			return "Connect to MeasurementEnvironmentController";
		case ERROR:
			return "Error";
		default:
			throw new IllegalStateException("No eventType " + type + " expected.");
		}
	}
}
