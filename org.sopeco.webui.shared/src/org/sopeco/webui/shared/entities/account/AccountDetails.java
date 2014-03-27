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
package org.sopeco.webui.shared.entities.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.sopeco.webui.shared.entities.ScenarioDetails;

/**
 * 
 * @author Marius Oehler
 * @author Peter Merkert
 */
@Entity
@NamedQueries({ @NamedQuery(name = "getAllAccountDetails", query = "SELECT u FROM AccountDetails u") })
public class AccountDetails implements Serializable {

	private static final long serialVersionUID = -7307533980593091796L;

	@Id
	@Column(name = "id")
	private long id;

	@Column(name = "accountName")
	private String accountName;

	@Lob
	@Column(name = "scenarioDetails")
	private List<ScenarioDetails> scenarioDetails;

	@Column(name = "selectedScenario")
	private String selectedScenario;

	@Column(name = "csvValueSeparator")
	private String csvValueSeparator = ";";

	@Column(name = "csvCommentSeparator")
	private String csvCommentSeparator = "#";

	@Column(name = "csvDecimalDelimiter")
	private String csvDecimalDelimiter = ".";

	public AccountDetails() {
		scenarioDetails = new ArrayList<ScenarioDetails>();
	}

	/**
	 * Returns the controller URL of the active controller.
	 * 
	 * @return
	 */
	public String getControllerUrl() {
		if (selectedScenario == null || selectedScenario.isEmpty()) {
			return null;
		}

		String url = getScenarioDetail(selectedScenario).getControllerProtocol()
				+ getScenarioDetail(selectedScenario).getControllerHost();

		if (getScenarioDetail(selectedScenario).getControllerPort() > 0) {
			url += ":" + getScenarioDetail(selectedScenario).getControllerPort();
		}
		url += "/" + getScenarioDetail(selectedScenario).getControllerName();

		return url;
	}
	
	/**
	 * Sets the experiment key for the current selected scenario. The key is only set, when
	 * the Account has a selected scenario.
	 * 
	 * @param experimentKey	the experiment key
	 */
	public void setExperimentKey(int experimentKey) {
		
		if (selectedScenario == null || selectedScenario.isEmpty()) {
			return;
		}

		getScenarioDetail(selectedScenario).setExperimentKey(experimentKey);
	}
	
	/**
	 * Returns the experiment key of the current selected scenario.
	 * 
	 * @return the experiment key for the current selected scenario
	 */
	public int getExperimentKeyOfSelectedScenario() {
		
		for (ScenarioDetails detail : scenarioDetails) {
			if (detail.getScenarioName().equals(selectedScenario)) {
				return detail.getExperimentKey();
			}
		}

		return -1;
	}

	/**
	 * Creates a new ScenarioDetails object and adds that to the scenarioDetails
	 * List.
	 */
	public void addScenarioDetails(String scenarioName) {
		ScenarioDetails detail = new ScenarioDetails();
		detail.setScenarioName(scenarioName);

		scenarioDetails.add(detail);
	}

	/**
	 * @return the selectedScenario
	 */
	public String getSelectedScenario() {
		return selectedScenario;
	}

	/**
	 * @param pSelectedScenario
	 *            the selectedScenario to set
	 */
	public void setSelectedScenario(String pSelectedScenario) {
		this.selectedScenario = pSelectedScenario;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param pId
	 *            the id to set
	 */
	public void setId(long pId) {
		this.id = pId;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param pAccountName
	 *            the accountName to set
	 */
	public void setAccountName(String pAccountName) {
		this.accountName = pAccountName;
	}

	/**
	 * @return the scenarioDetails
	 */
	public List<ScenarioDetails> getScenarioDetails() {
		return scenarioDetails;
	}

	/**
	 * @param pScenarioDetails
	 *            the scenarioDetails to set
	 */
	public void setScenarioDetails(List<ScenarioDetails> pScenarioDetails) {
		this.scenarioDetails = pScenarioDetails;
	}

	/**
	 * @return the csvValueSeparator
	 */
	public String getCsvValueSeparator() {
		return csvValueSeparator;
	}

	/**
	 * @param pCsvValueSeparator
	 *            the csvValueSeparator to set
	 */
	public void setCsvValueSeparator(String pCsvValueSeparator) {
		this.csvValueSeparator = pCsvValueSeparator;
	}

	/**
	 * @return the csvCommentSeparator
	 */
	public String getCsvCommentSeparator() {
		return csvCommentSeparator;
	}

	/**
	 * @return the csvCommentSeparator
	 * @deprecated use {@link #getCsvCommentSeparator()}
	 */
	@Deprecated
	public String getCsvQuoteChar() {
		return csvCommentSeparator;
	}

	/**
	 * @param pCsvCommentSeparator
	 *            the csvCommentSeparator to set
	 */
	public void setCsvCommentSeparator(String pCsvCommentSeparator) {
		this.csvCommentSeparator = pCsvCommentSeparator;
	}

	/**
	 * @return the csvDecimalDelimiter
	 */
	public String getCsvDecimalDelimiter() {
		return csvDecimalDelimiter;
	}

	/**
	 * @param pCsvDecimalDelimiter
	 *            the csvDecimalDelimiter to set
	 */
	public void setCsvDecimalDelimiter(String pCsvDecimalDelimiter) {
		this.csvDecimalDelimiter = pCsvDecimalDelimiter;
	}

	/**
	 * Returns the object that has the specified name, if it exists in the list.
	 * 
	 * @param scenarioName
	 * @return
	 */
	public ScenarioDetails getScenarioDetail(String scenarioName) {
		if (scenarioName == null) {
			return null;
		}
		for (ScenarioDetails detail : scenarioDetails) {
			if (detail.getScenarioName().equals(scenarioName)) {
				return detail;
			}
		}
		return null;
	}

	/**
	 * Returns an array with all names of the existing scenarios.
	 * 
	 * @return
	 */
	public String[] getScenarioNames() {
		String[] names = new String[scenarioDetails.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = scenarioDetails.get(i).getScenarioName();
		}
		return names;
	}
	
	@Override
	public String toString() {
		return "====      AccountDetails     ====" + "\n"
				+ "ID: " + id  + "\n"
				+ "Name: " + accountName  + "\n"
				+ "SelectedScenario: " + selectedScenario  + "\n"
				+ "#ScenarioDetails: " + scenarioDetails.size();
	}
}
