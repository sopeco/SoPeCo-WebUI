package org.sopeco.frontend.client.layout.center.execute;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class TabController {

	private ExecuteController executeController;

	public TabController(ExecuteController pExecuteController) {
		executeController = pExecuteController;
	}

	public abstract FlowPanel getView();

	/**
	 * This method is executed when the tab is selected.
	 */
	public abstract void onSelection();

	protected ExecuteController getParentController() {
		return executeController;
	}
}
