package org.sopeco.frontend.client.layout.center.environment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.widget.ClickPanel;
import org.sopeco.frontend.client.widget.ComboBox;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

/**
 * This panel consists of three elements.<br>
 * <b>Element 1:</b> [Panel to get the ME of a MEController (controllerPanel)]<br>
 * <b>Element 2:</b> [Slider to toggle the visibility of the controllerPanel
 * (sliderPanel)]<br>
 * <b>Element 3:</b> [MeasurementEnvironment Tree
 * (environmentDefinitionTreePanel)]<br>
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentView extends CenterPanel {

	/**
	 * Possible statuses of a controller.
	 */
	enum ControllerStatus {
		Offline, Online, Unknown, OnlineLimited, Checking, InvalidURL
	}

	/** Image-URL of the sliderImage, when the controllerPanel is hidden. */
	static final String SLIDER_PANEL_IMG_HIDDEN = "images/down.png";
	/** Image-URL of the sliderImage, when the controllerPanel is visible. */
	static final String SLIDER_PANEL_IMG_VISIBLE = "images/up.png";
	/** The full height (in px) of the controllerPanel. */
	// static final int MECPANEL_HEIGHT = 168;

	private static final String SLIDER_PANEL_ID = "mecSliderPanel";

	private ClickPanel sliderPanel;
	private FlowPanel controllerPanel, contentWrapper;
	private EnvironmentDefinitonTreePanel environmentDefinitionTreePanel;
	private HTML htmlStatus;

	private boolean isControllerPanelVisible = false;

	//

	private Image sliderImage;
	private HTML sliderText, statusText;
	private RadioButton radioDirectInput, radioFromController;
//	private ListBox mecDropDown;
//	private TextBox mecTextBox;
	private Button checkStatusBtn, getMEButton;
