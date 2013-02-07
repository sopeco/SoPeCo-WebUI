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
package org.sopeco.webui.server.helper;

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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.engine.measurementenvironment.IMeasurementEnvironmentController;
import org.sopeco.engine.measurementenvironment.connector.RestMEConnector;
import org.sopeco.webui.shared.helper.MEControllerProtocol;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ServerCheck {

	private static final Logger LOGGER = Logger.getLogger(ServerCheck.class.getName());

	private static final int SOCKET_TIMEOUT = 10000;

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
			LOGGER.info("Unknown host '" + host + ":" + port + "'");
		} catch (IOException e) {
			LOGGER.info("IOException at connection to '" + host + ":" + port + "'");
		}
		return false;
	}

	public static List<String> getController(MEControllerProtocol protocol, String host, int port) {
		switch (protocol) {
		case REST_HTTP:
			return getRestMEController(host, port);
		case RMI:
			return getRmiMEController(host, port);
		default:
			throw new IllegalStateException(protocol + " is not valid.");
		}
	}

	private static List<String> getRestMEController(String host, int port) {
		try {
			return Arrays.asList(RestMEConnector.getAvailableMEController("http://" + host + ":" + port + "/"));
		} catch (Exception x) {
			//TODO Exception handling
			x.printStackTrace();
			return new ArrayList<String>();
		}
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
	private static List<String> getRmiMEController(String host, int port) {
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
