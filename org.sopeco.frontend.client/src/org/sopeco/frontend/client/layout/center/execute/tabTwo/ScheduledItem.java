package org.sopeco.frontend.client.layout.center.execute.tabTwo;

import org.sopeco.frontend.client.R;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ScheduledItem extends FlexTable {

	private static final String ITEM_CSS = "scheduleItem";

	private HTML htmlLabel, htmlSpecification, htmlExperiment, htmlStart, htmlRepeat, htmlAdded;

	public ScheduledItem() {
		init();
	}

	private void init() {
		addStyleName(ITEM_CSS);

		htmlLabel = new HTML("MyExperiment");
		htmlSpecification = new HTML("mySpecification");
		htmlExperiment = new HTML("myExperimentSeries");
		htmlStart = new HTML("15:45 - 03.12.2012");
		htmlRepeat = new HTML("once");
		htmlAdded = new HTML("11:45 - 03.12.2012");

		setWidget(0, 0, htmlLabel);

		setWidget(1, 0, new HTML(R.get("Specification") + ":"));
		setWidget(2, 0, new HTML(R.get("ExperimentSeries") + ":"));

		setWidget(1, 1, htmlSpecification);
		setWidget(2, 1, htmlExperiment);

		setWidget(1, 2, new HTML(R.get("Start") + ":"));
		setWidget(2, 2, new HTML(R.get("Repeat") + ":"));

		setWidget(1, 3, htmlStart);
		setWidget(2, 3, htmlRepeat);
		
		setWidget(1, 4, new HTML(R.get("Added") + ":"));

		setWidget(1, 5, htmlAdded);

		String width = "1px";
		getColumnFormatter().setWidth(0, width);
		getColumnFormatter().setWidth(2, width);
		getColumnFormatter().setWidth(4, width);

		width = "33%";
		getColumnFormatter().setWidth(1, width);
		getColumnFormatter().setWidth(3, width);
		getColumnFormatter().setWidth(5, width);
		
		getFlexCellFormatter().setColSpan(2, 3, 3);
	}
}
