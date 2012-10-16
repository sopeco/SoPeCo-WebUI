package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationView extends FlowPanel {

	private HorizontalPanel topPanel;
	private TextBox textboxName;
	private HorizontalPanel bottomPanel;
	private Widget selectionView, assignmentListPanel;

	private static final String TOP_PANEL_ID = "specificationTopPanel";
	private static final String TOP_PANEL_HEIGHT = "60";
	private static final int SELECTION_PANEL_WIDTH = 400;

	public SpecificationView(Widget assignmentView, Widget sView) {
		assignmentListPanel = assignmentView;
		selectionView = sView;

		initialize();
	}

	/**
	 * Initializing the widgets.
	 */
	private void initialize() {
		setWidth("100%");
		setHeight("100%");

		topPanel = new HorizontalPanel();
		topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		topPanel.getElement().setId(TOP_PANEL_ID);
		topPanel.setHeight(TOP_PANEL_HEIGHT + "px");

		Label nameLabel = new Label(R.get("name") + ":");
		nameLabel.addStyleName("spc-Label");

		textboxName = new TextBox();
		textboxName.addStyleName("spc-TextBox");

		topPanel.add(nameLabel);
		topPanel.add(textboxName);

		topPanel.setCellWidth(nameLabel, "100");

		// bottomPanel = new HorizontalPanel();
		// bottomPanel.getElement().getStyle().setWidth(100, Unit.PCT);

		selectionView.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);
		selectionView.getElement().getStyle().setPosition(Position.ABSOLUTE);
		selectionView.getElement().getStyle().setRight(0, Unit.PX);
		selectionView.getElement().getStyle().setWidth(SELECTION_PANEL_WIDTH, Unit.PX);

		assignmentListPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		assignmentListPanel.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);
		assignmentListPanel.getElement().getStyle().setRight(SELECTION_PANEL_WIDTH, Unit.PX);

		// ADDING
		// bottomPanel.add(assignmentListPanel);
		// bottomPanel.add(selectionView);

		add(topPanel);
		add(assignmentListPanel);
		add(selectionView);

		// setCellHeight(topPanel, TOP_PANEL_HEIGHT);
	}

	/**
	 * Setting the given string to the specificationName-Textbox.
	 * 
	 * @param name
	 */
	public void setSpecificationName(String name) {
		textboxName.setText(name);
	}

	/**
	 * Returns the textbox of the specification name.
	 * 
	 * @return
	 */
	public TextBox getSpecificationNameTextbox() {
		return textboxName;
	}
}
