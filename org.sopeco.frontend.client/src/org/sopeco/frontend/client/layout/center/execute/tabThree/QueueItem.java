package org.sopeco.frontend.client.layout.center.execute.tabThree;

import org.sopeco.frontend.client.R;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class QueueItem extends FlexTable {

	private static final String CONTROLLER_QUEUE_ITEM_CSS = "controllerQueueItem";

	private Image imgRepeat;
	private HTML htmlLabel, htmlAccount, htmlScenario, htmlSpecification, htmlExperiment;
	
	public QueueItem() {
		init();
	}

	private void init() {
		addStyleName(CONTROLLER_QUEUE_ITEM_CSS);

		htmlLabel = new HTML("MyExperiment");
		htmlAccount = new HTML("Account");
		htmlScenario = new HTML("MyScenario");
		htmlSpecification = new HTML("mySpecification");
		htmlExperiment = new HTML("myExperimentSeries");

		imgRepeat = new Image("images/repeat.png");
		imgRepeat.setWidth("21px");
		imgRepeat.setHeight("14px");
		imgRepeat.setTitle(R.get("ExperimentIsRepeat"));

		setWidget(0, 0, htmlLabel);
		setWidget(0, 1, imgRepeat);

		setWidget(1, 0, new HTML(R.get("Account") + ":"));
		setWidget(2, 0, new HTML(R.get("Scenario") + ":"));

		setWidget(1, 1, htmlAccount);
		setWidget(2, 1, htmlScenario);

		setWidget(1, 2, new HTML(R.get("Specification") + ":"));
		setWidget(2, 2, new HTML(R.get("ExperimentSeries") + ":"));

		setWidget(1, 3, htmlSpecification);
		setWidget(2, 3, htmlExperiment);

		String width = "1px";
		getColumnFormatter().setWidth(0, width);
		getColumnFormatter().setWidth(2, width);

		width = "50%";
		getColumnFormatter().setWidth(1, width);
		getColumnFormatter().setWidth(3, width);
	}
}
