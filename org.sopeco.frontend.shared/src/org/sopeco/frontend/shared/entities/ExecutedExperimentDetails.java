package org.sopeco.frontend.shared.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.sopeco.frontend.shared.helper.MECLogEntry;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
@NamedQueries({ @NamedQuery(name = "getExperiments", query = "SELECT s FROM ExecutedExperimentDetails s WHERE s.accountId = :accountId AND s.scenarioName = :scenarioName") })
public class ExecutedExperimentDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "accountId")
	private String accountId;

	@Column(name = "scenarioName")
	private String scenarioName;

	@Column(name = "timeStarted")
	private long timeStarted;

	@Column(name = "timeFinished")
	private long timeFinished;

	@Lob
	@Column(name = "eventLog")
	private List<MECLogEntry> eventLog;

	@Column(name = "successful")
	private boolean successful;

	@Column(name = "name")
	private String name;

	@Column(name = "controllerURL")
	private String controllerURL;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getControllerURL() {
		return controllerURL;
	}

	public void setControllerURL(String pControllerURL) {
		this.controllerURL = pControllerURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String pName) {
		this.name = pName;
	}

	public long getTimeStarted() {
		return timeStarted;
	}

	public void setTimeStarted(long pTimeStarted) {
		this.timeStarted = pTimeStarted;
	}

	public long getTimeFinished() {
		return timeFinished;
	}

	public void setTimeFinished(long pTimeFinished) {
		this.timeFinished = pTimeFinished;
	}

	public List<MECLogEntry> getEventLog() {
		return eventLog;
	}

	public void setEventLog(List<MECLogEntry> pEventLog) {
		this.eventLog = pEventLog;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean pSuccessful) {
		this.successful = pSuccessful;
	}

}
