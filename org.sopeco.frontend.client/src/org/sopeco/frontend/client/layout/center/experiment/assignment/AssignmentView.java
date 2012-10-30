package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.layout.center.experiment.ParameterTreeController;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentView extends FlowPanel implements ClickHandler {
	private static final String EXP_PREPERATION_PANEL_ID = "expPreperationPanel";

	private Element headline;
	private Image editImage;

	public AssignmentView(String headlineText) {
		initialize(headlineText);
	}

	/**
	 * Inits all objects.
	 */
	private void initialize(String headlineText) {
		getElement().setId(EXP_PREPERATION_PANEL_ID);

		editImage = new Image("images/pencil.png");
		editImage.setHeight("18px");
		editImage.setWidth("18px");
		editImage.getElement().getStyle().setPosition(Position.ABSOLUTE);
		editImage.getElement().getStyle().setLeft(220, Unit.PX);
		editImage.getElement().getStyle().setTop(16, Unit.PX);
		editImage.getElement().getStyle().setCursor(Cursor.POINTER);
		editImage.addClickHandler(this);

		headline = DOM.createElement("h3");
		headline.setInnerHTML(headlineText);
		getElement().appendChild(headline);

		add(editImage);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (!ParameterTreeController.treeController.showView()) {
			ParameterTreeController.treeController.hideView();
		}
	}
}
