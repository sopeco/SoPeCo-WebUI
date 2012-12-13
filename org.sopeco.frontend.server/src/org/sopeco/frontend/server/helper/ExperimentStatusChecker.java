package org.sopeco.frontend.server.helper;


/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentStatusChecker extends Thread {

//	private static ExperimentStatusChecker checker;
//	private static final long CHECK_INTERVAL = 500;
//
//	public static ExperimentStatusChecker get() {
//		if (checker == null) {
//			checker = new ExperimentStatusChecker();
//			checker.start();
//		}
//		return checker;
//	}
//
//	private Map<String, List<String>> tokenToUsers = new HashMap<String, List<String>>();
//	private boolean running;
//
//	@Override
//	public void run() {
//		running = true;
//		while (running) {
//			for (String token : tokenToUsers.keySet()) {
//				StatusMessage currentStatus;
//				if (StatusBroker.get().getManager(token).hasNext()) {
//					currentStatus = StatusBroker.get().getManager(token).next();
//
//					StatusPackage statusPackage = new StatusPackage(
//							org.sopeco.frontend.shared.push.StatusPackage.EventType.valueOf(currentStatus
//									.getEventType().toString()));
//
//					for (String sessionId : tokenToUsers.get(token)) {
//						PushRPCImpl.push(sessionId, statusPackage);
//					}
//
//					if (currentStatus.getEventType() == EventType.MEASUREMENT_FINISHED) {
//						tokenToUsers.remove(token);
//					}
//				} else {
//					// currentStatus =
//					// StatusBroker.get().getManager(token).getCurrentStatus();
//				}
//			}
//
//			synchronized (tokenToUsers) {
//				try {
//					if (tokenToUsers.isEmpty()) {
//						tokenToUsers.wait();
//					} else {
//						tokenToUsers.wait(CHECK_INTERVAL);
//					}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		checker = null;
//	}
//
//	public void addUser(String token, String sessionId) {
//		synchronized (tokenToUsers) {
//			if (!tokenToUsers.containsKey(token)) {
//				tokenToUsers.put(token, new ArrayList<String>());
//			}
//			tokenToUsers.get(token).add(sessionId);
//			tokenToUsers.notify();
//		}
//	}
}