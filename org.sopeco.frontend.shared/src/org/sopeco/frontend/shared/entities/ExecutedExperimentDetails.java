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
