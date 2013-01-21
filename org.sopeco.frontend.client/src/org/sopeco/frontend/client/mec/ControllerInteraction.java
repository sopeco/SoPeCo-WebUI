package org.sopeco.frontend.client.mec;

import java.util.List;

import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.helper.INotifyHandler.Result;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.helper.MEControllerProtocol;

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
	public static void isPortReachable(String host, int port,
			final INotifyHandler<Boolean> handler) {
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
				Message.error(caught.getMessage());
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
