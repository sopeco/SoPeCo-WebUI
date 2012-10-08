package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.layout.center.CenterPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;

/**
 * 
 * @author Marius Oehler
 *
 */
public class SpecificationController implements ICenterController {

	private SpecificationView view;

	public SpecificationController() {
		reset();
	}

	@Override
	public void reset() {
		view = new SpecificationView();
	}

	@Override
	public CenterPanel getView() {
		return view;
	}

}
