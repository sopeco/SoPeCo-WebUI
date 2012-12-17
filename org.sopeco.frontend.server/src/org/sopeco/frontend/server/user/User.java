package org.sopeco.frontend.server.user;

import java.util.logging.Logger;

import org.sopeco.frontend.shared.builder.MeasurementSpecificationBuilder;
import org.sopeco.frontend.shared.builder.ScenarioDefinitionBuilder;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class User {

	private String sessionId;

	private ScenarioDefinitionBuilder currentScenarioDefinitionBuilder;

	private String workingSpecification;
	private DatabaseInstance currentAccount;
	private IPersistenceProvider currentPersistenceProvider;
	private static final Logger LOGGER = Logger.getLogger(User.class.getName());
	private long lastRequestTime;

	public User(String sId) {
		sessionId = sId;

		currentScenarioDefinitionBuilder = new ScenarioDefinitionBuilder();
	}

	public IPersistenceProvider getCurrentPersistenceProvider() {
		return currentPersistenceProvider;
	}

	public void setCurrentPersistenceProvider(IPersistenceProvider persistenceProvider) {
		this.currentPersistenceProvider = persistenceProvider;
	}

	public long getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(long pLastRequestTime) {
		this.lastRequestTime = pLastRequestTime;
	}

	public String getCurrentAccountId() {
		return currentAccount.getId();
	}

	public void setCurrentAccount(DatabaseInstance currentAccount) {
		this.currentAccount = currentAccount;
	}

	public DatabaseInstance getCurrentAccount() {
		return currentAccount;
	}

	public String getSessionId() {
		return sessionId;
	}

	public ScenarioDefinitionBuilder getCurrentScenarioDefinitionBuilder() {
		return currentScenarioDefinitionBuilder;
	}

	public void setCurrentScenarioDefinitionBuilder(ScenarioDefinitionBuilder scenarioDefinitionBuilder) {
		this.currentScenarioDefinitionBuilder = scenarioDefinitionBuilder;
	}

	// *******************************************************************************************************

	public void kill() {
		// TODO - close connections..
	}

	public String getWorkingSpecification() {
		return workingSpecification;
	}

	/**
	 * Set the current specification, which is in the builder as default/working
	 * spec.
	 * 
	 * @param pWorkingSpecification
	 */
	public void setWorkingSpecification(String pWorkingSpecification) {
		this.workingSpecification = pWorkingSpecification;

		MeasurementSpecification specification = getCurrentScenarioDefinitionBuilder().getMeasurementSpecification(
				pWorkingSpecification);
		MeasurementSpecificationBuilder specificationBuilder = new MeasurementSpecificationBuilder(specification);
		getCurrentScenarioDefinitionBuilder().setSpecificationBuilder(specificationBuilder);
	}

	/**
	 * 
	 */
	public void storeCurrentScenarioDefinition() {
		LOGGER.info("store current ScenarioDefinition");

		ScenarioDefinition scenarioDef = currentScenarioDefinitionBuilder.getBuiltScenario();

		currentPersistenceProvider.store(scenarioDef);
	}
}
