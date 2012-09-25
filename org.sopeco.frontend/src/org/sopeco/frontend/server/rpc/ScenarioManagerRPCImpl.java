package org.sopeco.frontend.server.rpc;

import java.util.List;

import org.sopeco.frontend.client.rpc.ScenarioManagerRPC;
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

	private static final long serialVersionUID = 1L;

	@Override
	public String[] getScenarioNames() {
		IPersistenceProvider dbCon = (IPersistenceProvider) getThreadLocalRequest().getSession().getAttribute(
				SessionAttribute.DatabaseConnection.name());

		if (dbCon == null) {
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

		IPersistenceProvider dbCon = (IPersistenceProvider) getThreadLocalRequest().getSession().getAttribute(
				SessionAttribute.DatabaseConnection.name());

		if (dbCon == null) {
			return false;
		}
		
		dbCon.store(emptyScenario);
		
		return true;
	}

}
