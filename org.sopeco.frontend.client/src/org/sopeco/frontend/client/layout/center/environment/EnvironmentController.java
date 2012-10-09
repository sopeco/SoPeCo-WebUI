package org.sopeco.frontend.client.layout.center.environment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.animation.ICompleteHandler;
import org.sopeco.frontend.client.animation.SlideDown;
import org.sopeco.frontend.client.animation.SlideUp;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentController implements ICenterController, ClickHandler {

	private EnvironmentPanel view;
	private boolean isControllerPanelVisible = false;

	public EnvironmentController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void reset() {
		view = new EnvironmentPanel();

		view.getSliderPanel().addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getSliderPanel()) {
			sliderPanelClickEvent();
		}
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

		view.setSliderImageURL(EnvironmentPanel.SLIDER_PANEL_IMG_HIDDEN);
		view.setSliderPanelText(R.get("getMEfromMEC"));
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
		SlideDown.start(view.getControllerPanel(), EnvironmentPanel.MECPANEL_HEIGHT, new ICompleteHandler() {
			@Override
			public void onComplete() {
				view.getControllerPanel().addStyleName("allowOverflow");
			}
		});

		view.setSliderImageURL(EnvironmentPanel.SLIDER_PANEL_IMG_VISIBLE);
		view.setSliderPanelText(R.get("hideThisArea"));
		DOM.getElementById("mecSliderPanel").addClassName("down");
	}
}
