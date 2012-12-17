package org.sopeco.frontend.server.execute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
	private static ExecutorService threadPool;

	private QueuedExperiment currentlyRunning;
	private List<QueuedExperiment> experimentQueue;
	private String currentRandomId, currentToken;
	private Future<?> currentStatus;

	private static ExecutorService getThreadPool() {
		if (threadPool == null) {
			threadPool = Executors.newCachedThreadPool();
		}
		return threadPool;
	}

	public ControllerQueue() {
		experimentQueue = new ArrayList<QueuedExperiment>();
	}

	public boolean isExecuting() {
		if (currentStatus != null && !currentStatus.isDone() && !currentStatus.isCancelled()) {
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
			currentStatus = getThreadPool().submit(runner);

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
