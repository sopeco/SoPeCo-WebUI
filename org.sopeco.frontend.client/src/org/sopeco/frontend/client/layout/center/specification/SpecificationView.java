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
	private Widget selectionView, assignmentListPanel;
	private int selectionPanelPosition;
	private boolean selectionPanelIsVisible;

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

		selectionView.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);
		selectionView.getElement().getStyle().setPosition(Position.ABSOLUTE);
		selectionView.getElement().getStyle().setWidth(SELECTION_PANEL_WIDTH, Unit.PX);

		assignmentListPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		assignmentListPanel.getElement().getStyle().setTop(Double.parseDouble(TOP_PANEL_HEIGHT), Unit.PX);

		setSelectionPanelPosition(1);
		selectionPanelIsVisible = false;

		// ADDING
		add(topPanel);
		add(assignmentListPanel);
		add(selectionView);
	}

	/**
	 * Set the position of the selection panel and of all related elements like
	 * the toggleSelectionElement and the assignmentListPanel. parameter is a
	 * percent value between 0 (0%) and 1 (100%).
	 * 
	 * @param x
	 */
	private void setSelectionPanelPosition(float x) {
		x = Math.max(0F, Math.min(1F, x));
		selectionPanelPosition = (int) (SELECTION_PANEL_WIDTH * x);

		selectionView.getElement().getStyle().setLeft(selectionPanelPosition - SELECTION_PANEL_WIDTH, Unit.PX);
		assignmentListPanel.getElement().getStyle().setLeft(selectionPanelPosition, Unit.PX);
	}

	/**
	 * Set whether the selectionPanel is visible.
	 * 
	 * @param visible
	 */
	public void setSelectionPanelVisible(boolean visible) {
		selectionPanelIsVisible = visible;
		if (visible) {
			setSelectionPanelPosition(1F);
		} else {
			setSelectionPanelPosition(0);
		}
	}

	/**
	 * @return the selectionPanelVisible
	 */
	public boolean isSelectionPanelVisible() {
		return selectionPanelIsVisible;
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
