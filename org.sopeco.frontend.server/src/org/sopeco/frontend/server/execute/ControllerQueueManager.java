package org.sopeco.frontend.server.execute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ControllerQueueManager {

	private ControllerQueueManager() {
	};

	private static Map<String, ControllerQueue> queueMap = new HashMap<String, ControllerQueue>();

	public static ControllerQueue get(String url) {
		if (!queueMap.containsKey(url)) {
			queueMap.put(url, new ControllerQueue(url));
		}
		return queueMap.get(url);
	}

	/**
	 * Returns a list with all ControllerQueues.
	 * 
	 * @return Collection with all queues
	 */
	public static List<ControllerQueue> getAllQueues() {
		return new ArrayList<ControllerQueue>(queueMap.values());
	}

	/**
	 * Returns a list with all running ControllerQueues.
	 * 
	 * @return Collection with all queues
	 */
	public static List<ControllerQueue> getAllRunningQueues() {
		List<ControllerQueue> controllerList = new ArrayList<ControllerQueue>();
		for (ControllerQueue cq : queueMap.values()) {
			if (cq.experimentIsLoaded()) {
				controllerList.add(cq);
			}
		}
		return controllerList;
	}

	public static Collection<ControllerQueue> getAllQueuesClone() {
		return new HashMap<String, ControllerQueue>(queueMap).values();
	}

	public static boolean hasRunningQueues() {
		for (ControllerQueue cq : queueMap.values()) {
			if (cq.experimentIsLoaded()) {
				return true;
			}
		}
		return false;
	}

	// public static List<Long> getRunningExperimentIds() {
	// List<Long> returnList = new ArrayList<Long>();
	// for (ControllerQueue cq : queueMap.values()) {
	// if (cq.isExecuting()) {
	// returnList.add(cq.getCurrentlyRunning().getId());
	// }
	// }
	// return returnList;
	// }
}
