package org.sopeco.frontend.server.rpc;

import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.rmi.RmiMEConnector;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.server.db.PersistenceProvider;
import org.sopeco.frontend.server.db.entities.MEControllerUrl;
import org.sopeco.frontend.server.model.MeasurementEnvironmentBuilder;
import org.sopeco.frontend.server.model.ScenarioDefinitionBuilder;
import org.sopeco.frontend.server.user.UserInfo;
import org.sopeco.frontend.shared.definitions.PushPackage;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;
import org.sopeco.persistence.exceptions.DataNotFoundException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class MEControllerRPCImpl extends RemoteServiceServlet implements MEControllerRPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MEControllerRPCImpl.class);

	private static final String[] CONTROLLER_URL_PATTERN = new String[] { "^rmi://[a-zA-Z0-9\\.]+(:[0-9]{1,5})?/[a-zA-Z][a-zA-Z0-9]*$" };

	@Override
	public List<String> getMEControllerList() {
		try {
			List<MEControllerUrl> controllerList = PersistenceProvider.getUIPersistenceProvider(
					getThreadLocalRequest().getSession()).loadAllMEControllerUrls();

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
			return MEControllerRPC.NO_VALID_MEC_URL;
		}

		HttpSession currentSession = getThreadLocalRequest().getSession();
		if (!PersistenceProvider.getUIPersistenceProvider(currentSession).checkMEControllerUrlExists(url)) {

			PersistenceProvider.getUIPersistenceProvider(currentSession).storeMEControllerUrl(url);

			pushNewMECToClients(url);
		}

		try {
			IMeasurementEnvironmentController meCotnroller = RmiMEConnector.connectToMEController(new URI(url));

			MeasurementEnvironmentDefinition med = meCotnroller.getMEDefinition();

			if (med == null) {
				return MEControllerRPC.STATUS_ONLINE_NO_META;
			} else {
				return MEControllerRPC.STATUS_ONLINE;
			}

		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		} catch (RemoteException e) {
			LOGGER.error(e.getMessage());
			throw new IllegalStateException(e);
		} catch (IllegalStateException x) {
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

		String dbName = UserInfo.getDatabaseOfSession(getThreadLocalRequest().getSession().getId());

		PushRPCImpl.pushToCODB(dbName, push);
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
			IMeasurementEnvironmentController meCotnroller = RmiMEConnector
					.connectToMEController(new URI(controllerUrl));

			MeasurementEnvironmentDefinition med = meCotnroller.getMEDefinition();

			MeasurementEnvironmentBuilder b = new MeasurementEnvironmentBuilder();
			b.addNamespaces("input/user_count");
			b.addNamespaces("input/user_activity");
			b.addNamespaces("input/parameter_n");
			b.addNamespaces("output/response_time");
			b.addNamespaces("output/throughput");
			
			return b.getMEDefinition();
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
		return MeasurementEnvironmentBuilder.createBlankEnvironmentDefinition();
	}
}
