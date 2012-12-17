package org.sopeco.frontend.client.layout.center.execute.tabThree;

import org.sopeco.frontend.client.layout.center.execute.ExecuteController;
import org.sopeco.frontend.client.layout.center.execute.TabController;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class TabControllerThree extends TabController {

	private ControllerQueueTab controllerQueueTab;

	public TabControllerThree(ExecuteController parentController) {
		super(parentController);
		initialize();
	}

	private void initialize() {
		controllerQueueTab = new ControllerQueueTab();
	}

	@Override
	public FlowPanel getView() {
		return controllerQueueTab;
	}

	@Override
	public void onSelection() {
	}
}
