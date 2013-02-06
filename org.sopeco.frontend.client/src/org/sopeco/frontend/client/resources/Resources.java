package org.sopeco.frontend.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * 
 * @author Marius Oehler
 * 
 */
public interface Resources extends ClientBundle {

	@Source("css/common.css")
	@CssResource.NotStrict
	CssResource cssCommon();

	@Source("css/experimentView.css")
	@CssResource.NotStrict
	CssResource cssExperiment();

	@Source("css/csvEditor.css")
	@CssResource.NotStrict
	CssResource cssCsvEditor();

	@Source("css/specificationView.css")
	@CssResource.NotStrict
	CssResource cssSpecification();

	@Source("css/topNavigation.css")
	@CssResource.NotStrict
	CssResource cssTopNavigation();

	@Source("css/scenarioAddView.css")
	@CssResource.NotStrict
	CssResource cssScenarioAdd();

	@Source("css/sopecoTabPanel.css")
	@CssResource.NotStrict
	CssResource cssSopecoTabPanel();

	@Source("css/controllerView.css")
	@CssResource.NotStrict
	CssResource cssControllerView();

	@Source("css/loginBox.css")
	@CssResource.NotStrict
	CssResource cssLoginBox();

	@Source("css/executeView.css")
	@CssResource.NotStrict
	CssResource cssExecuteView();

	@Source("visualizationView.css")
	@CssResource.NotStrict
	CssResource visualizationViewCss();

	@Source("visualizationWizard.css")
	@CssResource.NotStrict
	CssResource visualizationWizardCss();

	@Source("css/navigation.css")
	@CssResource.NotStrict
	CssResource cssNavigation();

	@Source("css/mecSettingsView.css")
	@CssResource.NotStrict
	CssResource cssMECSettingsView();

	/** IMAGES. */

	@Source("images/error.png")
	ImageResource imgError();

	@Source("images/success.png")
	ImageResource imgSuccess();

	@Source("images/database.png")
	ImageResource imgDatabase();

	@Source("images/background_noise.png")
	DataResource imgBackgroundNoise();

	@Source("images/background_noise_2.png")
	DataResource imgBackgroundNoise2();

	@Source("images/db_add.png")
	ImageResource imgIconDatabaseAdd();

	@Source("images/db_remove.png")
	ImageResource imgIconDatabaseRemove();

	@Source("images/sap_research_gray_orange.png")
	ImageResource imgSapResearchGrayOrange();

	@Source("images/sap_research_gray.png")
	ImageResource imgSapResearchGray();

	@Source("images/loading_indicator.gif")
	ImageResource imgLoadingIndicatorCircle();

	@Source("images/gear.png")
	DataResource imgGear();

	@Source("images/add_blue.png")
	DataResource imgAddBlue();

	@Source("images/switch.png")
	ImageResource imgSwitch();

	@Source("images/flag_de.png")
	ImageResource imgFlagDe();

	@Source("images/flag_en.png")
	ImageResource imgFlagEn();

	@Source("images/sopeco-logo.png")
	ImageResource imgSoPeCoLogo();

	@Source("images/reload.png")
	ImageResource imgReload();

	@Source("images/status-red.png")
	ImageResource imgStatusRed();

	@Source("images/status-green.png")
	ImageResource imgStatusGreen();

	@Source("images/status-yellow.png")
	ImageResource imgStatusYellow();
	
	@Source("images/status-gray.png")
	ImageResource imgStatusGray();
}