//	private boolean checkAndGet = false;

	private ComboBox meControllerCombobox;

	public EnvironmentView() {
		initialize();
	}

	/**
	 * Initialize this widget and all necessary elements.
	 */
	private void initialize() {
		add(getControllerPanel());
		add(getSliderPanel());
		add(getEnvironmentDefinitonTreePanel());
	}

	/**
	 * Returns the environmentDefinitionTreePanel. If it doesn't exist, it will
	 * be created.
	 * 
	 * @return environmentDefinitionTreePanel
	 */
	public EnvironmentDefinitonTreePanel getEnvironmentDefinitonTreePanel() {
		if (environmentDefinitionTreePanel == null) {
			environmentDefinitionTreePanel = new EnvironmentDefinitonTreePanel();
		}

		return environmentDefinitionTreePanel;
	}

	FlowPanel getContentWrapper() {
		return contentWrapper;
	}

	/**
	 * Returns the sliderPanel to show and hide the controllerPanel. If the
	 * sliderPanel doesn't exist, it will be created.
	 * 
	 * @return
	 */
	ClickPanel getSliderPanel() {
		if (sliderPanel != null) {
			return sliderPanel;
		}

		sliderPanel = new ClickPanel();
		sliderPanel.getElement().setId(SLIDER_PANEL_ID);

		FlowPanel innerSliderPanel = new FlowPanel();

		sliderText = new HTML(R.get("getMEfromMEC"));
		sliderText.addStyleName("slideText");
		innerSliderPanel.add(sliderText);

		sliderImage = new Image(SLIDER_PANEL_IMG_HIDDEN);
		innerSliderPanel.add(sliderImage);

		sliderPanel.add(innerSliderPanel);

		return sliderPanel;
	}

	/**
	 * Returns whether the controllerPanel is visible.
	 * 
	 * @return
	 */
	boolean isControllerPanelVisible() {
		return isControllerPanelVisible;
	}

	/**
	 * Setting the text on the sliderPanel.
	 * 
	 * @param newText
	 */
	void setSliderPanelText(String newText) {
		sliderText.setText(newText);
	}

	/**
	 * Setting the URL of the image on the sliderPanel.
	 * 
	 * @param imageURL
	 */
	void setSliderImageURL(String imageURL) {
		sliderImage.setUrl(imageURL);
	}

	/**
	 * Returns the "Get Environment" Button.
	 * 
	 * @return getMEButton
	 */
	Button getGetEnvironmentButton() {
		return getMEButton;
	}

	/**
	 * Returns the controllerPanel. If it doesn't exist, it will be created.
	 * 
	 * @return
	 */
	FlowPanel getControllerPanel() {
		if (controllerPanel != null) {
			return controllerPanel;
		}

		controllerPanel = new FlowPanel();
		controllerPanel.getElement().setId("controllerPanel");
		controllerPanel.setHeight("0px");

		// Wrapper for padding
		contentWrapper = new FlowPanel();
		contentWrapper.getElement().setId("contentWrapper");

		// Headline
		HTML headline = new HTML(R.get("selectMESource"));
		headline.addStyleName("headline");
		contentWrapper.add(headline);

		// Table
		FlexTable table = new FlexTable();
		table.getElement().setId("mecGrid");
		table.setCellSpacing(8);

		// Labels
		Label labelFromController = new Label(R.get("fromController") + ":");
		Label labelDirectInput = new Label(R.get("directInput") + ":");
		Label labelControllerStatus = new Label(R.get("controllerStatus") + ":");
		Label labelInsertOwnME = new Label(R.get("insertME"));
		htmlStatus = new HTML(R.get("unknown"));

		labelFromController.addStyleName("bold");
		labelDirectInput.addStyleName("bold");
		labelControllerStatus.addStyleName("bold");

		// RadioButtons
		radioFromController = new RadioButton("meSource");
		radioDirectInput = new RadioButton("meSource");

		radioFromController.setValue(true);

		// Combobox
		meControllerCombobox = new ComboBox();
		meControllerCombobox.setWidth(500);

		// Button
		getMEButton = new Button(R.get("getEnv"));
		getMEButton.setEnabled(false);

		// Adding all widgets to the table
		table.setWidget(0, 0, labelFromController);
		table.setWidget(2, 0, labelDirectInput);
		table.setWidget(1, 2, labelControllerStatus);
		table.setWidget(2, 2, labelInsertOwnME);
		table.setWidget(1, 3, htmlStatus);

		table.setWidget(0, 1, radioFromController);
		table.setWidget(2, 1, radioDirectInput);

		table.setWidget(0, 2, meControllerCombobox);

		table.setWidget(1, 4, getMEButton);

		table.getFlexCellFormatter().setColSpan(0, 2, 3);
		table.getFlexCellFormatter().setColSpan(2, 2, 3);

		table.getCellFormatter().setWidth(0, 0, "110");
		table.getCellFormatter().setWidth(1, 2, "125");

		contentWrapper.add(table);

		// selectHTML.setWidth("120px");

		// **************
		// First Row - DropDown Box

		HorizontalPanel firstRow = new HorizontalPanel();
		firstRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		// radioFromController = new RadioButton("urlSelection");
		radioFromController.setValue(true);
		radioFromController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				mecTextBox.removeStyleName("invalid");
				checkStatusBtn.setEnabled(true);
			}
		});

		// mecDropDown = new ListBox();
		// mecDropDown.setVisibleItemCount(1);
		// mecDropDown.addChangeHandler(new ChangeHandler() {
		// @Override
		// public void onChange(ChangeEvent event) {
		// radioDropDown.setValue(true);
		// radioTextField.setValue(false);
		// mecTextBox.removeStyleName("invalid");
		// resetStatus();
		//
		// runControllerCheck();
		// }
		// });

		// meControllerCombobox = new ComboBox();
		// meControllerCombobox.setWidth(400);

		// firstRow.add(radioFromController);
		// firstRow.add(meControllerCombobox);

		// firstRow.setCellWidth(radioFromController, "30");

		// grid.setWidget(0, 1, firstRow);

		// **************
		// Second Row - Textfield

		HorizontalPanel secondRow = new HorizontalPanel();
		secondRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		// radioTextField = new RadioButton("urlSelection");
		radioDirectInput.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					// checkTextfieldInput();
				}
			}
		});

		// **************

		controllerPanel.add(contentWrapper);

		return controllerPanel;
	}

	/**
	 * Returns the controller-combobox.
	 * 
	 * @return the combobox
	 */
	public ComboBox getControllerComboBox() {
		return meControllerCombobox;
	}

	/**
	 * Sets the html element, which contains the status text of the controller.
	 * 
	 * @param html
	 */
	public void setControllerStatus(ControllerStatus status) {
		htmlStatus.setStyleName("");

		switch (status) {
		case Online:
			htmlStatus.addStyleName("positive");
			htmlStatus.setHTML(R.get("online"));
			break;
		case OnlineLimited:
			htmlStatus.addStyleName("warning");
			htmlStatus.setHTML(R.get("controllerHasNoInfos"));
			break;
		case Offline:
			htmlStatus.addStyleName("negative");
			htmlStatus.setHTML(R.get("offline"));
			break;
		case Checking:
			String img = "<img class=\"loadingIndicator\" src=\"images/loader_circle.gif\" />";
			htmlStatus.setHTML(img + R.get("checking"));
			break;
		case InvalidURL:
			htmlStatus.addStyleName("warning");
			htmlStatus.setHTML(R.get("invalidUrl"));
			break;
		case Unknown:
		default:
			htmlStatus.setHTML(R.get("unknown"));
		}
	}

	/**
	 * Enables or disables the "Get Environment" Button.
	 * 
	 * @param status
	 */
	public void setGetEnvironmentButtonStatus(boolean status) {
		if (status) {
			getMEButton.setEnabled(true);
		} else {
			getMEButton.setEnabled(false);
		}
	}

	// private void updateMECList(final boolean setRBtoTB) {
	// // Loader.showLoader();
	//
	// RPC.getMEControllerRPC().getMEControllerList(new
	// AsyncCallback<List<String>>() {
	// @Override
	// public void onFailure(Throwable caught) {
	// // Loader.hideLoader();
	// Message.error(caught.getMessage());
	// }
	//
	// @Override
	// public void onSuccess(List<String> result) {
	// if (result.isEmpty()) {
	// // mecDropDown.setEnabled(false);
	// // radioFromController.setEnabled(false);
	// // radioFromController.setValue(false);
	// // radioDirectInput.setValue(true);
	//
	// // if (mecTextBox.getText().isEmpty()) {
	// // checkStatusBtn.setEnabled(false);
	// // }
	// } else {
	// mecDropDown.setEnabled(true);
	// radioFromController.setEnabled(true);
	//
	// if (setRBtoTB) {
	// radioFromController.setValue(true);
	// radioDirectInput.setValue(false);
	// }
	//
	// mecDropDown.clear();
	// for (String mec : result) {
	// mecDropDown.addItem(mec);
	// }
	// }
	//
	// runControllerCheck();
	// }
	// });
	// }

//	public void addMEControllerUrl(String url) {
//		mecDropDown.addItem(url);
//		mecDropDown.setEnabled(true);
//		radioFromController.setEnabled(true);
//	}
}
