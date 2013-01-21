package org.sopeco.frontend.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ImageResources extends ClientBundle {

	@Source("images/error.png")
	ImageResource error();

	@Source("images/success.png")
	ImageResource success();

}
