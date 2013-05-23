package org.sopeco.gwt.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ImageHover extends Image implements MouseOverHandler,
		MouseOutHandler, ClickHandler {

	private ImageResource defaultImage, hoverImage;

	public ImageHover(ImageResource pDefaultImage, ImageResource pHoverImage) {
		super(pDefaultImage);
		defaultImage = pDefaultImage;
		hoverImage = pHoverImage;

		if (hoverImage != null) {
			addMouseOverHandler(this);
			addMouseOutHandler(this);
			addClickHandler(this);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		setResource(defaultImage);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		setResource(hoverImage);
	}

	@Override
	public void onClick(ClickEvent event) {
		setResource(defaultImage);
	}
}
