package org.sopeco.frontend.server.rpc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.frontend.client.rpc.ExecuteRPC;
import org.sopeco.frontend.server.helper.ExperimentStatusChecker;
import org.sopeco.persistence.entities.definition.ScenarioDefinition;
import org.sopeco.runner.SoPeCoRunner;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteRPCImpl extends SuperRemoteServlet implements ExecuteRPC {

	/** */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(ExecuteRPCImpl.class.getName());

	private static Map<String, Thread> threadMap = new HashMap<String, Thread>();
	private static Map<String, Long> timeMap = new HashMap<String, Long>();

	@Override
	public void execute(String url) {
		// TODO start Experiment...
		LOGGER.info("start on: " + url);

		try {
			ScenarioDefinition src = getUser().getCurrentScenarioDefinitionBuilder().getBuiltScenario();

			// src.getExperimentSeriesDefinition("x1").getTerminationConditions().iterator().next().getParametersDefaultValues().put("repetitions",
			// "3");

			Configuration.getSessionSingleton(getSessionId()).setMeasurementControllerURI(url);
			Configuration.getSessionSingleton(getSessionId()).setScenarioDescription(src);

			Configuration.getSessionSingleton(getSessionId()).setProperty(IConfiguration.SENDING_STATUS_MESSAGES,
					"true");

			SoPeCoRunner runner = new SoPeCoRunner(getSessionId());

			threadMap.put(url, new Thread(runner));
			timeMap.put(url, System.currentTimeMillis());

			threadMap.get(url).start();

			long s = System.currentTimeMillis();
			String token;
			while ((token = StatusBroker.get().getToken(getSessionId() + url)) == null) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}

			ExperimentStatusChecker.get().addUser(token, getSessionId());

			System.out.println("\t>waiting - " + (System.currentTimeMillis() - s));
			System.out.println(token);

		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// private void releaseMEC(String url) {
	// try {
	// IMeasurementEnvironmentController meCotnroller =
	// RmiMEConnector.connectToMEController(new URI(
	// url));
	//
	// meCotnroller.releaseMEController(getSessionId());
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// LOGGER.warning("Cant release controller '" + url + "'");
	// } catch (RemoteException e) {
	// e.printStackTrace();
	// LOGGER.warning("Cant release controller '" + url + "'");
	// }
	// }

	@Override
	public long isRunning(String url) {
		if (!threadMap.containsKey(url)) {
			return -1;
		}

		if (threadMap.get(url).isAlive()) {
			return timeMap.get(url);
		}

		return -1;
	}

	@Override
	public void stop(String url) {
		if (!threadMap.containsKey(url)) {
			return;
		}

		LOGGER.info("interrupt");
		threadMap.get(url).interrupt();
	}
}
