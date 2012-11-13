package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentSettingsView extends FlowPanel {

	private static final String EXP_SETTINGS_PANEL_ID = "expSettingsPanel";
	private static final String EXP_SETTINGS_NAME_PANEL_ID = "expSettingsNamePanel";

	private HTML htmlName;
	private HorizontalPanel hPanelName;
	private TextBox textboxName;

	private Image removeExperimentImage;

	public ExperimentSettingsView() {
		initialize();
	}

	/**
	 * Inits the necessary objects.
	 */
	private void initialize() {
		getElement().setId(EXP_SETTINGS_PANEL_ID);

		htmlName = new HTML(R.get("name") + ":");
		textboxName = new TextBox();
		removeExperimentImage = new Image("images/trash.png");

		removeExperimentImage.getElement().getStyle().setCursor(Cursor.POINTER);
		removeExperimentImage.setTitle(R.get("removeExpSeries"));

		hPanelName = new HorizontalPanel();
		hPanelName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanelName.getElement().setId(EXP_SETTINGS_NAME_PANEL_ID);

		hPanelName.add(htmlName);
		hPanelName.add(textboxName);
		hPanelName.add(removeExperimentImage);

		hPanelName.setCellWidth(htmlName, "1px");
		hPanelName.setCellWidth(textboxName, "1px");

		add(hPanelName);
	}

	public void addExtensionView(ExperimentExtensionView extView) {
		add(extView);
	}

	/**
	 * Sets all important values to a default value.
	 */
	public void reset() {
		textboxName.setText("");
	}

	/**
	 * @return the textboxName
	 */
	public TextBox getTextboxName() {
		return textboxName;
	}

	/**
	 * @return the removeExperimentImage
	 */
	public Image getRemoveExperimentImage() {
		return removeExperimentImage;
	}

}
