package org.sopeco.frontend.client.mec;

import org.sopeco.frontend.client.helper.INotifyHandler;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class ControllerInteraction {

	private static Double checkId = -1D;

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

					handler.call(true, result);

					checkId = -1D;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				handler.call(false, false);
			}
		});
	}

	// private void retrieveController() {
	// final String tempSelectedController = cbController.getText();
	//
	// cbController.clear();
	//
	// if (cbProtocol.getText().equals("rmi://")) {
	// RPC.getMEControllerRPC().getRMIController(tbHostname.getText(),
	// Integer.parseInt(tbPort.getText()),
	// new AsyncCallback<List<String>>() {
	// @Override
	// public void onFailure(Throwable caught) {
	// Message.error(caught.getMessage());
	// }
	//
	// @Override
	// public void onSuccess(List<String> result) {
	// int count = 0;
	// for (String name : result) {
	// cbController.addItem(name);
	//
	// if (name.equals(tempSelectedController)) {
	// cbController.setSelectedIndex(count);
	// }
	//
	// count++;
	// }
	// }
	// });
	// }
	// }

}
