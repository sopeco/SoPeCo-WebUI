package org.sopeco.frontend.server.rpc;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.frontend.client.rpc.ScenarioManagerRPC;
import org.sopeco.frontend.server.db.PersistenceProvider;
import org.sopeco.frontend.server.model.ScenarioDefinitionBuilder;
import org.sopeco.persistence.IPersistenceProvider;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Implementation of the ScenarioManagerRPC. The Frontend is getting all
 * scenario inforamtion from these calls.
 * 
 * @author Marius Oehler
 * 
 */
public class ScenarioManagerRPCImpl extends RemoteServiceServlet implements ScenarioManagerRPC {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioManagerRPCImpl.class);
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getScenarioNames() {
		HttpSession session = getThreadLocalRequest().getSession();
		IPersistenceProvider dbCon = PersistenceProvider.getPersistenceProvider(session);

		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return null;
		}

		try {
			List<ScenarioDefinition> scenarioList = dbCon.loadAllScenarioDefinitions();

			String[] retValues = new String[scenarioList.size()];

			for (int i = 0; i < scenarioList.size(); i++) {
				ScenarioDefinition sd = scenarioList.get(i);
				retValues[i] = sd.getScenarioName();
			}

			return retValues;
		} catch (DataNotFoundException e) {
			return null;
		}
	}

	@Override
	public boolean addScenario(String name) {

		name = name.replaceAll("[^a-zA-Z0-9_]", "_");

		ScenarioDefinition emptyScenario = ScenarioDefinitionBuilder.buildEmptyScenario(name);

		HttpSession session = getThreadLocalRequest().getSession();
		IPersistenceProvider dbCon = PersistenceProvider.getPersistenceProvider(session);

		if (dbCon == null) {
			LOGGER.warn("No database connection found.");
			return false;
		}

		dbCon.store(emptyScenario);

		return true;
	}

	@Override
	public boolean removeScenario(String name) {
		if (!name.matches("[a-zA-Z0-9_]+")) {
			return false;
		}

		HttpSession session = getThreadLocalRequest().getSession();
		IPersistenceProvider dbCon = PersistenceProvider.getPersistenceProvider(session);

		try {
			ScenarioDefinition definition = dbCon.loadScenarioDefinition(name);

			dbCon.remove(definition);

			return true;
		} catch (DataNotFoundException e) {
			LOGGER.warn("Scenario '{}' not found.", name);
			return false;
		}
	}

}
