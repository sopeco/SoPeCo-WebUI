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
package org.sopeco.frontend.client.layout;

import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.layout.center.CenterType;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ViewSwitch {
	private static final Logger LOGGER = Logger.getLogger("view");

	/**
	 * Switch the view to the panel with the given panel-type.
	 * 
	 * @param type
	 *            type of the panel which will be shown.
	 */
	public void switchTo(CenterType type) {
		switchTo(type, true);
	}

	private void switchTo(CenterType type, boolean historyEvent) {
		LOGGER.fine("Switch view to type: " + type.toString());

		MainLayoutPanel.get().getCenterController(type).onSwitchTo();
		MainLayoutPanel.get().updateCenterPanel(type);
		MainLayoutPanel.get().getNavigationController().hideChangeSpecpanel();
	}

	/**
	 * Switch the view to the experiment-view and displays the experiment with
	 * the given name. Then it fires an ExperimentChanged-Event.
	 * 
	 * @param experimentName
	 *            name of the experiment
	 */
	public void switchToExperiment(String experimentName) {
		LOGGER.fine("Switch view to experiment: " + experimentName);

		if (MainLayoutPanel.get().getCenterType() != CenterType.Experiment) {
			switchTo(CenterType.Experiment);
		}

		MainLayoutPanel.get().getNavigationController().highlightExperiment(experimentName);

		ExperimentChangedEvent expChangedEvent = new ExperimentChangedEvent(experimentName);
		EventControl.get().fireEvent(expChangedEvent);
	}
}
