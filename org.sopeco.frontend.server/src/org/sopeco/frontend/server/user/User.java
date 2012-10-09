package org.sopeco.frontend.server.user;

import org.sopeco.frontend.server.db.UIPersistenceProvider;
import org.sopeco.frontend.server.model.ScenarioDefinitionBuilder;
import org.sopeco.persistence.IPersistenceProvider;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class User {

	private String sessionId;

	private ScenarioDefinitionBuilder currentScenarioDefinitionBuilder;

	private String currentDatabaseId;
	private IPersistenceProvider currentPersistenceProvider;
	private UIPersistenceProvider uiPesistenceProvider;

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

	public UIPersistenceProvider getUiPesistenceProvider() {
		return uiPesistenceProvider;
	}

	public void setUiPesistenceProvider(UIPersistenceProvider pesistenceProvider) {
		this.uiPesistenceProvider = pesistenceProvider;
	}

	public String getCurrentDatabaseId() {
		return currentDatabaseId;
	}

	public void setCurrentDatabaseId(String databaseId) {
		this.currentDatabaseId = databaseId;
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
	
	public void storeCurrentScenarioDefinition() {
		currentPersistenceProvider.store(currentScenarioDefinitionBuilder.getBuiltScenario());
	}
}
