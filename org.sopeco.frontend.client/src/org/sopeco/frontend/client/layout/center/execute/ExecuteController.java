package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController implements ICenterController {

	private ExecuteView view;

	public ExecuteController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void reset() {
		view = new ExecuteView();
	}

}
