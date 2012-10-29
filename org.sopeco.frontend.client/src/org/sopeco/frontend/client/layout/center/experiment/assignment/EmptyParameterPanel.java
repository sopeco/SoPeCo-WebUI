package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EmptyParameterPanel extends ParameterPanel {

	public EmptyParameterPanel(AssignmentItem item) {
		super(item);
	}

	@Override
	public Map<String, String> getConfig() {
		return new HashMap<String, String>();
	}

	@Override
	public String getValue() {
		return "";
	}
}
