/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.gwt.widgets;

import org.sopeco.gwt.widgets.resources.WidgetResources;

import com.google.gwt.animation.client.Animation;
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

	private Animation animationTimer;

	private double maxValue = 100;
	private double minValue = 0;
	private double value = 50;
	private double barValue = 50;
	private double tempBarValue = 50;
	private int animationDuration = 500;
	private boolean isAnimating = false;

	public ProgressBar() {
		this(0, 100, 50);
	}

	public ProgressBar(double pMinValue, double pMaxValue, double pValue) {
		maxValue = pMaxValue;
		minValue = pMinValue;
		value = pValue;

		WidgetResources.resc.progressBarCss().ensureInjected();
		init();
	}

	private void init() {
		addStyleName(SOPECO_PROGRESS_BAR_CSS);

		bar = new FlowPanel();
		bar.addStyleName(SOPECO_PROGRESS_BAR_BAR_CSS);

		label = new HTML("0 %");

		add(label);
		add(bar);

		setValue(value, false);
		updateBarWidth();

		animationTimer = new Animation() {
			@Override
			protected void onUpdate(double progress) {
				double barOffset = (value - barValue) * progress;
				tempBarValue = barValue + barOffset;
				updateBarWidth();
			}

			@Override
			protected void onStart() {
				super.onStart();
				isAnimating = true;
			}

			@Override
			protected void onComplete() {
				super.onComplete();
				isAnimating = false;
				tempBarValue = value;
				barValue = value;
				updateBarWidth();
			}
		};

	}

	private void runBarAnimation() {
		animationTimer.run(animationDuration);
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
		setValue(pValue, true);
	}

	public void setValue(double pValue, boolean animation) {
		this.value = pValue;

		if (isAnimating) {
			animationTimer.cancel();
			barValue = tempBarValue;
		}

		double progressPercent = 100 / (maxValue - minValue)
				* (value - minValue);
		label.setText(((int) progressPercent) + " %");

		if (animation) {
			runBarAnimation();
		} else {
			tempBarValue = value;
			barValue = value;
			updateBarWidth();
		}
	}

	private void updateBarWidth() {
		bar.setWidth(tempBarValue + "%");
	}

	public void setLableVisible(boolean visible) {
		label.setVisible(visible);
	}
}
