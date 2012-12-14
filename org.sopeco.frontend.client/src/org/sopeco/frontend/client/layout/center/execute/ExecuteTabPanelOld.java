package org.sopeco.frontend.client.layout.center.execute;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.layout.center.execute.tabOne.ExecuteTab;
import org.sopeco.frontend.client.layout.center.execute.tabThree.ControllerQueueTab;
import org.sopeco.frontend.client.layout.center.execute.tabTwo.ScheduleTab;
import org.sopeco.frontend.client.resources.FrontEndResources;

import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExecuteTabPanelOld extends TabPanel {

	private static final String CSS_CLASS = "sopeco-TabPanel";
	private static final String EXECUTE_CSS_CLASS = "executeTabPanel";

	// private HTML htmlStatus;
	// private Button btnStartExperiment;

	private ExecuteTab tabExecute;
	private ScheduleTab tabSchedule;
	private ControllerQueueTab queueTab;

	public ExecuteTabPanelOld() {
		FrontEndResources.loadSopecoTabPanelCSS();
		init();
	}

	/**
	 * 
	 */
	private void init() {
		addStyleName(CSS_CLASS);
		addStyleName(EXECUTE_CSS_CLASS);

		tabExecute = new ExecuteTab();
		tabSchedule = new ScheduleTab();
		queueTab = new ControllerQueueTab();

		add(tabExecute, R.get("ExecuteExperiment"));
		add(tabSchedule, R.get("ScheduledExperiments") + " [0]");
		add(queueTab, R.get("controllerQueue"));

		selectTab(0);

		// btnStartExperiment = new Button(R.get("startExperiment"));
		// btnStartExperiment.getElement().getStyle().setMargin(1, Unit.EM);
		//
		// htmlStatus = new HTML("Status: -");
		// htmlStatus.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		// htmlStatus.getElement().getStyle().setMarginLeft(1, Unit.EM);
		//
		// add(htmlStatus);
		// add(btnStartExperiment);

	}

	public ExecuteTab getTabExecute() {
		return tabExecute;
	}

	public ScheduleTab getTabSchedule() {
		return tabSchedule;
	}

	// /**
	// * @return the btnStartExperiment
	// */
	// public Button getBtnStartExperiment() {
	// return btnStartExperiment;
	// }
	//
	// /**
	// * @return the htmlStatus
	// */
	// public HTML getHtmlStatus() {
	// return htmlStatus;
	// }

}
