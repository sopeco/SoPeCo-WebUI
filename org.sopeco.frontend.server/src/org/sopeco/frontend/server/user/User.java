package org.sopeco.frontend.server.user;

import java.util.logging.Logger;

import org.sopeco.frontend.server.db.UIPersistenceProvider;
import org.sopeco.frontend.shared.builder.MeasurementSpecificationBuilder;
import org.sopeco.frontend.shared.builder.ScenarioDefinitionBuilder;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

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
	private static final Logger LOGGER = Logger.getLogger(User.class.getName());

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
		LOGGER.info("store current ScenarioDefinition");
		
		ScenarioDefinition test2 = currentScenarioDefinitionBuilder.getBuiltScenario();
		
		currentPersistenceProvider.store(test2);

		try {
			ScenarioDefinition test = currentPersistenceProvider.loadScenarioDefinition(test2.getScenarioName());

			System.out.println("");
		} catch (DataNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
