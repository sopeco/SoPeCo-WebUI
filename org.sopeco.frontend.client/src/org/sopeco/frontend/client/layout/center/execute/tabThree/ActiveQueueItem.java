package org.sopeco.frontend.client.layout.center.execute.tabThree;

import org.sopeco.frontend.client.R;
import org.sopeco.gwt.widgets.ProgressBar;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ActiveQueueItem extends FlexTable {

	private static final String ITEM_CSS = "activeQueueItem";

	private HTML htmlAccount, htmlScenario, htmlSpecification, htmlExperiment, htmlStart, htmlProgress,
			htmlElapsedTime, htmlRemainingTime, htmlLabel;

	private ProgressBar progressBar;

	public ActiveQueueItem() {
		init();
	}

	private void init() {
		addStyleName(ITEM_CSS);

		htmlLabel = new HTML("Label");
		htmlAccount = new HTML("Account");
		htmlScenario = new HTML("MyScenario");
		htmlSpecification = new HTML("mySpecification");
		htmlExperiment = new HTML("myExperimentSeries");
		htmlStart = new HTML("11:25");
		htmlProgress = new HTML("60%");
		htmlElapsedTime = new HTML("00:01:33");
		htmlRemainingTime = new HTML("00:09:22");
		progressBar = new ProgressBar();

		setWidget(0, 0, new HTML(R.get("Label") + ":"));
		setWidget(0, 1, htmlLabel);
		setWidget(0, 2, new Anchor("Cancel Execution"));

		setWidget(1, 0, new HTML(R.get("Account") + ":"));
		setWidget(2, 0, new HTML(R.get("Scenario") + ":"));

		setWidget(1, 1, htmlAccount);
		setWidget(2, 1, htmlScenario);

		setWidget(1, 2, new HTML(R.get("Specification") + ":"));
		setWidget(2, 2, new HTML(R.get("ExperimentSeries") + ":"));

		setWidget(1, 3, htmlSpecification);
		setWidget(2, 3, htmlExperiment);

		setWidget(1, 4, new HTML(R.get("Start") + ":"));
		setWidget(2, 4, new HTML(R.get("Progress") + ":"));

		setWidget(1, 5, htmlStart);
		setWidget(2, 5, htmlProgress);

		setWidget(1, 6, new HTML(R.get("ElapsedTime") + ":"));
		setWidget(2, 6, new HTML(R.get("RemainingTime") + ":"));

		setWidget(1, 7, htmlElapsedTime);
		setWidget(2, 7, htmlRemainingTime);

		setWidget(3, 0, progressBar);

		String width = "1px";
		getColumnFormatter().setWidth(0, width);
		getColumnFormatter().setWidth(2, width);
		getColumnFormatter().setWidth(4, width);
		getColumnFormatter().setWidth(6, width);

		width = "25%";
		getColumnFormatter().setWidth(1, width);
		getColumnFormatter().setWidth(3, width);
		getColumnFormatter().setWidth(5, width);
		getColumnFormatter().setWidth(7, width);

		getFlexCellFormatter().setColSpan(3, 0, 8);
		getFlexCellFormatter().setColSpan(0, 2, 6);
		getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		setIdle();
	}

	public void setLabel(String text) {
		htmlLabel.setText(text);
	}

	public void setAccount(String text) {
		htmlAccount.setText(text);
	}

	public void setScenario(String text) {
		htmlScenario.setText(text);
	}

	public void setSpecification(String text) {
		htmlSpecification.setText(text);
	}

	public void setExperiment(String text) {
		htmlExperiment.setText(text);
	}

	public void setStart(String text) {
		htmlStart.setText(text);
	}

	public void setProgress(String text) {
		htmlProgress.setText(text);
	}

	public void setElapsedTime(String text) {
		htmlElapsedTime.setText(text);
	}

	public void setRemainingTime(String text) {
		htmlRemainingTime.setText(text);
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setIdle() {
		setLabel("idle");
		setAccount("-");
		setScenario("-");
		setSpecification("-");
		setExperiment("-");
		setStart("-");
		setProgress("-");
		setElapsedTime("-");
		setRemainingTime("-");
		getProgressBar().setValue(0);
	}
	
	public void setDummyProgress() {
		setLabel("executing");
		setAccount("MyAccount");
		setScenario("MyScenario");
		setSpecification("MySpecification");
		setExperiment("MyExperiment");
		setStart("10:02 AM / 06.12.2012");
		setProgress("43%");
		setElapsedTime("00:06:32");
		setRemainingTime("01:48:22");
		getProgressBar().setValue(43);
	}
}
