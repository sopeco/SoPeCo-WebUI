package org.sopeco.frontend.client.layout.center;

import java.util.List;

import org.sopeco.frontend.client.animation.ICompleteHandler;
import org.sopeco.frontend.client.animation.SlideDown;
import org.sopeco.frontend.client.animation.SlideUp;
import org.sopeco.frontend.client.layout.ClickPanel;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.rpc.MEControllerRPC;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

	private static final int MECPANEL_HEIGHT = 231;
	private boolean mecPanelIsDown = false;
	private Image slideImage;
	private HTML slideText, statusText;
	private RadioButton radioTextField, radioDropDown;
	private ListBox mecDropDown;
	private TextBox mecTextBox;
	private Button checkStatusBtn;

	public EnvironmentPanel() {
		initialize();
	}

	private void initialize() {
		mecSlider = createMECSlider();
		mecPanel = createMECPanel();

		add(mecPanel);
		add(mecSlider);

		updateMECList(true);
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
					mecPanelIsDown = false;
					SlideUp.start(mecPanel, new ICompleteHandler() {
						@Override
						public void onComplete() {
							DOM.getElementById("mecSliderPanel").removeClassName("down");
						}
					});

					slideImage.setUrl("images/down.png");
					slideText.setHTML(R.get("getMEfromMEC"));
				} else {
					mecPanelIsDown = true;
					SlideDown.start(mecPanel, MECPANEL_HEIGHT);
					slideImage.setUrl("images/up.png");
					slideText.setHTML(R.get("hideThisArea"));
					DOM.getElementById("mecSliderPanel").addClassName("down");
				}
			}
		});
		return retPanel;
	}

	private void slideMEPanelUp () {
		
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
		mecDropDown = new ListBox();
		mecDropDown.setVisibleItemCount(1);

		firstRow.add(radioDropDown);
		firstRow.add(mecDropDown);

		firstRow.setCellWidth(radioDropDown, "30");

		grid.setWidget(0, 1, firstRow);

		// **************
		// Second Row - Textfield

		HorizontalPanel secondRow = new HorizontalPanel();
		secondRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		radioTextField = new RadioButton("urlSelection");
		mecTextBox = new TextBox();
		mecTextBox.addKeyPressHandler(getTextFieldHandler());

		secondRow.add(radioTextField);
		secondRow.add(mecTextBox);

		secondRow.setCellWidth(radioTextField, "30");

		grid.setWidget(1, 1, secondRow);

		// **************
		// Third Row - Status Label and Check Button

		HorizontalPanel statusPanel = new HorizontalPanel();
		statusPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		statusText = new HTML(R.get("unknown"));
		statusText.addStyleName("statusText");
		statusPanel.add(statusText);
		statusPanel.getElement().getStyle().setMarginLeft(30, Unit.PX);
		statusPanel.setCellWidth(statusText, "120");

		checkStatusBtn = new Button(R.get("check"));
		checkStatusBtn.addClickHandler(getCheckButtonHandler());
		statusPanel.add(checkStatusBtn);

		grid.setWidget(2, 1, statusPanel);

		// **************
		// Footer - Get Button

		HorizontalPanel actionPanel = new HorizontalPanel();
		actionPanel.setWidth("100%");
		actionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		actionPanel.addStyleName("actionline");
		actionPanel.addStyleName("headline");

		HTML overwriteHTML = new HTML(R.get("overwriteSettings"));

		Button getME = new Button(R.get("getME"));
		getME.addClickHandler(getGMEButtonHandler());
		
		actionPanel.add(overwriteHTML);
		actionPanel.add(getME);

		actionPanel.setCellWidth(overwriteHTML, "300");

		// **************

		innerPanel.add(grid);
		innerPanel.add(actionPanel);

		panel.add(innerPanel);

		return panel;
	}

	private ClickHandler getCheckButtonHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String image = "<img src=\"images/loader_circle.gif\" />";
				statusText.setHTML(image + R.get("checking") + "..");
				statusText.removeStyleName("positive");
				statusText.removeStyleName("negative");

				checkStatusBtn.setEnabled(false);

				String url = "";
				if (radioDropDown.getValue()) {
					url = mecDropDown.getItemText(mecDropDown.getSelectedIndex());
				} else {
					url = mecTextBox.getText();
				}

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
						} else if (result == MEControllerRPC.STATUS_OFFLINE) {
							text = R.get("offline");
							textClass = "negative";
						} else {
							text = R.get("unknown");
						}

						statusText.setHTML(text);
						if (!textClass.isEmpty()) {
							statusText.addStyleName(textClass);
						}

						checkStatusBtn.setEnabled(true);
					}

					@Override
					public void onFailure(Throwable caught) {
						checkStatusBtn.setEnabled(true);
						Message.error(caught.getMessage());
					}
				});
			}
		};
	}

	private KeyPressHandler getTextFieldHandler() {
		return new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				radioDropDown.setValue(false);
				radioTextField.setValue(true);
			}
		};
	}

	private ClickHandler getGMEButtonHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Message.warning("btn clicked");
			}
		};
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
				// Loader.hideLoader();
			}
		});
	}

	public void addMEControllerUrl(String url) {
		mecDropDown.addItem(url);
		mecDropDown.setEnabled(true);
		radioDropDown.setEnabled(true);
	}
}
