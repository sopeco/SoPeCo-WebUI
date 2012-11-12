package org.sopeco.frontend.client.layout.center;

import org.sopeco.frontend.client.layout.ScenarioAddController;


/**
 * 
 * @author Marius Oehler
 * 
 */
public class NoScenario extends CenterPanel {

	private static final String ADD_SCENARIO_BOX = "noScenarioBox";
	
	public NoScenario() {		
		ScenarioAddController sac = new ScenarioAddController(true, false);
		sac.getView().addStyleName(ADD_SCENARIO_BOX);
		
		add(sac.getView());
		
	}
}
