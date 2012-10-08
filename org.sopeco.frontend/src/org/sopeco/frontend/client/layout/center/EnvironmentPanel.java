package org.sopeco.frontend.client.layout.center;

import java.util.List;

import org.sopeco.frontend.client.animation.ICompleteHandler;
import org.sopeco.frontend.client.animation.SlideDown;
import org.sopeco.frontend.client.animation.SlideUp;
import org.sopeco.frontend.client.layout.center.environment.EnvironmentDefinitonTreePanel;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.client.widget.ClickPanel;
import org.sopeco.frontend.shared.rsc.R;
import org.sopeco.persistence.entities.definition.MeasurementEnvironmentDefinition;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentPanel extends CenterPanel {
	private ClickPanel mecSlider;
	private FlowPanel mecPanel;
	private EnvironmentDefinitonTreePanel edTreePanel;

	private static final int MECPANEL_HEIGHT = 231;
	private boolean mecPanelIsDown = false;
	private Image slideImage;
	private HTML slideText, statusText;
	private RadioButton radioTextField, radioDropDown;
	private ListBox mecDropDown;
	private TextBox mecTextBox;
	private Button checkStatusBtn, getMEButton;
	private boolean checkAndGet = false;

	private String[] validUrlPatterns;

	public EnvironmentPanel() {
		initialize();
	}

	private void initialize() {
		mecSlider = createMECSlider();
		mecPanel = createMECPanel();
		edTreePanel = new EnvironmentDefinitonTreePanel();

		add(mecPanel);
		add(mecSlider);
		add(edTreePanel);

		updateMECList(true);

		getValidUrlPattern();
	}

	public EnvironmentDefinitonTreePanel getEnvironmentDefinitonTreePanel() {
		return edTreePanel;
	}

	/**
	 * Gets the url patterns for the textbox.
	 */
	private void getValidUrlPattern() {
		RPC.getMEControllerRPC().getValidUrlPattern(new AsyncCallback<String[]>() {
			@Override
			public void onFailure(Throwable caught) {
				Message.error("Can't get valid url patterns.");
			}

			@Override
			public void onSuccess(String[] result) {
				validUrlPatterns = result;
			}
		});
	}

	/**
	 * Creates the Slide to Show and Hide the MECPanel.
	 * 
	 * @return
	 */
	private ClickPanel createMECSlider() {
		FlowPanel panel = new FlowPanel();
		panel.getElement().setId("mecSliderPanel");

		slideText = new HTML(R.get("getMEfromMEC"));
		slideText.addStyleName("slideText");
		panel.add(slideText);

		slideImage = new Image("images/down.png");
		panel.add(slideImage);

		ClickPanel retPanel = new ClickPanel();
		retPanel.add(panel);

		retPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (mecPanelIsDown) {
					slideMEPanelUp();
				} else {
					slideMEPanelDown();
				}
			}
		});
		return retPanel;
	}

	/**
	 * Hides the "getMEC"-Panel.
	 */
	public void slideMEPanelUp() {
		if (!mecPanelIsDown) {
			return;
		}

		mecPanelIsDown = false;
		SlideUp.start(mecPanel, new ICompleteHandler() {
			@Override
			public void onComplete() {
				DOM.getElementById("mecSliderPanel").removeClassName("down");
			}
		});

		slideImage.setUrl("images/down.png");
		slideText.setHTML(R.get("getMEfromMEC"));
	}

	/**
	 * Shows the "getMEC"-Panel.
	 */
	public void slideMEPanelDown() {
		if (mecPanelIsDown) {
			return;
		}

		mecPanelIsDown = true;
		SlideDown.start(mecPanel, MECPANEL_HEIGHT);
		slideImage.setUrl("images/up.png");
		slideText.setHTML(R.get("hideThisArea"));
		DOM.getElementById("mecSliderPanel").addClassName("down");
	}

	/**
	 * Creates the panel, which contains the setting-elements.
	 * 
	 * @return
	 */
	private FlowPanel createMECPanel() {
		FlowPanel panel = new FlowPanel();
		panel.getElement().setId("mecPanel");
		panel.setHeight("0px");

		FlowPanel innerPanel = new FlowPanel();
		innerPanel.getElement().getStyle().setPadding(1, Unit.EM);

		// Headline
		HTML headline = new HTML(R.get("getMEfromMEC"));
		headline.addStyleName("headline");
		innerPanel.add(headline);

		// Grid
		Grid grid = new Grid(3, 2);
		grid.getElement().setId("mecGrid");
		grid.setCellSpacing(8);

		// Grid Labels
		HTML selectHTML = new HTML(R.get("mecSelect") + ":");
		HTML statusHTML = new HTML(R.get("mecStatus") + ":");

		selectHTML.addStyleName("description");
		statusHTML.addStyleName("description");

		selectHTML.setWidth("120px");

		grid.setWidget(0, 0, selectHTML);
		grid.setWidget(2, 0, statusHTML);

		// **************
		// First Row - DropDown Box

		HorizontalPanel firstRow = new HorizontalPanel();
		firstRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		radioDropDown = new RadioButton("urlSelection");
		radioDropDown.setValue(true);
		radioDropDown.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				mecTextBox.removeStyleName("invalid");
				checkStatusBtn.setEnabled(true);
			}
		});

		mecDropDown = new ListBox();
		mecDropDown.setVisibleItemCount(1);
		mecDropDown.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				radioDropDown.setValue(true);
				radioTextField.setValue(false);
				mecTextBox.removeStyleName("invalid");
				resetStatus();

				runControllerCheck();
			}
		});

		firstRow.add(radioDropDown);
		firstRow.add(mecDropDown);

		firstRow.setCellWidth(radioDropDown, "30");

		grid.setWidget(0, 1, firstRow);

		// **************
		// Second Row - Textfield

		HorizontalPanel secondRow = new HorizontalPanel();
		secondRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		radioTextField = new RadioButton("urlSelection");
		radioTextField.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					checkTextfieldInput();
				}
			}
		});

		mecTextBox = new TextBox();
		mecTextBox.addKeyPressHandler(getTextFieldPressHandler());
		mecTextBox.addKeyUpHandler(getTextFieldUpHandler());
		mecTextBox.addChangeHandler(getTextFieldChangeHandler());

		secondRow.add(radioTextField);
		secondRow.add(mecTextBox);

		secondRow.setCellWidth(radioTextField, "30");

		grid.setWidget(1, 1, secondRow);

		// **************
		// Third Row - Status Label and Check Button

		HorizontalPanel statusPanel = new HorizontalPanel();
		statusPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		checkStatusBtn = new Button(R.get("check"));
		checkStatusBtn.addClickHandler(getCheckButtonHandler());
		statusPanel.add(checkStatusBtn);
		statusPanel.setCellWidth(checkStatusBtn, "120");

		statusText = new HTML(R.get("unknown"));
		statusText.addStyleName("statusText");
		statusPanel.add(statusText);
		statusPanel.getElement().getStyle().setMarginLeft(30, Unit.PX);
		statusPanel.setCellWidth(statusText, "250");

		grid.setWidget(2, 1, statusPanel);

		// **************
		// Footer - Get Button

		HorizontalPanel actionPanel = new HorizontalPanel();
		actionPanel.setWidth("100%");
		actionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		actionPanel.addStyleName("actionline");
		actionPanel.addStyleName("headline");

		HTML overwriteHTML = new HTML(R.get("overwriteSettings"));

		getMEButton = new Button(R.get("getME"));
		getMEButton.addClickHandler(getGMEButtonHandler());
		getMEButton.setEnabled(false);

		actionPanel.add(overwriteHTML);
		actionPanel.add(getMEButton);

		actionPanel.setCellWidth(overwriteHTML, "300");

		// **************

		innerPanel.add(grid);
		innerPanel.add(actionPanel);

		panel.add(innerPanel);

		return panel;
	}

	private String getControllerUrl() {
		String url = "";
		if (radioDropDown.getValue()) {
			url = mecDropDown.getItemText(mecDropDown.getSelectedIndex());
		} else {
			url = mecTextBox.getText();
		}
		return url;
	}

	private ClickHandler getCheckButtonHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				runControllerCheck();
			}
		};
	}

	private void runControllerCheck() {
		String image = "<img src=\"images/loader_circle.gif\" />";
		statusText.setHTML(image + R.get("checking") + "..");

		clearStatusText();

		getMEButton.setEnabled(false);

		checkStatusBtn.setEnabled(false);

		String url = getControllerUrl();

		if (url.isEmpty()) {
			checkStatusBtn.setEnabled(true);
			statusText.setHTML(R.get("unknown"));
			return;
		}

		RPC.getMEControllerRPC().checkControllerStatus(url, new AsyncCallback<Integer>() {
			@Override
			public void onSuccess(Integer result) {
				String text = "";
				String textClass = "";
				if (result == MEControllerRPC.STATUS_ONLINE) {
					text = R.get("online");
					textClass = "positive";
					setGetMEButtonStatus(true);

					if (checkAndGet) {
						getMEfromController();
					}
				} else if (result == MEControllerRPC.STATUS_OFFLINE) {
					text = R.get("offline");
					textClass = "negative";
					setGetMEButtonStatus(false);
				} else if (result == MEControllerRPC.NO_VALID_MEC_URL) {
					text = R.get("invalidUrl");
					textClass = "negative";
					setGetMEButtonStatus(false);
				} else if (result == MEControllerRPC.STATUS_ONLINE_NO_META) {
					text = R.get("controllerHasNoInfos");
					textClass = "warning";
					setGetMEButtonStatus(false);
				} else {
					text = R.get("unknown");
					setGetMEButtonStatus(false);
				}

				statusText.setHTML(text);
				if (!textClass.isEmpty()) {
					statusText.addStyleName(textClass);
				}

				checkStatusBtn.setEnabled(true);

				checkAndGet = false;

			}

			@Override
			public void onFailure(Throwable caught) {
				checkStatusBtn.setEnabled(true);
				setGetMEButtonStatus(false);
				Message.error(caught.getMessage());

				checkAndGet = false;
			}
		});
	}

	private void setGetMEButtonStatus(boolean status) {
		if (status) {
			getMEButton.setEnabled(true);
		} else {
			getMEButton.setEnabled(false);
		}
	}

	private KeyPressHandler getTextFieldPressHandler() {
		return new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				radioDropDown.setValue(false);
				radioTextField.setValue(true);
			}
		};
	}

	private KeyUpHandler getTextFieldUpHandler() {
		return new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				radioDropDown.setValue(false);
				radioTextField.setValue(true);

				checkTextfieldInput();
			}
		};
	}

	private ChangeHandler getTextFieldChangeHandler() {
		return new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				radioDropDown.setValue(false);
				radioTextField.setValue(true);

				checkTextfieldInput();

				if (checkStatusBtn.isEnabled()) {
					runControllerCheck();
				}
			}
		};
	}

	private void checkTextfieldInput() {
		if (validUrlPatterns == null) {
			return;
		}

		boolean matched = false;

		for (String pattern : validUrlPatterns) {
			if (mecTextBox.getText().matches(pattern)) {
				matched = true;
				break;
			}
		}

		if (matched) {
			checkStatusBtn.setEnabled(true);
			mecTextBox.removeStyleName("invalid");
		} else {
			checkStatusBtn.setEnabled(false);
			mecTextBox.addStyleName("invalid");
		}
	}

	private ClickHandler getGMEButtonHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				checkAndGet = true;
				runControllerCheck();
			}
		};
	}

	private void getMEfromController() {
		Loader.showLoader();

		RPC.getMEControllerRPC().getMEDefinitionFromMEC(getControllerUrl(),
				new AsyncCallback<MeasurementEnvironmentDefinition>() {
					@Override
					public void onSuccess(MeasurementEnvironmentDefinition result) {
						Loader.hideLoader();
						slideMEPanelUp();

						edTreePanel.setEnvironmentDefiniton(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						Loader.hideLoader();
						Message.error(caught.getMessage());
					}
				});
	}

	private void resetStatus() {
		getMEButton.setEnabled(false);
		checkStatusBtn.setEnabled(true);
		statusText.setText(R.get("unknown"));

		clearStatusText();
	}

	private void clearStatusText() {
		statusText.removeStyleName("positive");
		statusText.removeStyleName("negative");
		statusText.removeStyleName("warning");
	}

	private void updateMECList(final boolean setRBtoTB) {
		// Loader.showLoader();

		RPC.getMEControllerRPC().getMEControllerList(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				// Loader.hideLoader();
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				if (result.isEmpty()) {
					mecDropDown.setEnabled(false);
					radioDropDown.setEnabled(false);
					radioDropDown.setValue(false);
					radioTextField.setValue(true);

					if (mecTextBox.getText().isEmpty()) {
						checkStatusBtn.setEnabled(false);
					}
				} else {
					mecDropDown.setEnabled(true);
					radioDropDown.setEnabled(true);

					if (setRBtoTB) {
						radioDropDown.setValue(true);
						radioTextField.setValue(false);
					}

					mecDropDown.clear();
					for (String mec : result) {
						mecDropDown.addItem(mec);
					}
				}

				runControllerCheck();
			}
		});
	}

	public void addMEControllerUrl(String url) {
		mecDropDown.addItem(url);
		mecDropDown.setEnabled(true);
		radioDropDown.setEnabled(true);
	}
}
