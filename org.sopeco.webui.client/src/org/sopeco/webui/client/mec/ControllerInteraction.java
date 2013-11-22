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
package org.sopeco.webui.client.mec;

import java.util.List;

import org.sopeco.webui.client.SoPeCoUI;
import org.sopeco.webui.client.helper.INotifyHandler;
import org.sopeco.webui.client.helper.INotifyHandler.Result;
import org.sopeco.webui.shared.helper.MEControllerProtocol;
import org.sopeco.webui.shared.rpc.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ControllerInteraction {

	private static Double checkId = -1D;
	public static final String KEY_PORT_REACHABLE = "PORT_REACHABLE";
	public static final String KEY_RETRIEVE_MEC = "KEY_RETRIEVE_MEC";

	/**
	 * Default ports of the MEController, ordered by {@link Protocol} (RMI,
	 * HTTP).
	 */
	public static final int[] DEFAULT_PORTS = new int[] { 1099, 1300 };

	private ControllerInteraction() {
	}

	/**
	 * Checks whether the given host accepts a connection on the given port.
	 * 
	 * @param host
	 *            host adress
	 * @param port
	 *            port on the host
	 * @param handler
	 *            event that is called after the result was determined
	 */
	public static void isPortReachable(String host, int port, final INotifyHandler<Boolean> handler) {
		final double myId = Math.random();
		checkId = myId;

		RPC.getMEControllerRPC().isPortReachable(host, port, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				synchronized (checkId) {
					if (checkId != myId) {
						return;
					}

					Result<Boolean> callResult = new Result<Boolean>(true, result, KEY_PORT_REACHABLE);
					handler.call(callResult);

					checkId = -1D;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Result<Boolean> callResult = new Result<Boolean>(false, false);
				handler.call(callResult);
			}
		});
	}

	public static void retrieveController(MEControllerProtocol protocol, final String host, final int port,
			final INotifyHandler<String[]> handler) {

		RPC.getMEControllerRPC().getController(protocol, host, port, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				SoPeCoUI.get().onUncaughtException(caught);

				Result<String[]> callResult = new Result<String[]>(false, null);
				handler.call(callResult);
			}

			@Override
			public void onSuccess(List<String> result) {
				if (result == null) {
					handler.call(null);
					return;
				}

				String[] resultArray = result.toArray(new String[0]);
				Result<String[]> callResult = new Result<String[]>(true, resultArray, KEY_RETRIEVE_MEC);
				handler.call(callResult);
			}
		});

	}

}
