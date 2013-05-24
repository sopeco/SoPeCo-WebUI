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
package org.sopeco.webui.client.layout.center.experiment;

import org.sopeco.webui.client.resources.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TabPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentTabPanel extends TabPanel implements ClickHandler {

	private static final String EXPERIMENT_TAB_PANEL_CLASS = "sopeco-TabPanel";

	private ExperimentSettingsView settingsView;
	private ExperimentParameterView parameterView;

	public ExperimentTabPanel() {
		R.css.cssSopecoTabPanel().ensureInjected();
		init();
	}

	/**
	 * Initializes all necessary objects.
	 */
	private void init() {
		addStyleName(EXPERIMENT_TAB_PANEL_CLASS);

		settingsView = new ExperimentSettingsView();
		parameterView = new ExperimentParameterView();

		add(settingsView, R.lang.expConfig());
		add(parameterView, R.lang.expParamAssignments());

		selectTab(0);

		getTabBar().getTab(1).addClickHandler(this);
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == getTabBar().getTab(1)) {

		}
	}

	/**
	 * @return the settingsView
	 */
	public ExperimentSettingsView getSettingsView() {
		return settingsView;
	}

	/**
	 * @return the parameterView
	 */
	public ExperimentParameterView getParameterView() {
		return parameterView;
	}

}
