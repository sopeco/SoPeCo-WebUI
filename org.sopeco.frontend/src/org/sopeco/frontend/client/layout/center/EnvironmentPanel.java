package org.sopeco.frontend.client.layout.center;

import org.sopeco.frontend.client.animation.ICompleteHandler;
import org.sopeco.frontend.client.animation.SlideDown;
import org.sopeco.frontend.client.animation.SlideUp;
import org.sopeco.frontend.client.layout.ClickPanel;
import org.sopeco.frontend.shared.rsc.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
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

	private static final int MECPANEL_HEIGHT = 224;
	private boolean mecPanelIsDown = false;
	private Image slideImage;
	private HTML slideText;

	public EnvironmentPanel() {
		initialize();
	}

	private void initialize() {
		mecSlider = createMECSlider();
		mecPanel = createMECPanel();

		mecSlider.addClickHandler(new ClickHandler() {
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

		add(mecPanel);
		add(mecSlider);
	}

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
		return retPanel;
	}

	private FlowPanel createMECPanel() {
		FlowPanel panel = new FlowPanel();
		panel.getElement().setId("mecPanel");
		panel.setHeight("0px");

		FlowPanel innerPanel = new FlowPanel();
		innerPanel.getElement().getStyle().setPadding(1, Unit.EM);

		HTML headline = new HTML(R.get("getMEfromMEC"));
		headline.addStyleName("headline");
		innerPanel.add(headline);

		HTML selectHTML = new HTML("Select MEC:");
		selectHTML.addStyleName("description");
		HTML statusHTML = new HTML("MEC Status:");
		statusHTML.addStyleName("description");

		Grid grid = new Grid(3, 2);
		grid.getElement().setId("mecGrid");
		grid.setCellSpacing(8);
		grid.setWidget(0, 0, selectHTML);
		grid.setWidget(2, 0, statusHTML);

		// **************

		HorizontalPanel firstRow = new HorizontalPanel();
		firstRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		RadioButton ddRadioButton = new RadioButton("tst");
		ListBox dropDown = new ListBox();
		dropDown.setVisibleItemCount(1);

		firstRow.add(ddRadioButton);
		firstRow.add(dropDown);

		firstRow.setCellWidth(ddRadioButton, "30");

		grid.setWidget(0, 1, firstRow);

		// **************

		HorizontalPanel secondRow = new HorizontalPanel();
		secondRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		RadioButton tbRadioButton = new RadioButton("tst");
		TextBox urlBox = new TextBox();

		secondRow.add(tbRadioButton);
		secondRow.add(urlBox);

		secondRow.setCellWidth(tbRadioButton, "30");

		grid.setWidget(1, 1, secondRow);

		// **************

		HorizontalPanel statusPanel = new HorizontalPanel();
		statusPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		HTML statusText = new HTML("unknown");
		statusPanel.add(statusText);
		statusPanel.setCellWidth(statusText, "80");

		Button checkStatusBtn = new Button("Check");
		statusPanel.add(checkStatusBtn);

		grid.setWidget(2, 1, statusPanel);

		// **************

		HorizontalPanel actionPanel = new HorizontalPanel();
		actionPanel.setWidth("100%");
		actionPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		actionPanel.addStyleName("actionline");
		actionPanel.addStyleName("headline");

		HTML overwriteHTML = new HTML("Current settings will be overwritten!");

		Button getME = new Button("Get MeasurementEnvironment");

		actionPanel.add(overwriteHTML);
		actionPanel.add(getME);

		actionPanel.setCellWidth(overwriteHTML, "300");

		// **************

		innerPanel.add(grid);
		innerPanel.add(actionPanel);

		panel.add(innerPanel);

		return panel;
	}
}
