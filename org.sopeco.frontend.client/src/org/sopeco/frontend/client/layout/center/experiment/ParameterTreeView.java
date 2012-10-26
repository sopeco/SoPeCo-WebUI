package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

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
public class ParameterTreeView extends FlowPanel implements ClickHandler {

	private static final String PARAMETER_TREE_ID = "expParameterTree";

	private Image closeImage;

	public ParameterTreeView() {
		initialize();
	}

	/**
	 * Inits all objects.
	 */
	private void initialize() {
		getElement().setId(PARAMETER_TREE_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("envParameter"));

		closeImage = new Image("images/close.png");
		closeImage.addClickHandler(this);
		closeImage.getElement().getStyle().setPosition(Position.ABSOLUTE);
		closeImage.getElement().getStyle().setRight(10, Unit.PX);
		closeImage.getElement().getStyle().setTop(14, Unit.PX);
		closeImage.getElement().getStyle().setCursor(Cursor.POINTER);
		closeImage.getElement().getStyle().setOpacity(0.5);

		getElement().appendChild(headline);
		add(closeImage);

		setVisible(false);
	}

	@Override
	public void onClick(ClickEvent event) {
		ParameterTreeController.treeController.hideView();
	}
}
