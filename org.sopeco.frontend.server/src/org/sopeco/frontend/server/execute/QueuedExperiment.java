package org.sopeco.frontend.server.execute;

import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;

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

	public QueuedExperiment(ScheduledExperiment pScheduledExperiment) {
		scheduledExperiment = pScheduledExperiment;
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

}
