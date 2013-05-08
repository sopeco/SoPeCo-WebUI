package org.sopeco.webui.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface ImageBundle extends ClientBundle {

	@Source("images/background_noise.png")
	DataResource backgroundNoise();

	@Source("images/background_noise_2.png")
	DataResource backgroundNoise2();

	@Source("images/sopeco-icon-set.png")
	ImageResource iconSet();

	@Source("images/sopeco-logo-gray.png")
	ImageResource sopecoLogoGray();

	@Source("images/sopeco-logo.png")
	ImageResource sopecoLogo();
	
	@Source("images/sopeco-logo-small.png")
	ImageResource sopecoLogoSmall();

	@Source("images/flag_en.png")
	ImageResource flagEn();

	@Source("images/flag_de.png")
	ImageResource flagDe();

	@Source("images/loading_indicator.gif")
	ImageResource loadingIndicatorCircle();

	@Source("images/sap_research_gray.png")
	ImageResource sapResearchGray();

	@Source("images/sap_research_gray_orange.png")
	ImageResource sapResearchGrayOrange();

	@Source("images/database_remove.png")
	ImageResource iconDatabaseRemove();

	@Source("images/database_add.png")
	ImageResource iconDatabaseAdd();


}
