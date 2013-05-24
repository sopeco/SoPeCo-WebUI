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

import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.webui.shared.push.PushSerializable;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class FrontendScheduledExperiment implements PushSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long accountId;

	private String controllerUrl;

	private long id;

	private boolean isRepeating;

	private String label;

	private String repeatDays;

	private String repeatHours;

	private String repeatMinutes;

	private ScenarioDefinition scenarioDefinition;

	private long startTime;

	private long addTime;

	private long nextExecutionTime;

	private long lastExecutionTime;

	private boolean enabled;

	private List<Long> durations;

	private boolean isRunning;

	private List<String> selectedExperiments;

	public List<String> getSelectedExperiments() {
		return selectedExperiments;
	}

	public void setSelectedExperiments(List<String> filterMap) {
		this.selectedExperiments = filterMap;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean pIsRunning) {
		this.isRunning = pIsRunning;
	}

	public List<Long> getDurations() {
		return durations;
	}

	public void setDurations(List<Long> pDurations) {
		this.durations = pDurations;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean pEnabled) {
		this.enabled = pEnabled;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long pAddTime) {
		this.addTime = pAddTime;
	}

	public String getControllerUrl() {
		return controllerUrl;
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	public void setNextExecutionTime(long pNextExecutionTime) {
		this.nextExecutionTime = pNextExecutionTime;
	}

	public long getLastExecutionTime() {
		return lastExecutionTime;
	}

	public void setLastExecutionTime(long pLastExecutionTime) {
		this.lastExecutionTime = pLastExecutionTime;
	}

	public long getAccount() {
		return accountId;
	}

	public void setAccount(long pAccount) {
		this.accountId = pAccount;
	}

	public void setId(long pId) {
		this.id = pId;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getRepeatDays() {
		return repeatDays;
	}

	public String getRepeatHours() {
		return repeatHours;
	}

	public String getRepeatMinutes() {
		return repeatMinutes;
	}

	public ScenarioDefinition getScenarioDefinition() {
		return scenarioDefinition;
	}

	public long getStartTime() {
		return startTime;
	}

	public boolean isRepeating() {
		return isRepeating;
	}

	public void setControllerUrl(String pControllerUrl) {
		controllerUrl = pControllerUrl;
	}

	public void setLabel(String pLabel) {
		label = pLabel;
	}

	public void setRepeatDays(String pRepeatDays) {
		repeatDays = pRepeatDays;
	}

	public void setRepeatHours(String pRepeatHours) {
		repeatHours = pRepeatHours;
	}

	public void setRepeating(boolean pIsRepeating) {
		isRepeating = pIsRepeating;
	}

	public void setRepeatMinutes(String pRepeatMinutes) {
		repeatMinutes = pRepeatMinutes;
	}

	public void setScenarioDefinition(ScenarioDefinition pScenarioDefinition) {
		scenarioDefinition = pScenarioDefinition;
	}

	public void setStartTime(long pStartTime) {
		startTime = pStartTime;
	}

}
