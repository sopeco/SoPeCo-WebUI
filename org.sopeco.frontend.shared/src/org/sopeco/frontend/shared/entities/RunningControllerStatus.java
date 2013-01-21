package org.sopeco.frontend.shared.entities;

import java.util.List;

import org.sopeco.frontend.shared.helper.MECLogEntry;
import org.sopeco.frontend.shared.push.PushSerializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class RunningControllerStatus implements PushSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long timeStart;
	private long timeRemaining;

	private String label;
	private String account;
	private String scenario;
	private float progress;
	
	private List<MECLogEntry> eventLogList;

	private boolean hasFinished;

	/**
	 * @return the eventLogList
	 */
	public List<MECLogEntry> getEventLogList() {
		return eventLogList;
	}

	/**
	 * @param eventLogList
	 *            the eventLogList to set
	 */
	public void setEventLogList(List<MECLogEntry> eventLogList) {
		this.eventLogList = eventLogList;
	}

	
	
	public boolean isFinished() {
		return hasFinished;
	}

	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	/**
	 * @return the timeStart
	 */
	public long getTimeStart() {
		return timeStart;
	}

	/**
	 * @param timeStart
	 *            the timeStart to set
	 */
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}

	/**
	 * @return the timeRemaining
	 */
	public long getTimeRemaining() {
		return timeRemaining;
	}

	/**
	 * @param timeRemaining
	 *            the timeRemaining to set
	 */
	public void setTimeRemaining(long timeRemaining) {
		this.timeRemaining = timeRemaining;
	}


	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the scenario
	 */
	public String getScenario() {
		return scenario;
	}

	/**
	 * @param scenario
	 *            the scenario to set
	 */
	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	/**
	 * @return the progress
	 */
	public float getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(float progress) {
		this.progress = progress;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

}
