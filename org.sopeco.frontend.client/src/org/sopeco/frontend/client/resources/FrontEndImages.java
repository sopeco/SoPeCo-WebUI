package org.sopeco.frontend.client.resources;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Marius Oehler
 * 
 */
public final class FrontEndImages {
	private FrontEndImages() {
	}

	private static ImageResources images;

	public static ImageResources images() {
		if (images == null) {
			images = GWT.create(ImageResources.class);
		}
		return images;
	}
}