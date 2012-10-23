package org.sopeco.frontend.client.layout.center.experiment;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentParameterView extends FlowPanel {

	private static final String EXP_PARAMETER_PANEL_ID = "expParameterPanel";

	public ExperimentParameterView() {
		initialize();
	}

	/**
	 * Initialize all required objects etc.
	 */
	private void initialize() {
		getElement().setId(EXP_PARAMETER_PANEL_ID);
		getElement().getStyle().setLeft(ExperimentView.EXP_SETTINGS_PANEL_WIDTH, Unit.PX);
	}	
}
