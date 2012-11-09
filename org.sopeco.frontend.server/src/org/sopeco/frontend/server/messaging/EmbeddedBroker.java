package org.sopeco.frontend.server.messaging;

import java.util.logging.Logger;

import org.apache.activemq.broker.BrokerService;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class EmbeddedBroker {

	private static final Logger LOGGER = Logger.getLogger(EmbeddedBroker.class.getName());
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 61616;

	private static EmbeddedBroker embeddedBroker;
	private static boolean running = false;

	/**
	 * Returns whether the messaging server is running.
	 * 
	 * @return the running
	 */
	public static boolean isRunning() {
		return running;
	}

	public static void startMessagingBroker() {
		startMessagingBroker(DEFAULT_HOST, DEFAULT_PORT);
	}

	public static void startMessagingBroker(String host, int port) {
		try {
			getEmbeddedBroker().startBroker(host, port);

			running = true;
		} catch (Exception e) {
			LOGGER.warning(e.getMessage());
			running = false;
		}
	}

	/**
	 * Returns the EmbeddedBroker Singleton. If it doesnt exist, it will be
	 * created.
	 * 
	 * @return
	 */
	private static EmbeddedBroker getEmbeddedBroker() {
		if (embeddedBroker == null) {
			embeddedBroker = new EmbeddedBroker();
		}
		return embeddedBroker;
	}

	private BrokerService broker;

	private EmbeddedBroker() {
	}

	private void startBroker(String pHost, int pPort) throws Exception {
		broker = new BrokerService();

		broker.addConnector("tcp://" + pHost + ":" + pPort);

		broker.start();
	}
}
