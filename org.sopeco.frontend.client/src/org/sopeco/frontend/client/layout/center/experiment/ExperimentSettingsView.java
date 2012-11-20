package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.ClearDiv;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentSettingsView extends FlowPanel {

	private static final String EXP_SETTINGS_PANEL_ID = "expSettingsPanel";
	private static final String IMAGE_DUPLICATE = "images/duplicate.png";
	private static final String IMAGE_RENAME = "images/rename.png";
	private static final String IMAGE_REMOVE = "images/trash.png";

	private HTML htmlName;
	private FlowPanel topWrapper;

	private Image imgDuplicate, imgRemove, imgRename;

	public ExperimentSettingsView() {
		initialize();
	}

	/**
	 * Inits the necessary objects.
	 */
	private void initialize() {
		getElement().setId(EXP_SETTINGS_PANEL_ID);

		htmlName = new HTML("1234567890");
		htmlName.addStyleName("name");

		imgRename = new Image(IMAGE_RENAME);
		imgDuplicate = new Image(IMAGE_DUPLICATE);
		imgRemove = new Image(IMAGE_REMOVE);

		imgRename.setTitle(R.get("Rename"));
		imgDuplicate.setTitle(R.get("Duplicate"));
		imgRemove.setTitle(R.get("Remove"));

		topWrapper = new FlowPanel();
		topWrapper.add(new HTML("Name:"));
		topWrapper.add(htmlName);
		topWrapper.add(imgRename);
		topWrapper.add(imgDuplicate);
		topWrapper.add(imgRemove);
		topWrapper.add(new ClearDiv());
		topWrapper.addStyleName("expTopWrapper");

		add(topWrapper);
	}

	public void addExtensionView(ExperimentExtensionView extView) {
		add(extView);
	}

	/**
	 * Sets all important values to a default value.
	 */
	public void reset() {
	}

	public void setExperimentName(String text) {
		htmlName.setText(text);
	}

	/**
	 * @return the removeExperimentImage
	 */
	public Image getImgRemove() {
		return imgRemove;
	}

	/**
	 * @return the imgDuplicate
	 */
	public Image getImgDuplicate() {
		return imgDuplicate;
	}

	/**
	 * @return the imgRename
	 */
	public Image getImgRename() {
		return imgRename;
	}

}
