package org.sopeco.frontend.server.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.config.Configuration;
import org.sopeco.config.IConfiguration;
import org.sopeco.config.exception.ConfigurationException;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.runner.SoPeCoRunner;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ControllerQueue {

	private static final Logger LOGGER = Logger.getLogger(ControllerQueue.class.getName());

	private QueuedExperiment currentlyRunning;
	private List<QueuedExperiment> experimentQueue;
	private Thread sopecoRunnerThread;
	private String currentRandomId, currentToken;

	public ControllerQueue() {
		experimentQueue = new ArrayList<QueuedExperiment>();
	}

	public boolean isExecuting() {
		if (sopecoRunnerThread != null && sopecoRunnerThread.isAlive()) {
			return true;
		}
		return false;
	}

	public void addExperiment(QueuedExperiment experiment) {
		experimentQueue.add(experiment);
		executeNext();
	}

	private void executeNext() {
		if (currentlyRunning == null && !experimentQueue.isEmpty()) {
			currentlyRunning = experimentQueue.get(0);
			experimentQueue.remove(0);
			execute(currentlyRunning);
		}
	}

	public synchronized void finished() {
		if (currentlyRunning != null) {
			sopecoRunnerThread = null;
			LOGGER.info("Experiment finished on: " + currentlyRunning.getControllerUrl());
			currentlyRunning = null;
			executeNext();
		}
	}

	public String getCurrentToken() {
		return currentToken;
	}

	public QueuedExperiment getCurrentlyRunning() {
		return currentlyRunning;
	}

	private void execute(QueuedExperiment experiment) {
		LOGGER.info("Start experiment on: " + experiment.getControllerUrl());
		try {
			experiment.setTimeStarted(System.currentTimeMillis());

			currentRandomId = System.currentTimeMillis() + "" + Math.random();

			Configuration.getSessionSingleton(currentRandomId).overwrite((Configuration) experiment.getConfiguration());

			Configuration.getSessionSingleton(currentRandomId).setMeasurementControllerURI(
					experiment.getControllerUrl());
			Configuration.getSessionSingleton(currentRandomId).setScenarioDescription(
					experiment.getScenarioDefinition());

			Configuration.getSessionSingleton(currentRandomId).setProperty(IConfiguration.SENDING_STATUS_MESSAGES,
					"true");

			SoPeCoRunner runner = new SoPeCoRunner(currentRandomId);
			sopecoRunnerThread = new Thread(runner);
			sopecoRunnerThread.start();

			// ###############################
			LOGGER.info("Waiting for Token..");
			int limit = 100;
			while ((currentToken = StatusBroker.get().getToken(currentRandomId + experiment.getControllerUrl())) == null
					&& limit-- > 0) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}

			ProgressChecker.get().check();
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
