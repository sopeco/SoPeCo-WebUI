package org.sopeco.frontend.client.layout.center;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EmptyCenterPanel extends CenterPanel {
	private static final int IMG_SIZE_X = 640, IMG_SIZE_Y = 323;
	private static final int FIFTY = 50;
	private static final double OPACITY = 0.1;
	private static Image logo;

	public EmptyCenterPanel() {
		if (logo == null) {
			logo = new Image("images/sap-logo.jpg");
			logo.setSize(IMG_SIZE_X + "px", IMG_SIZE_Y + "px");
			logo.getElement().getStyle().setPosition(Position.ABSOLUTE);
			logo.getElement().getStyle().setTop(FIFTY, Unit.PCT);
			logo.getElement().getStyle().setLeft(FIFTY, Unit.PCT);
			logo.getElement().getStyle().setMarginTop(-IMG_SIZE_Y / 2, Unit.PX);
			logo.getElement().getStyle().setMarginLeft(-IMG_SIZE_X / 2, Unit.PX);
			logo.getElement().getStyle().setOpacity(OPACITY);
		}
		add(logo);
	}
}
