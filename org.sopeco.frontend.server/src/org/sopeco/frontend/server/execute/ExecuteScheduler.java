package org.sopeco.frontend.server.execute;

import java.util.List;

import org.sopeco.frontend.server.persistence.UiPersistenceProviderFactory;
import org.sopeco.frontend.shared.entities.AccountDetails;
import org.sopeco.frontend.shared.entities.ScenarioDetails;
import org.sopeco.frontend.shared.entities.ScheduledExperiment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ExecuteScheduler {

	private static ExecuteScheduler executeScheduler;

	private ExecuteScheduler() {
	}

	public static ExecuteScheduler get() {
		if (executeScheduler == null) {
			executeScheduler = new ExecuteScheduler();
		}
		return executeScheduler;
	}

	public void check() {
		List<AccountDetails> accountList = UiPersistenceProviderFactory.createUiPersistenceProvider()
				.loadAllAccountDetails();

		for (AccountDetails account : accountList) {
			for (ScenarioDetails scenario : account.getScenarioDetails()) {
				for (ScheduledExperiment experiment : scenario.getScheduledExperimentsList()) {
					
				}
			}
		}
	}
}
