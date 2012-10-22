package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentSettingsView extends FlowPanel {

	private static final String EXP_SETTINGS_PANEL_ID = "expSettingsPanel";
	/** Width of the SettingsPanel. */
	public static final int EXP_SETTINGS_PANEL_WIDTH = 400;

	private static final String EXP_SETTINGS_NAME_PANEL_ID = "expSettingsNamePanel";
	private static final String EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH = "100";

	private HTML htmlName;
	private HorizontalPanel hPanelName;
	private TextBox textboxName;

	public ExperimentSettingsView() {
		initialize();
	}

	/**
	 * Inits the necessary objects.
	 */
	private void initialize() {
		hPanelName = new HorizontalPanel();
		htmlName = new HTML(R.get("name"));
		textboxName = new TextBox();

		hPanelName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hPanelName.add(htmlName);
		hPanelName.add(textboxName);

		add(hPanelName);

		// Styles etc..
		getElement().setId(EXP_SETTINGS_PANEL_ID);
		getElement().getStyle().setWidth(EXP_SETTINGS_PANEL_WIDTH, Unit.PX);

		hPanelName.getElement().setId(EXP_SETTINGS_NAME_PANEL_ID);
		hPanelName.setCellWidth(htmlName, EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH);
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
}
