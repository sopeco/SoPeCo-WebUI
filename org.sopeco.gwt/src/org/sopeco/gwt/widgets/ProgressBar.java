package org.sopeco.gwt.widgets;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ProgressBar extends FlowPanel {

	private static final String SOPECO_PROGRESS_BAR_CSS = "sopeco-ProgressBar";
	private static final String SOPECO_PROGRESS_BAR_BAR_CSS = "sopeco-ProgressBar-Bar";
	private FlowPanel bar;
	private HTML label;

	private double maxValue = 100;
	private double minValue = 0;
	private double value = 50;

	public ProgressBar() {
		this(0, 100, 50);
	}

	public ProgressBar(double pMinValue, double pMaxValue, double pValue) {
		maxValue = pMaxValue;
		minValue = pMinValue;
		value = pValue;

		WidgetResources.loadProgressBarCSS();
		init();
	}

	private void init() {
		addStyleName(SOPECO_PROGRESS_BAR_CSS);

		bar = new FlowPanel();
		bar.addStyleName(SOPECO_PROGRESS_BAR_BAR_CSS);

		label = new HTML("0 %");

		add(label);
		add(bar);

		setValue(value);
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double pMaxValue) {
		this.maxValue = pMaxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double pMinValue) {
		this.minValue = pMinValue;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double pValue) {
		this.value = pValue;

		double barWidth = 100 / (maxValue - minValue) * (value - minValue);
		bar.setWidth(barWidth + "%");

		label.setText(((int) barWidth) + " %");
	}

	public void setLableVisible(boolean visible) {
		label.setVisible(visible);
	}
}
