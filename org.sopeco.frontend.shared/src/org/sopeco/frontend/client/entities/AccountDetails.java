package org.sopeco.frontend.client.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
public class AccountDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;

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
	public String getId() {
		return id;
	}

	/**
	 * @param pId
	 *            the id to set
	 */
	public void setId(String pId) {
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
}
