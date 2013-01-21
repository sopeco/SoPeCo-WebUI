package org.sopeco.frontend.shared.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@NamedQueries({ @NamedQuery(name = "getLogs", query = "SELECT s FROM MECLog s WHERE s.accountId = :accountId AND s.scenarioName = :scenarioName") })
public class MECLog {

	@Column(name = "accountId")
	private String accountId;

	@Column(name = "scenarioName")
	private String scenarioName;

	@Lob
	@Column(name = "entries")
	private List<MECLogEntry> entries;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String pAccountId) {
		this.accountId = pAccountId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String pScenarioName) {
		this.scenarioName = pScenarioName;
	}

	public List<MECLogEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<MECLogEntry> pEntries) {
		this.entries = pEntries;
	}

}
