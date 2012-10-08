package org.sopeco.frontend.client.layout.center.environment;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EnvironmentController implements ICenterController {

	private EnvironmentPanel view;

	public EnvironmentController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void reset() {
		view = new EnvironmentPanel();
	}

}
