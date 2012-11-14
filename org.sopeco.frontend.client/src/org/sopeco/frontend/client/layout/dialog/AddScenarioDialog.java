package org.sopeco.frontend.client.layout.dialog;

import org.sopeco.frontend.client.layout.ScenarioAddController;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AddScenarioDialog extends DialogBox implements ClickHandler {

	public AddScenarioDialog() {
		super(false, true);
		setGlassEnabled(true);
		
		ScenarioAddController sca = new ScenarioAddController(false, true, true);
		sca.addHideHandler(this);
		add(sca.getView());
	}

	@Override
	public void onClick(ClickEvent event) {
		hide();	
	}
}
