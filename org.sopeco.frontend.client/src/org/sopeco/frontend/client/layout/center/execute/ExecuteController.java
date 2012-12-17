package org.sopeco.frontend.client.layout.center.execute;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.execute.tabOne.ExecuteTab;
import org.sopeco.frontend.client.layout.center.execute.tabOne.TabControllerOne;
import org.sopeco.frontend.client.layout.center.execute.tabThree.TabControllerThree;
import org.sopeco.frontend.client.layout.center.execute.tabTwo.TabControllerTwo;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.Manager;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.shared.entities.FrontendScheduledExperiment;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteController implements ICenterController, ClickHandler, SelectionHandler<Integer> {

	private static final Logger LOGGER = Logger.getLogger(ExecuteController.class.getName());

	private TabControllerOne tabControllerOne;
	private TabControllerTwo tabControllerTwo;
	private TabControllerThree tabControllerThree;

	private ExecuteTabPanel view;

	public ExecuteController() {
		FrontEndResources.loadExecuteViewCSS();
		init();
	}

	private void init() {
		tabControllerOne = new TabControllerOne(this);
		tabControllerTwo = new TabControllerTwo(this);
		tabControllerThree = new TabControllerThree(this);

		view = new ExecuteTabPanel();
		view.add(tabControllerOne.getView(), R.get("ExecuteExperiment"));
		view.add(tabControllerTwo.getView(), R.get("ScheduledExperiments"));
		view.add(tabControllerThree.getView(), R.get("controllerQueue"));
		view.selectTab(0);

//		view.getTabExecute().getBtnExecute().addClickHandler(this);

		view.getTabBar().addSelectionHandler(this);
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		if (event.getSelectedItem() == 2) {
			tabControllerTwo.onSelection();
		}
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void onSwitchTo() {
		((ExecuteTab) tabControllerOne.getView()).generateTree();
		refreshScheduleTab();
	}

	@Override
	public void reset() {
	}

	@Override
	public void onClick(ClickEvent event) {

	}

	public void refreshScheduleTab() {
		RPC.getExecuteRPC().getScheduledExperiments(new AsyncCallback<List<FrontendScheduledExperiment>>() {
			@Override
			public void onFailure(Throwable caught) {
				LOGGER.severe(caught.getLocalizedMessage());
				Message.error(caught.getMessage());
			}

			@Override
			public void onSuccess(List<FrontendScheduledExperiment> result) {
				tabControllerTwo.updateList(result);
			}
		});
	}

}
