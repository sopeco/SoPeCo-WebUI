/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.webui.server.execute;

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
