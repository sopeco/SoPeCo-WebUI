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
import org.sopeco.engine.status.EventType;
import org.sopeco.engine.status.IStatusListener;
import org.sopeco.engine.status.ProgressInfo;
import org.sopeco.engine.status.StatusBroker;
import org.sopeco.engine.status.StatusMessage;
import org.sopeco.frontend.client.rpc.PushRPC.Type;
import org.sopeco.frontend.server.persistence.UiPersistence;
import org.sopeco.frontend.server.persistence.entities.ScheduledExperiment;
import org.sopeco.frontend.server.rpc.PushRPCImpl;
import org.sopeco.frontend.server.user.UserManager;
import org.sopeco.frontend.shared.entities.RunningControllerStatus;
import org.sopeco.frontend.shared.push.PushListPackage;
import org.sopeco.frontend.shared.push.PushObjectPackage;
import org.sopeco.frontend.shared.push.PushSerializable;
import org.sopeco.persistence.metadata.entities.DatabaseInstance;
import org.sopeco.runner.SoPeCoRunner;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ControllerQueue implements IStatusListener {

	private static final Logger LOGGER = Logger.getLogger(ControllerQueue.class.getName());
	private static ExecutorService threadPool;

	/** Queue of waiting experiments. */
	private List<QueuedExperiment> experimentQueue;

	/** The experiment which is performed at the moment. */
	private QueuedExperiment runningExperiment;

	private String currentToken;
	private Future<?> executeStatus;

	private String controllerURL;

	/**
	 * Creates an ThreadPool, which is responsible for the SoPeCo Runners.
	 * 
	 * @return ExecutorService
	 */
	private static ExecutorService getThreadPool() {
		if (threadPool == null) {
			threadPool = Executors.newCachedThreadPool();
		}
		return threadPool;
	}

	/**
	 * Constructor.
	 */
	public ControllerQueue(String pControllerURL) {
		experimentQueue = new ArrayList<QueuedExperiment>();
		controllerURL = pControllerURL;
		StatusBroker.getManager(pControllerURL).addStatusListener(this);
	}

	/**
	 * Returns whether the active experiment is executed by a thread.
	 * 
	 * @return true if a runner is running
	 */
	public boolean isExecuting() {
		if (executeStatus != null) {
			synchronized (executeStatus) {
				if (executeStatus.isDone() || executeStatus.isCancelled()) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether an experiment is loaded, and perhaps is executed.
	 * 
	 * @return true if an experiment is active
	 */
	public boolean experimentIsLoaded() {
		if (runningExperiment != null) {
			return true;
		}
		return false;
	}

	/**
	 * Adds an experiment to this ControllerQueue. If no experiment is executed
	 * yet, this will be executed.
	 * 
	 * @param experiment
	 */
	public void addExperiment(QueuedExperiment experiment) {
		LOGGER.info("Adding experiment id:" + experiment.getScheduledExperiment().getId() + " to queue.");
		experiment.setTimeQueued(System.currentTimeMillis());
		experimentQueue.add(experiment);
		checkQueue();
	}

	/**
	 * Checks if the controller is ready and a experiment is waiting in the
	 * queue. If so, the next experiment is started.
	 */
	private void checkQueue() {
		synchronized (experimentQueue) {
			LOGGER.info("Looking for waiting experiment..");
			if (isExecuting()) {
				LOGGER.info("Controller is running.");
				pushCurrentControllerQueue();
				return;
			} else if (experimentIsLoaded()) {
				LOGGER.info("Experiment is already loaded.");
			} else if (experimentQueue.isEmpty()) {
				LOGGER.info("Queue is empty.");
			} else {
				runningExperiment = experimentQueue.get(0);
				experimentQueue.remove(0);
				pushCurrentControllerQueue();
				execute();
			}
		}
	}

	/**
	 * Start the execution of the given experiment.
	 * 
	 * @param experiment
	 */
	private void execute() {
		LOGGER.info("Start experiment id:" + runningExperiment.getScheduledExperiment().getId() + " on: "
				+ runningExperiment.getScheduledExperiment().getControllerUrl());

		try {

			String randomId = "RANDOMID" + (long) (Long.MAX_VALUE * Math.random());

			IConfiguration config = Configuration.getSessionSingleton(randomId);
			config.overwrite((Configuration) runningExperiment.getScheduledExperiment().getConfiguration());
			config.setMeasurementControllerURI(runningExperiment.getScheduledExperiment().getControllerUrl());
			config.setScenarioDescription(runningExperiment.getScheduledExperiment().getScenarioDefinition());
			config.setProperty(IConfiguration.SENDING_STATUS_MESSAGES, "true");
			config.setProperty(IConfiguration.CONF_MEC_CONNECTION_TYPE, "REST");

			SoPeCoRunner runner = new SoPeCoRunner(randomId);
			executeStatus = getThreadPool().submit(runner);

			runningExperiment.setTimeStarted(System.currentTimeMillis());

			currentToken = waitForToken(randomId);

			ProgressWatcher.get().continueLoop();

			notifyAccount();
			// notifyStatusChange(EStatus.START_MEASUREMENT.toString());
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Waiting for the token string with which the status of the current SoPeCo
	 * Runner can be requested.
	 * 
	 * @param id
	 *            which is used for the configuration of the runner
	 * @return token string or null if a timeout is exceeded
	 */
	private String waitForToken(String id) {
		LOGGER.info("Waiting for Token..");
		int loopLimit = 100;
		String token = null;
		while (token == null && loopLimit-- > 0) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			// token = StatusBroker.get().getToken(id +
			// runningExperiment.getScheduledExperiment().getControllerUrl());
			token = "dummy";
		}
		return token;
	}

	/**
	 * Returns the current token with which the status can be requested of the
	 * {@link StatusBroker#getManager(String)} or null if no status is
	 * available.
	 * 
	 * @return token string
	 */
	public String getCurrentToken() {
		return currentToken;
	}

	/**
	 * Ends the execution of the current experiment and stores information about
	 * it.
	 */
	synchronized void finished() {
		LOGGER.info("Experiment id:" + runningExperiment.getScheduledExperiment().getId() + " finished on: "
				+ runningExperiment.getScheduledExperiment().getControllerUrl());

		runningExperiment.setTimeEnded(System.currentTimeMillis());
		saveDurationInExperiment();

		notifyAccount();

		executeStatus = null;
		runningExperiment = null;
		checkQueue();
	}

	/**
	 * Stores the duration of this execution in the list, which is stored in the
	 * ScheduledExperiment.
	 */
	private void saveDurationInExperiment() {
		ScheduledExperiment exp = UiPersistence.getUiProvider().loadScheduledExperiment(
				runningExperiment.getScheduledExperiment().getId());
		if (exp != null) {
			if (exp.getDurations() == null) {
				exp.setDurations(new ArrayList<Long>());
			}
			long duration = runningExperiment.getTimeEnded() - runningExperiment.getTimeStarted();
			exp.getDurations().add(duration);
			UiPersistence.getUiProvider().storeScheduledExperiment(exp);
		}
	}

	/**
	 * Returns the experiment which is loaded at the moment.
	 * 
	 * @return QueuedExperiment
	 */
	public QueuedExperiment getCurrentlyRunning() {
		return runningExperiment;
	}

	/**
	 * Pushes the next status of the running experiment.
	 * 
	 * @param statusMessage
	 */
	public void nextStatusMessage(StatusMessage statusMessage) {
		runningExperiment.getEventLogList().add(statusMessage);
		if (statusMessage.getStatusInfo() != null && statusMessage.getStatusInfo() instanceof ProgressInfo) {
			runningExperiment.setLastProgressInfo((ProgressInfo) statusMessage.getStatusInfo());
		}

		pushRunningExperimentStatus();
	}

	/**
	 * Pushes the status information of the current running experiment to all
	 * users, which use this MEController.
	 */
	private void pushRunningExperimentStatus() {
		PushObjectPackage objectPackage = new PushObjectPackage();
		objectPackage.setType(Type.PUSH_CURRENT_CONTROLLER_EXPERIMENT);
		objectPackage.setAttachment(createControllerStatusPackage());

		PushRPCImpl.pushToAllOnController(runningExperiment.getScheduledExperiment().getControllerUrl(), objectPackage);
	}

	/**
	 * Pushes a list with the QueuedExperiments, which are waiting in the
	 * controller queue.
	 */
	private void pushCurrentControllerQueue() {
		if (runningExperiment == null) {
			return;
		}

		ArrayList<PushSerializable> list = new ArrayList<PushSerializable>();
		for (QueuedExperiment experiment : experimentQueue) {
			list.add(experiment.getScheduledExperiment().createFrontendScheduledExperiment());
		}

		PushListPackage listPackage = new PushListPackage();
		listPackage.setType(Type.PUSH_CURRENT_CONTROLLER_QUEUE);
		listPackage.setAttachment(list);

		PushRPCImpl.pushToAllOnController(runningExperiment.getScheduledExperiment().getControllerUrl(), listPackage);
	}

	//
	// #################################### \/ \/ \/ checken \/ \/ \/ \/ \/ \/
	//

	@Override
	public void onNewStatus(StatusMessage statusMessage) {
		LOGGER.info("New Status on '" + this.controllerURL + "': " + statusMessage.getEventType());
		nextStatusMessage(statusMessage);
	}

	/**
	 * Creates the CurrentControllerExperiment object, that contains all
	 * necessary information about the current controller state.
	 * */
	public RunningControllerStatus createControllerStatusPackage() {
		RunningControllerStatus cce = new RunningControllerStatus();

		cce.setAccount(runningExperiment.getScheduledExperiment().getAccount());
		cce.setScenario(runningExperiment.getScheduledExperiment().getScenarioDefinition().getScenarioName());
		cce.setTimeStart(runningExperiment.getTimeStarted());
		cce.setLabel(runningExperiment.getScheduledExperiment().getLabel());
		cce.setEventLogList(runningExperiment.getEventLogLiteList());

		if (runningExperiment.getEventLogList().get(runningExperiment.getEventLogList().size() - 1).getEventType() == EventType.MEASUREMENT_FINISHED) {
			cce.setHasFinished(true);
		} else {
			cce.setHasFinished(false);
		}

		if (runningExperiment.getLastProgressInfo() != null) {
			ProgressInfo info = runningExperiment.getLastProgressInfo();
			float progress = 100F / info.getNumberOfRepetition() * info.getRepetition();
			cce.setProgress(progress);
		} else {
			cce.setProgress(-1);
		}

		if (runningExperiment.getScheduledExperiment().getDurations().size() > 2) {
			long sum = 0;
			for (long l : runningExperiment.getScheduledExperiment().getDurations()) {
				sum += l;
			}
			long estiamtedDuration = sum / runningExperiment.getScheduledExperiment().getDurations().size();
			cce.setTimeRemaining(estiamtedDuration);
		}

		return cce;
	}

	/**
	 * Sends all schedule exps.
	 */
	private void notifyAccount() {
		List<ScheduledExperiment> resultList = UiPersistence.getUiProvider().loadScheduledExperimentsByAccount(
				runningExperiment.getScheduledExperiment().getAccount());

		ArrayList<PushSerializable> fseList = new ArrayList<PushSerializable>();
		for (ScheduledExperiment experiment : resultList) {
			fseList.add(experiment.createFrontendScheduledExperiment());
		}

		PushListPackage listPackage = new PushListPackage();
		listPackage.setType(Type.PUSH_SCHEDULED_EXPERIMENT);
		listPackage.setAttachment(fseList);

		for (String sId : UserManager.getAllUsers().keySet()) {
			DatabaseInstance db = UserManager.getUser(sId).getCurrentAccount();
			if (db != null && db.getDbName().equals(runningExperiment.getScheduledExperiment().getAccount())) {
				PushRPCImpl.push(sId, listPackage);
			}
		}
	}

}
