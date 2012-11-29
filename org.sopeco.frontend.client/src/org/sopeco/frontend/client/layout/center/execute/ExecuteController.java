package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * TODO: This class was for the time being created provisionally.
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController implements ICenterController, ClickHandler {

	private ExecuteView view;
	private boolean running = false;

	public ExecuteController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void reset() {
		view = new ExecuteView();
		addHandler();

		/*Timer t = new Timer() {
			@Override
			public void run() {
				pollingCheckStatus(false);
			}
		};
		t.schedule(1000);*/
	}

	private void addHandler() {
		view.getBtnStartExperiment().addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getBtnStartExperiment()) {
			String url = Manager.get().getControllerUrl();

			if (running) {
				// RPC.getExecuteRPC().stop(url, new AsyncCallback<Void>() {
				// @Override
				// public void onSuccess(Void result) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// Message.error(caught.getMessage());
				// }
				// });
			} else {
				RPC.getExecuteRPC().execute(url, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						view.getBtnStartExperiment().setText(R.get("stop"));
						view.getBtnStartExperiment().setEnabled(false);
						view.getHtmlStatus().setText("Status: running");

						running = true;

						pollingCheckStatus();
					}

					@Override
					public void onFailure(Throwable caught) {
						Message.error(caught.getMessage());
					}
				});
			}
		}
	}

	private void pollingCheckStatus() {
		String url = Manager.get().getControllerUrl();

		if (url.isEmpty()) {
			return;
		}

		RPC.getExecuteRPC().isRunning(url, new AsyncCallback<Long>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(Long result) {
				if (result != -1) {
					long temp = (System.currentTimeMillis() - result) / 1000;

					String since = "[for ";
					if (temp > 60) {
						temp /= 60;
						if (temp > 60) {
							since += (temp / 60) + "h " + (temp % 60) + " min]";
						} else {
							since += temp + " minutes]";
						}
					} else {
						since += (temp) + " seconds]";
					}

					view.getBtnStartExperiment().setText(R.get("stop"));
					view.getBtnStartExperiment().setEnabled(false);
					view.getHtmlStatus().setText("Status: running " + since);

					running = true;

					Timer t = new Timer() {
						@Override
						public void run() {
							// if (repeate) {
							pollingCheckStatus();
							// }
						}
					};

					t.schedule(2500);
				} else {
					view.getBtnStartExperiment().setText(R.get("startExperiment"));
					view.getHtmlStatus().setText("Status: -");
					view.getBtnStartExperiment().setEnabled(true);

					running = false;
				}
			}
		});
	}
}
