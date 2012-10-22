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
	/** Width of the SettingsPanel */
	public static final int EXP_SETTINGS_PANEL_WIDTH = 400;

	private static final String EXP_SETTINGS_NAME_PANEL_ID = "expSettingsNamePanel";
	private static final String EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH = "100";

//	private static final String EXP_SETTINGS_EXPLORATION_PANEL_ID = "expSettingsExplroationPanel";
//	private static final String EXP_SETTINGS_TERMINATION_PANEL_ID = "expSettingsTerminationPanel";

//	private static final String TERMIANTION_RADIO_GRP = "TERMINATION_GRP";

	private HTML htmlName, htmlRepetitions, htmlTimeout;
	private HorizontalPanel hPanelName;
	private TextBox textboxName/* , textboxRepetitions, textboxTimeout */;
//	private FlowPanel explorationPanel, terminationPanel;
	// private RadioButton radioRepetitions, radioTimeout;
	// private FlexTable terminationGrid;
//	private ComboBox terminationCombobox;

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
//		explorationPanel = new FlowPanel();
//		terminationPanel = new FlowPanel();

		hPanelName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		hPanelName.add(htmlName);
		hPanelName.add(textboxName);

		add(hPanelName);
//		add(explorationPanel);
//		add(terminationPanel);

		// Styles etc..
		getElement().setId(EXP_SETTINGS_PANEL_ID);
		getElement().getStyle().setWidth(EXP_SETTINGS_PANEL_WIDTH, Unit.PX);

		hPanelName.getElement().setId(EXP_SETTINGS_NAME_PANEL_ID);
		hPanelName.setCellWidth(htmlName, EXP_SETTINGS_NAME_PANEL_LEFTCELL_WIDTH);

//		initExplorationPanel();
//
//		initTerminationPanel();
	}

	public void addExtensionView (ExperimentExtensionView extView){
		add(extView);
	}
	
//	/**
//	 * Initialization of the explorationPanel.
//	 */
//	private void initExplorationPanel() {
//		explorationPanel.getElement().setId(EXP_SETTINGS_EXPLORATION_PANEL_ID);
//
//		Element headline = DOM.createElement("h3");
//		headline.setInnerHTML(R.get("explStrategy"));
//
//		explorationPanel.getElement().appendChild(headline);
//	}
//
//	/**
//	 * Initialization of the terminationPanel.
//	 */
//	private void initTerminationPanel() {
//		terminationPanel.getElement().setId(EXP_SETTINGS_TERMINATION_PANEL_ID);
//
//		Element headline = DOM.createElement("h3");
//		headline.setInnerHTML(R.get("termination"));
//
//		terminationPanel.getElement().appendChild(headline);
//
//		// terminationCombobox = new ComboBox();
//		// terminationCombobox.setWidth(TERMIANTION_COMBOBOX_WIDTH);
//		// terminationCombobox.getElement().getStyle()
//		// .setMarginLeft((EXP_SETTINGS_PANEL_WIDTH -
//		// TERMIANTION_COMBOBOX_WIDTH) / 2, Unit.PX);
//
////		terminationPanel.add(terminationCombobox);
//
//		// terminationGrid = new FlexTable();
//		// radioRepetitions = new RadioButton(TERMIANTION_RADIO_GRP);
//		// radioTimeout = new RadioButton(TERMIANTION_RADIO_GRP);
//		// htmlRepetitions = new HTML(R.get("repetitions"));
//		// htmlTimeout = new HTML(R.get("timeout"));
//		// textboxRepetitions = new TextBox();
//		// textboxTimeout = new TextBox();
//		//
//		// NumbersOnlyHandler.setOn(textboxRepetitions);
//		// NumbersOnlyHandler.setOn(textboxTimeout);
//		//
//		// terminationGrid.setWidget(0, 0, radioRepetitions);
//		// terminationGrid.setWidget(1, 0, radioTimeout);
//		// terminationGrid.setWidget(0, 1, htmlRepetitions);
//		// terminationGrid.setWidget(1, 1, htmlTimeout);
//		// terminationGrid.setWidget(0, 2, textboxRepetitions);
//		// terminationGrid.setWidget(1, 2, textboxTimeout);
//		//
//		// terminationPanel.add(terminationGrid);
//	}

	private void addExistingTerminationConditions() {

	}

	// /**
	// * Sets the specified row transparent.
	// *
	// * @param row
	// */
	// public void setTerminationGridRowTransparent(int row) {
	// switch (row) {
	// case 0:
	// terminationGrid.getRowFormatter().addStyleName(0, "transparent-50");
	// terminationGrid.getRowFormatter().removeStyleName(1, "transparent-50");
	// break;
	// case 1:
	// terminationGrid.getRowFormatter().addStyleName(1, "transparent-50");
	// terminationGrid.getRowFormatter().removeStyleName(0, "transparent-50");
	// break;
	// default:
	// terminationGrid.getRowFormatter().removeStyleName(0, "transparent-50");
	// terminationGrid.getRowFormatter().removeStyleName(1, "transparent-50");
	// break;
	// }
	// }

	/**
	 * Sets all important values to a default value.
	 */
	public void reset() {
		textboxName.setText("");
		// textboxRepetitions.setText("");
		// textboxTimeout.setText("");
		// radioRepetitions.setValue(false);
		// radioTimeout.setValue(false);
		//
		// setTerminationGridRowTransparent(-1);
	}

	/**
	 * @return the textboxName
	 */
	public TextBox getTextboxName() {
		return textboxName;
	}

	// /**
	// * @return the textboxRepetitions
	// */
	// public TextBox getTextboxRepetitions() {
	// return textboxRepetitions;
	// }
	//
	// /**
	// * @return the textboxTimeout
	// */
	// public TextBox getTextboxTimeout() {
	// return textboxTimeout;
	// }
	//
	// /**
	// * @return the checkboxRepetitions
	// */
	// public CheckBox getRadioRepetitions() {
	// return radioRepetitions;
	// }
	//
	// /**
	// * @return the checkboxTimeout
	// */
	// public CheckBox getRadioTimeout() {
	// return radioTimeout;
	// }

	// /**
	// * @return the terminationGrid
	// */
	// public Grid getTerminationGrid() {
	// return terminationGrid;
	// }

}
