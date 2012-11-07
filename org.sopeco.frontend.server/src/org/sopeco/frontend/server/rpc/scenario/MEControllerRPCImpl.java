package org.sopeco.frontend.server.rpc.scenario;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rmi.RmiMEConnector;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.server.db.entities.MEControllerUrl;
import org.sopeco.frontend.server.helper.ServerCheck;
import org.sopeco.frontend.server.rpc.PushRPCImpl;
import org.sopeco.frontend.server.rpc.SuperRemoteServlet;
import org.sopeco.frontend.server.user.User;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.builder.MeasurementEnvironmentBuilder;
import org.sopeco.frontend.shared.builder.ScenarioDefinitionBuilder;
import org.sopeco.frontend.shared.definitions.PushPackage;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;
import org.sopeco.persistence.entities.definition.ParameterRole;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerRPCImpl extends SuperRemoteServlet implements
		MEControllerRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MEControllerRPCImpl.class);

	private static final String[] CONTROLLER_URL_PATTERN = new String[] { "^rmi://[a-zA-Z0-9\\.]+(:[0-9]{1,5})?/[a-zA-Z][a-zA-Z0-9]*$" };

	@Override
	public List<String> getMEControllerList() {
		try {
			List<MEControllerUrl> controllerList = getUser().getUiPesistenceProvider().loadAllMEControllerUrls();

			List<String> retList = new ArrayList<String>();

			for (MEControllerUrl cUrl : controllerList) {
				retList.add(cUrl.getUrl());
			}

			return retList;
		} catch (DataNotFoundException e) {
			return new ArrayList<String>();
		}
	}

	@Override
	public int checkControllerStatus(String url) {
		if (!checkUrlIsValid(url) || url == null) {
			LOGGER.debug("Controller-Status: NO_VALID_MEC_URL");
			return MEControllerRPC.NO_VALID_MEC_URL;
		}

		if (!getUser().getUiPesistenceProvider().checkMEControllerUrlExists(url)) {

			getUser().getUiPesistenceProvider().storeMEControllerUrl(url);

			// pushNewMECToClients(url);
		}

		try {
			IMeasurementEnvironmentController meCotnroller = RmiMEConnector.connectToMEController(new URI(
					url));

			MeasurementEnvironmentDefinition med = meCotnroller.getMEDefinition();

			if (med == null) {
				LOGGER.debug("Controller-Status: STATUS_ONLINE_NO_META");
				return MEControllerRPC.STATUS_ONLINE_NO_META;
			} else {
				LOGGER.debug("Controller-Status: STATUS_ONLINE");
				return MEControllerRPC.STATUS_ONLINE;
			}

		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		} catch (RemoteException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		} catch (IllegalStateException x) {
			LOGGER.debug("Controller-Status: STATUS_OFFLINE");
			return MEControllerRPC.STATUS_OFFLINE;
		}
	}

	/**
	 * Sending the given url to the connected clients.
	 * 
	 * @param controllerUrl
	 */
	private void pushNewMECToClients(String controllerUrl) {
		PushPackage push = new PushPackage(Type.NEW_MEC_AVAILABLE);
		push.setPiggyback(controllerUrl);

		String dbId = getUser().getCurrentDatabaseId();

		PushRPCImpl.pushToCODB(dbId, push);
	}

	/**
	 * Checks if the given url is like a valid pattern.
	 * 
	 * @param url
	 * @return
	 */
	private boolean checkUrlIsValid(String url) {
		for (String pattern : CONTROLLER_URL_PATTERN) {
			if (url.matches(pattern)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String[] getValidUrlPattern() {
		return CONTROLLER_URL_PATTERN;
	}

	@Override
	public MeasurementEnvironmentDefinition getMEDefinitionFromMEC(String controllerUrl) {
		try {
			IMeasurementEnvironmentController meCotnroller = RmiMEConnector.connectToMEController(new URI(
					controllerUrl));

			MeasurementEnvironmentDefinition med = meCotnroller.getMEDefinition();

			// if (med.getRoot().getName().isEmpty()) {
			// med.getRoot().setName("root");
			// }

			setNewMEDefinition(med);

			return med;
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		} catch (RemoteException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	@Override
	public MeasurementEnvironmentDefinition getBlankMEDefinition() {
		MeasurementEnvironmentDefinition meDefinition = MeasurementEnvironmentBuilder.createBlankEnvironmentDefinition();

		setNewMEDefinition(meDefinition);

		return meDefinition;
	}

	private void setNewMEDefinition(MeasurementEnvironmentDefinition definition) {
		LOGGER.debug("Set a new environment definition.");
		getUser().getCurrentScenarioDefinitionBuilder().setMEDefinition(definition);

		getUser().storeCurrentScenarioDefinition();
	}

	@Override
	public MeasurementEnvironmentDefinition getCurrentMEDefinition() {
		LOGGER.debug("getCurrentMEDefinition");
		return getUser().getCurrentScenarioDefinitionBuilder().getMEDefinition();
	}

	@Override
	public boolean addNamespace(String path) {
		LOGGER.debug("rpc: addNamespace: {}", path);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().addNamespaces(path);

		if (ns == null) {
			return false;
		}

		getUser().storeCurrentScenarioDefinition();

		pushNotice();
		return true;
	}

	@Override
	public boolean removeNamespace(String path) {
		LOGGER.debug("rpc: removeNamespace: {}", path);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getNamespace(path);

		if (ns == null) {
			LOGGER.debug("nothing found");
			return false;
		}

		if (getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().removeNamespace(ns)) {
			getUser().storeCurrentScenarioDefinition();

			LOGGER.debug("namespace {} removed", path);

			pushNotice();
			return true;
		}

		LOGGER.warn("namespace {} wasn't removed", path);
		return false;
	}

	@Override
	public boolean renameNamespace(String namespacePath, String newName) {
		LOGGER.debug("rpc: renameNamespace: {}", namespacePath);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getNamespace(namespacePath);

		if (ns == null) {
			LOGGER.debug("no namespace '{}' found", ns);
			return false;
		}

		getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().renameNamespace(ns, newName);

		getUser().storeCurrentScenarioDefinition();

		pushNotice();
		return true;
	}

	@Override
	public boolean addParameter(String path, String name, String type, ParameterRole role) {
		LOGGER.debug("rpc: addParameter: {} to '{}'", name, path);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getNamespace(path);

		if (ns == null) {
			LOGGER.debug("no namespace '{}' found", ns);
			return false;
		}

		getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().addParameter(name, type, role, ns);

		getUser().storeCurrentScenarioDefinition();

		pushNotice();
		return true;
	}

	@Override
	public boolean removeParameter(String path, String name) {
		LOGGER.debug("rpc: removeParameter: {} from '{}'", name, path);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getNamespace(path);

		if (ns == null) {
			LOGGER.debug("no namespace '{}' found", ns);
			return false;
		}

		getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().removeParameter(name, ns);

		getUser().storeCurrentScenarioDefinition();

		pushNotice();
		return true;
	}

	@Override
	public boolean updateParameter(String path, String oldName, String newName, String type, ParameterRole role) {
		LOGGER.debug("rpc: updateParameter: {} from '{}'", oldName, path);

		ParameterNamespace ns = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getNamespace(path);

		if (ns == null) {
			LOGGER.debug("no namespace '{}' found", ns);
			return false;
		}

		ParameterDefinition parameter = getUser().getCurrentScenarioDefinitionBuilder().getEnvironmentBuilder().getParameter(oldName, ns);

		if (parameter == null) {
			LOGGER.debug("no parameter '{}' found", oldName);
			return false;
		}

		parameter.setName(newName);
		parameter.setType(type);
		parameter.setRole(role);

		getUser().storeCurrentScenarioDefinition();

		pushNotice();

		return true;
	}

	private void pushNotice() {
		// LOGGER.debug("push updateParameter");
		// PushPackage pp = new PushPackage(Type.NEW_ENV_DEFINITION);
		//
		// String database = getUser().getCurrentDatabaseId();
		// String myScenario =
		// getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario().getScenarioName();
		// for (User u : UserManager.getAllUserOnDatabase(database)) {
		// if (u == getUser()) {
		// continue;
		// }
		//
		// LOGGER.debug("push '{}' to user {}", myScenario, u.getSessionId());
		//
		// if
		// (u.getCurrentScenarioDefinitionBuilder().getBuiltScenario().getScenarioName().equals(myScenario))
		// {
		//
		// try {
		// ScenarioDefinition def =
		// u.getCurrentPersistenceProvider().loadScenarioDefinition(
		// u.getCurrentScenarioDefinitionBuilder().getBuiltScenario().getScenarioName());
		// ScenarioDefinitionBuilder builder =
		// ScenarioDefinitionBuilder.load(def);
		// u.setCurrentScenarioDefinitionBuilder(builder);
		//
		// PushRPCImpl.push(u.getSessionId(), pp);
		// LOGGER.debug("environment changed pushed to user {}",
		// u.getSessionId());
		// } catch (DataNotFoundException e) {
		// LOGGER.warn("no scenario definition found");
		// }
		//
		// }
		// }
	}

	@Override
	public boolean isPortReachable(String host, int port) {
		return ServerCheck.isPortReachable(host, port);
	}

	@Override
	public List<String> getRMIController(String host, int port) {
		return ServerCheck.getRMIController(host, port);
	}
}
