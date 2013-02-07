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
package org.sopeco.webui.client.layout.center;

import org.sopeco.gwt.widgets.Headline;
import org.sopeco.gwt.widgets.SlidePanel;
import org.sopeco.webui.client.event.EventControl;
import org.sopeco.webui.client.event.MEControllerEvent;
import org.sopeco.webui.client.event.MEControllerEvent.EventType;
import org.sopeco.webui.client.helper.SimpleNotify;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.layout.ScenarioAddController;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.manager.Manager.ControllerStatus;
import org.sopeco.webui.client.mec.MEControllerSettings;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NoScenario extends CenterPanel implements ClickHandler, BlurHandler, KeyUpHandler,
		ValueChangeHandler<Boolean>, SimpleNotify {

	private static final String ADD_SCENARIO_BOX = "noScenarioBox";
	private static final int SLIDER_HEIGHT = 250, SLIDER_WIDTH = 410;

	private static final String FOOTER_CSS_CLASS = "noscFooterPanel";

	private SlidePanel slidePanel;
	private Button btnNext, btnPrevious;
	private ScenarioAddController sac;
	private MEControllerSettings mecController;
	private FlowPanel mecPanel;

	public NoScenario() {
		slidePanel = new SlidePanel(SLIDER_WIDTH, SLIDER_HEIGHT);
		slidePanel.addStyleName(ADD_SCENARIO_BOX);

		slidePanel.getFooterPanel().addStyleName(FOOTER_CSS_CLASS);

		btnNext = new Button(R.get("Next"));
		btnNext.addClickHandler(this);
		btnNext.getElement().getStyle().setFloat(Float.RIGHT);

		btnPrevious = new Button(R.get("Previous"));
		btnPrevious.addClickHandler(this);

		slidePanel.addFooterWidget(btnPrevious);
		slidePanel.addFooterWidget(btnNext);

		sac = new ScenarioAddController(true, false, false);
		sac.addBlurHandlerSName(this);
		sac.addKeyUpHandlerSName(this);

		mecController = new MEControllerSettings();
		mecController.addValueChangeHandler(this);

		mecPanel = new FlowPanel();
		Headline headline = new Headline(R.lang.measurementEnvironmentController());
		headline.getElement().getStyle().setMarginTop(0, Unit.PX);
		mecPanel.add(headline);
		mecPanel.add(mecController.getView());
		mecPanel.getElement().getStyle().setPadding(1, Unit.EM);

		slidePanel.addWidget(sac.getView());
		slidePanel.addWidget(mecPanel);

		add(slidePanel);

		updateButtons();
	}

	@Override
	public void onBlur(BlurEvent event) {
		btnNext.setEnabled(sac.checkForm());
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		btnNext.setEnabled(sac.checkForm());
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == btnNext) {
			if (slidePanel.getSlidePosition() == 0) {
				slidePanel.next();
			} else {
				addScenario();
			}
		} else if (event.getSource() == btnPrevious) {
			slidePanel.previous();
		}
		updateButtons();
	}

	/**
	 * 
	 */
	private void addScenario() {
		sac.createAndAddScenario(this);
	}

	@Override
	public void call() {
		addMEController();
		MainLayoutPanel.get().getNorthPanel().updateScenarioList();
		ScenarioManager.get().switchScenario(Manager.get().getAccountDetails().getSelectedScenario());
	}

	/**
	 * 
	 */
	private void addMEController() {
		mecController.saveControllerSettings();
	}

	/**
	 * 
	 */
	private void updateButtons() {
		btnPrevious.setEnabled(slidePanel.hasPrevious());

		if (!slidePanel.hasNext()) {
			btnNext.setText(R.get("AddScenario"));
			btnNext.setEnabled(mecController.getLastValue());
		} else {
			btnNext.setText(R.get("Next"));
			btnNext.setEnabled(true);
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		updateButtons();
	}
}
