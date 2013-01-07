package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class CurrentControllerExperiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long timeStart;
	private long timeRemaining;

	private String account;
	private String scenario;
	private float progress;

	private EStatus status;

	public enum EStatus {
		START_MEASUREMENT, ACQUIRE_MEC, ACQUIRE_MEC_FAILED, ERROR, EXECUTE_EXPERIMENTRUN, FINALIZE_EXPERIMENTSERIES, INIT_MEC, MEASUREMENT_FINISHED, PREPARE_EXPERIMENTSERIES, RELEASE_MEC
	}

	public String getStatusString() {
		switch (status) {
		case START_MEASUREMENT:
			return "Starting Measurement";
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
			return "-";
		}
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
	 * @return the status
	 */
	public EStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EStatus status) {
		this.status = status;
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

}
