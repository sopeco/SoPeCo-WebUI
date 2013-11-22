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
package org.sopeco.webui.shared.entities;

import java.util.List;

import org.sopeco.webui.shared.helper.MECLogEntry;
import org.sopeco.webui.shared.push.PushSerializable;

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
	private long accountId;
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
	public long getAccount() {
		return accountId;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(long account) {
		this.accountId = account;
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
