package org.sopeco.frontend.client.layout.center.execute.tabThree;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.ProgressBar;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class StatusPanel extends FlowPanel {

	private static final String CSS_CLASS = "statusPanel";
	private static final String TABLE_CSS_CLASS = "detailsTable";

	private FlowPanel innerPanel;
	private FlexTable detailsTable;

	private HTML htmlLabel;

	private HTML labelAccount, labelScenario, labelExperiments, labelStart, labelTimeElapsed, labelTimeRemaining;
	private HTML valueAccount, valueScenario, valueExperiments, valueStart, valueTimeElapsed, valueTimeRemaining;
	private ProgressBar progressBar;

	/**
	 * Constructor.
	 */
	public StatusPanel() {
		initialize();
	}

	/**
	 * Initialize all components.
	 */
	private void initialize() {
		addStyleName(CSS_CLASS);

		htmlLabel = new HTML("<b>Executing: MyLabel</b>");
		progressBar = new ProgressBar();

		initTable();

		innerPanel = new FlowPanel();
		innerPanel.add(htmlLabel);
		innerPanel.add(detailsTable);
		innerPanel.add(progressBar);

		add(innerPanel);
	}

	/**
	 * Initializes the table for the details and all necessary HTML elements.
	 */
	private void initTable() {
		detailsTable = new FlexTable();
		detailsTable.addStyleName(TABLE_CSS_CLASS);

		labelAccount = new HTML(R.get("Account") + ":");
		labelScenario = new HTML(R.get("Scenario") + ":");
		labelExperiments = new HTML(R.get("Experiments") + ":");
		labelStart = new HTML(R.get("Start") + ":");
		labelTimeElapsed = new HTML(R.get("ElapsedTime") + ":");
		labelTimeRemaining = new HTML(R.get("RemainingTime") + ":");

		valueAccount = new HTML("-");
		valueScenario = new HTML("-");
		valueExperiments = new HTML("-");
		valueStart = new HTML("-");
		valueTimeElapsed = new HTML("-");
		valueTimeRemaining = new HTML("-");

		detailsTable.setWidget(0, 0, labelAccount);
		detailsTable.setWidget(1, 0, labelScenario);
		detailsTable.setWidget(0, 1, valueAccount);
		detailsTable.setWidget(1, 1, valueScenario);

		detailsTable.setWidget(0, 2, labelExperiments);
		detailsTable.setWidget(0, 3, valueExperiments);

		detailsTable.setWidget(0, 4, labelStart);
		detailsTable.setWidget(0, 5, valueStart);

		detailsTable.setWidget(0, 6, labelTimeElapsed);
		detailsTable.setWidget(1, 5, labelTimeRemaining);
		detailsTable.setWidget(0, 7, valueTimeElapsed);
		detailsTable.setWidget(1, 6, valueTimeRemaining);

		String width = "1px";
		detailsTable.getColumnFormatter().setWidth(0, width);
		detailsTable.getColumnFormatter().setWidth(2, width);
		detailsTable.getColumnFormatter().setWidth(4, width);
		detailsTable.getColumnFormatter().setWidth(6, width);

		width = "25%";
		detailsTable.getColumnFormatter().setWidth(1, width);
		detailsTable.getColumnFormatter().setWidth(3, width);
		detailsTable.getColumnFormatter().setWidth(5, width);
		detailsTable.getColumnFormatter().setWidth(7, width);

		detailsTable.getFlexCellFormatter().setRowSpan(0, 2, 2);
		detailsTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
	}

	/**
	 * Sets the value of the account HTML object.
	 * 
	 * @param new value
	 */
	public void setAccount(String value) {
		valueAccount.setHTML(value);
	}

	/**
	 * Sets the value of the scenario HTML object.
	 * 
	 * @param new value
	 */
	public void setScenario(String value) {
		valueScenario.setHTML(value);
	}

	/**
	 * Sets the value of the starting time HTML object.
	 * 
	 * @param new value
	 */
	public void setTimeStart(String value) {
		valueStart.setHTML(value);
	}

	/**
	 * Sets the value of the elapsed time HTML object.
	 * 
	 * @param new value
	 */
	public void setTimeElapsed(String value) {
		valueTimeElapsed.setHTML(value);
	}

	/**
	 * Sets the value of the remaining time HTML object.
	 * 
	 * @param new value
	 */
	public void setTimeRemaining(String value) {
		valueTimeRemaining.setHTML(value);
	}

	/**
	 * Sets the value of the label HTML object.
	 * 
	 * @param new value
	 */
	public void setStatusLabel(String value) {
		htmlLabel.setHTML("<b>" + value + "</b>");
	}

	/**
	 * @return the progressBar
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 * @param valueExperiments the valueExperiments to set
	 */
	public void setExperiments(String value) {
		valueExperiments.setHTML(value);
	}

}
