package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
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
	private static final int EXP_SETTINGS_PANEL_WIDTH = 400;

	private static final String EXP_SETTINGS_NAME_PANEL_ID = "expSettingsNamePanel";
	private static final String EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH = "100";

	private static final String EXP_SETTINGS_EXPLORATION_PANEL_ID = "expSettingsExplroationPanel";
	private static final String EXP_SETTINGS_TERMINATION_PANEL_ID = "expSettingsTerminationPanel";

	private HTML htmlName;
	private HorizontalPanel hPanelName;
	private TextBox textboxName;
	private FlowPanel explorationPanel, terminationPanel;

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
		explorationPanel = new FlowPanel();
		terminationPanel = new FlowPanel();

		hPanelName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hPanelName.add(htmlName);
		hPanelName.add(textboxName);

		add(hPanelName);
		add(explorationPanel);
		add(terminationPanel);

		// Styles etc..
		getElement().setId(EXP_SETTINGS_PANEL_ID);
		getElement().getStyle().setWidth(EXP_SETTINGS_PANEL_WIDTH, Unit.PX);

		hPanelName.getElement().setId(EXP_SETTINGS_NAME_PANEL_ID);
		hPanelName.setCellWidth(htmlName, EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH);

		initExplorationPanel();

		initTerminationPanel();
	}

	/**
	 * Initialization of the explorationPanel.
	 */
	private void initExplorationPanel() {
		explorationPanel.getElement().setId(EXP_SETTINGS_EXPLORATION_PANEL_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("explStrategy"));

		explorationPanel.getElement().appendChild(headline);
	}

	/**
	 * Initialization of the terminationPanel.
	 */
	private void initTerminationPanel() {
		terminationPanel.getElement().setId(EXP_SETTINGS_TERMINATION_PANEL_ID);

		Element headline = DOM.createElement("h3");
		headline.setInnerHTML(R.get("termination"));

		terminationPanel.getElement().appendChild(headline);
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
