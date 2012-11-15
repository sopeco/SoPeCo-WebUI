package org.sopeco.frontend.server.helper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ServerCheck {

	private static final Logger LOGGER = Logger.getLogger(ServerCheck.class.getName());

	private static final int SOCKET_TIMEOUT = 1000;

	private static final int PORT_RMI = 1099;
	private static final int PORT_HTTP = 80;
	private static final int PORT_HTTPS = 443;

	private ServerCheck() {
	}

	/**
	 * Tries to connect to the specified host and port. If that is successful,
	 * it will return true.
	 * 
	 * @param host
	 *            target host
	 * @param port
	 *            taget port
	 * @return connection to given port possible
	 */
	public static boolean isPortReachable(String host, int port) {
		try {
			SocketAddress socketAddress = new InetSocketAddress(host, port);

			Socket socket = new Socket();
			socket.connect(socketAddress, SOCKET_TIMEOUT);
			socket.close();
			return true;
		} catch (UnknownHostException e) {
			LOGGER.info("unknown host '" + host + ":" + port + "'");
		} catch (IOException e) {
			LOGGER.info("IOException at connection to '" + host + ":" + port + "'");
		}
		return false;
	}

	/**
	 * Checked using the {@link #isPortReachable(String, int)} method whether
	 * the port 1099 is open.
	 * 
	 * @param host
	 *            target host
	 * @return
	 */
	public static boolean serverHasRMI(String host) {
		return isPortReachable(host, PORT_RMI);
	}

	/**
	 * Checked using the {@link #isPortReachable(String, int)} method whether
	 * the port 1099 is open.
	 * 
	 * @param host
	 *            target host
	 * @return
	 */
	public static boolean serverHasHTTP(String host) {
		return isPortReachable(host, PORT_HTTP);
	}

	/**
	 * Checked using the {@link #isPortReachable(String, int)} method whether
	 * the port 1099 is open.
	 * 
	 * @param host
	 *            target host
	 * @return
	 */
	public static boolean serverHasHTTPS(String host) {
		return isPortReachable(host, PORT_HTTPS);
	}

	/**
	 * Returns a String Array which contains all available RMI-controller on the
	 * given host.
	 * 
	 * @param host
	 *            target host
	 * @param port
	 *            target port
	 * @return
	 */
	public static List<String> getRMIController(String host, int port) {
		try {
			String prefix = "rmi://";
			if (host.length() <= prefix.length() || !host.substring(0, prefix.length()).equals(prefix)) {
				host = prefix + host;
			}

			String[] lookupArray = Naming.list(host + ":" + port);

			List<String> retList = new ArrayList<String>();

			for (String name : lookupArray) {
				if (name.matches("\\/\\/[a-zA-Z0-9\\.\\-]*:[0-9]{1,5}\\/[a-zA-Z0-9]+")) {
					try {
						Remote remoteStub = Naming.lookup(name);
						if (remoteStub instanceof IMeasurementEnvironmentController) {
							String controllerName = name.substring(name.lastIndexOf("/") + 1);
							retList.add(controllerName);
						} else {
							LOGGER.info(name + " is not a MEC-RMI");
						}
					} catch (NotBoundException e) {
						e.printStackTrace();
					}
				}
			}
			return retList;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
