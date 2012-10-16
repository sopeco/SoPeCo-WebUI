package org.sopeco.frontend.client.layout.center.environment;

import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.animation.ICompleteHandler;
import org.sopeco.frontend.client.animation.SlideDown;
import org.sopeco.frontend.client.animation.SlideUp;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.environment.EnvironmentView.ControllerStatus;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
@SuppressWarnings("rawtypes")
public class EnvironmentController implements ICenterController, ClickHandler, ValueChangeHandler {

	private EnvironmentView view;
	private boolean isControllerPanelVisible = false;
	private String[] validControllerUrlPattern;
	private int controllerPanelHeight;
	private MeasurementEnvironmentDefinition currentMeasurementEnvironment;

	public EnvironmentController() {
		getValidUrlPattern();

		reset();
	}

	@Override
	public Widget getView() {
		return new ScrollPanel(view);
	}

	@Override
	public void reset() {
		view = new EnvironmentView();

		view.getSliderPanel().addClickHandler(this);
		view.getGetEnvironmentButton().addClickHandler(this);
		view.getControllerComboBox().addValueChangeHandler(this);

		getControllerPanelheight();
		updateControllerURLList();
	}

	/**
	 * Return the current measurement environment.
	 * 
	 * @return MeasurementEnvironmentDefinition
	 */
	public MeasurementEnvironmentDefinition getCurrenEnvironmentDefinition() {
		return currentMeasurementEnvironment;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getSliderPanel()) {
			// Click-Event of SliderPanel
			sliderPanelClickEvent();
		} else if (event.getSource() == view.getGetEnvironmentButton()) {
			// Click-Event of "Get Env.." Button
			startControllerStatusCheck(true);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getSource() == view.getControllerComboBox()) {
			// ChangeEvent of the Combobox
			comboboxChangedValue();
		}
	}

	/**
	 * Check if the value, entered in the text field, is a valid URL controller.
	 */
	private void comboboxChangedValue() {
		if (checkComboboxURL()) {
			startControllerStatusCheck();
		} else {
			view.setControllerStatus(ControllerStatus.InvalidURL);
		}
	}

	/**
	 * Returns whether the text of the combobox is a valid controller URL.
	 * 
	 * @return
	 */
	private boolean checkComboboxURL() {
		if (isStringValidControllerURL(view.getControllerComboBox().getText())) {
			view.getControllerComboBox().removeStyleName("invalid");
			return true;
		} else {
			view.getControllerComboBox().addStyleName("invalid");
			return false;
		}
	}

	/**
	 * Check the status of the controller, whose URL is in the combobox.
	 */
	private void startControllerStatusCheck() {
		startControllerStatusCheck(false);
	}

	/**
	 * Check the status of the controller, whose URL is in the combobox. If the
	 * controller is online, the environment of itS will be retrieved.
	 * 
	 * @param retreiveEnvironment
	 *            retrieve environmen
	 */
	private void startControllerStatusCheck(final boolean retreiveEnvironment) {
		view.setControllerStatus(ControllerStatus.Checking);

		final String controllerURL = view.getControllerComboBox().getText();

		if (!view.getControllerComboBox().containsString(controllerURL)) {
			view.getControllerComboBox().addItem(controllerURL);
		}

		RPC.getMEControllerRPC().checkControllerStatus(controllerURL, new AsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {

				boolean isGetEnvironmentEnabled = false;

				switch (result) {
				case MEControllerRPC.STATUS_ONLINE:
					view.setControllerStatus(ControllerStatus.Online);
					isGetEnvironmentEnabled = true;

					if (retreiveEnvironment) {
						getEnvironmentFromController(controllerURL);
					}
					break;
				case MEControllerRPC.STATUS_OFFLINE:
					view.setControllerStatus(ControllerStatus.Offline);
					break;
				case MEControllerRPC.NO_VALID_MEC_URL:
					view.setControllerStatus(ControllerStatus.InvalidURL);
					break;
				case MEControllerRPC.STATUS_ONLINE_NO_META:
					view.setControllerStatus(ControllerStatus.OnlineLimited);
					break;
				default:
					view.setControllerStatus(ControllerStatus.Unknown);
				}

				view.setGetEnvironmentButtonStatus(isGetEnvironmentEnabled);
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());

				view.setGetEnvironmentButtonStatus(false);
				view.setControllerStatus(ControllerStatus.Unknown);
			}
		});
	}

	/**
	 * Retrieve the environment of the controller with the given URL.
	 */
	private void getEnvironmentFromController(final String controllerURL) {
		Loader.showLoader();
		RPC.getMEControllerRPC().getMEDefinitionFromMEC(controllerURL,
				new AsyncCallback<MeasurementEnvironmentDefinition>() {

					@Override
					public void onFailure(Throwable caught) {
						Loader.hideLoader();
						Message.error(caught.getMessage());
					}

					@Override
					public void onSuccess(MeasurementEnvironmentDefinition result) {
						currentMeasurementEnvironment = result;
						view.getEnvironmentDefinitonTreePanel().setEnvironmentDefiniton(result);

						Loader.hideLoader();
					}
				});
	}

	/**
	 * Returns whether the string is a valid rmi-URL.
	 * 
	 * @return
	 */
	private boolean isStringValidControllerURL(String url) {
		if (validControllerUrlPattern == null) {
			return false;
		}

		for (String pattern : validControllerUrlPattern) {
			if (url.matches(pattern)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Saving the full height of the controllerPanel. Required for the show/hide
	 * animation.
	 */
	private void getControllerPanelheight() {
		// Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		// @Override
		// public void execute() {
		// int height = view.getContentWrapper().getOffsetHeight();
		// if (height != 0) {
		// controllerPanelHeight = height;
		// }
		// }
		// });
		controllerPanelHeight = 168;
	}

	/**
	 * Gets all valid MEControllerURL-Patterns from the backend.
	 */
	private void getValidUrlPattern() {
		RPC.getMEControllerRPC().getValidUrlPattern(new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error("Can't get valid url patterns.");
			}

			@Override
			public void onSuccess(String[] result) {
				validControllerUrlPattern = result;
			}
		});
	}

	/**
	 * This method will be executed, when the sliderPanel is pressed.
	 */
	private void sliderPanelClickEvent() {
		if (isControllerPanelVisible) {
			hideControllerPanel();
		} else {
			showControllerPanel();
		}
	}

	/**
	 * Hiding the controllerPanel.
	 */
	private void hideControllerPanel() {
		if (!isControllerPanelVisible) {
			GWT.log("hide false");
			return;
		}

		view.getControllerPanel().removeStyleName("allowOverflow");

		isControllerPanelVisible = false;
		SlideUp.start(view.getControllerPanel(), new ICompleteHandler() {
			@Override
			public void onComplete() {
				DOM.getElementById("mecSliderPanel").removeClassName("down");
			}
		});

		view.setSliderImageURL(EnvironmentView.SLIDER_PANEL_IMG_HIDDEN);
		view.setSliderPanelText(R.get("getMEfromMEC"));
	}

	public void updateControllerURLList() {

		RPC.getMEControllerRPC().getMEControllerList(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				view.getControllerComboBox().clear();

				for (String controllerURL : result) {
					view.getControllerComboBox().addItem(controllerURL);
				}
			}
		});
	}

	/**
	 * Shows the controllerPanel.
	 */
	private void showControllerPanel() {
		if (isControllerPanelVisible) {
			GWT.log("show false");
			return;
		}

		isControllerPanelVisible = true;
		SlideDown.start(view.getControllerPanel(), controllerPanelHeight, new ICompleteHandler() {
			@Override
			public void onComplete() {
				view.getControllerPanel().addStyleName("allowOverflow");
			}
		});

		view.setSliderImageURL(EnvironmentView.SLIDER_PANEL_IMG_VISIBLE);
		view.setSliderPanelText(R.get("hideThisArea"));
		DOM.getElementById("mecSliderPanel").addClassName("down");
	}
}
