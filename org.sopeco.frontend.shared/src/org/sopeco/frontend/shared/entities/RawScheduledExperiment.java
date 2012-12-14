package org.sopeco.frontend.shared.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.sopeco.persistence.entities.definition.ScenarioDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
@Entity
@SequenceGenerator(sequenceName = "SCHEDULE_SEQ", name = "SCHEDULE_SEQ_GEN")
public class RawScheduledExperiment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "account")
	private String account;

	@Column(name = "controllerUrl")
	private String controllerUrl;

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "isRepeating")
	private boolean isRepeating;

	@Column(name = "label")
	private String label;

	@Column(name = "repeatDays")
	private String repeatDays;

	@Column(name = "repeatHours")
	private String repeatHours;

	@Column(name = "repeatMinutes")
	private String repeatMinutes;

	@Lob
	@Column(name = "scenarioDefinition")
	private ScenarioDefinition scenarioDefinition;

	@Column(name = "startTime")
	private long startTime;

	public String getControllerUrl() {
		return controllerUrl;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
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
