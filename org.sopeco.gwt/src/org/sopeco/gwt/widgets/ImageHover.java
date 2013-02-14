package org.sopeco.gwt.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ImageHover extends Image implements MouseOverHandler,
		MouseOutHandler {

	private SafeUri image, imageHover;
	private int left, top, width, height;
	private int leftHover, topHover, widthHover, heightHover;

	public ImageHover(SafeUri pImage, int pLeft, int pTop, int pWidth,
			int pHeight, SafeUri pImageHover, int pLeftHover, int pTopHover,
			int pWidthHover, int pHeightHover) {
		super(pImage, pLeft, pTop, pWidth, pHeight);

		image = pImage;
		imageHover = pImageHover;

		left = pLeft;
		top = pTop;
		width = pWidth;
		height = pHeight;

		leftHover = pLeftHover;
		topHover = pTopHover;
		widthHover = pWidthHover;
		heightHover = pHeightHover;

		if (imageHover != null) {
			addMouseOverHandler(this);
			addMouseOutHandler(this);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		setUrlAndVisibleRect(image, left, top, width, height);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		setUrlAndVisibleRect(imageHover, leftHover, topHover, widthHover,
				heightHover);
	}
}
