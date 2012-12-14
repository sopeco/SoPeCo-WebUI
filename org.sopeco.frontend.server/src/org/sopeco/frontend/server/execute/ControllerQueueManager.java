package org.sopeco.frontend.server.execute;

import java.util.Collection;
import java.util.HashMap;
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
			queueMap.put(url, new ControllerQueue());
		}
		return queueMap.get(url);
	}

	public static int activeQueueCount() {
		int c = 0;
		for (String key : new HashMap<String, ControllerQueue>(queueMap).keySet()) {
			if (queueMap.get(key).isExecuting()) {
				c++;
			} else {
				queueMap.remove(key);
			}
		}
		return c;
	}

	public static Collection<ControllerQueue> getAllQueues() {
		return queueMap.values();
	}
}
