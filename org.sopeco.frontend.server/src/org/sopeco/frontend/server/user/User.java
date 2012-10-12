package org.sopeco.frontend.server.user;

import org.sopeco.frontend.server.db.UIPersistenceProvider;
import org.sopeco.frontend.server.model.MeasurementSpecificationBuilder;
import org.sopeco.frontend.server.model.ScenarioDefinitionBuilder;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class User {

	private String sessionId;

	private ScenarioDefinitionBuilder currentScenarioDefinitionBuilder;

	private String workingSpecification;
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

	public String getWorkingSpecification() {
		return workingSpecification;
	}

	/**
	 * Set the current specification, which is in the builder as default/working
	 * spec.
	 * 
	 * @param workingSpecification
	 */
	public void setWorkingSpecification(String workingSpecification) {
		this.workingSpecification = workingSpecification;

		MeasurementSpecification specification = getCurrentScenarioDefinitionBuilder().getMeasurementSpecification(workingSpecification);
		MeasurementSpecificationBuilder specificationBuilder = new MeasurementSpecificationBuilder(
				specification);
		getCurrentScenarioDefinitionBuilder().setSpecificationBuilder(specificationBuilder);
	}

	public void storeCurrentScenarioDefinition() {
		currentPersistenceProvider.store(currentScenarioDefinitionBuilder.getBuiltScenario());
	}
}
