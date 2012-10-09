package org.sopeco.frontend.client.layout.center.result;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ResultController implements ICenterController {

	private ResultView view;

	public ResultController() {
		reset();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

	@Override
	public void reset() {
		view = new ResultView();
	}

}
