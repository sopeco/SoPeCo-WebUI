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
package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentAssignmentsChangedEventHandler;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.experiment.assignment.items.AssignmentItem;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentController {

	private static final Logger LOGGER = Logger.getLogger(AssignmentController.class.getName());

	/** Assignment Type. */
	public enum Type {
		/** */
		EXPERIMENT, PREPERATION
	}

	private AssignmentView view;

	private List<AssignmentItem> assignmentItems2;

	public AssignmentController(Type type) {
		String headline = "";
		headline = R.get("expAssignsments");

		assignmentItems2 = new ArrayList<AssignmentItem>();

		view = new AssignmentView(headline);

		registerHandlerExperiment();
		registerHandlerCommon();
	}

	/**
	 * 
	 */
	private void registerHandlerExperiment() {
		EventControl.get().addHandler(ExperimentAssignmentsChangedEvent.TYPE,
				new ExperimentAssignmentsChangedEventHandler() {
					@Override
					public void onAssignmentChangedEvent(ExperimentAssignmentsChangedEvent event) {
						assignmentsChanged();
					}
				});
	}

	/**
	 * Register some event handlers.
	 */
	private void registerHandlerCommon() {
		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				addExisitngAssignments();
			}
		});
	}

	/**
	 * The called method at an PreperationAssignmentsChangedEvent.
	 */
	private void assignmentsChanged() {
		addExisitngAssignments();
	}

	/**
	 * @return the view
	 */
	public AssignmentView getView() {
		return view;
	}

	/**
	 * Adds all existing prepreration assignments to the list.
	 */
	private void addExisitngAssignments() {
		if (ScenarioManager.get().experiment().getCurrentExperiment() == null) {
			return;
		}
		
		Map<String, ParameterValueAssignment> sortedMap = new TreeMap<String, ParameterValueAssignment>();


		for (ParameterValueAssignment pva : ScenarioManager.get().experiment().getCurrentExperiment()
				.getExperimentAssignments()) {
			sortedMap.put(pva.getParameter().getFullName(), pva);
		}


		assignmentItems2.clear();

		Iterator<ParameterValueAssignment> iter = sortedMap.values().iterator();
		while (iter.hasNext()) {
			ParameterValueAssignment pva = iter.next();

			AssignmentItem item = new AssignmentItem(pva);
			item.setController(this);
			assignmentItems2.add(item);

		}

		refreshUI();
	}

	public void refreshUI() {
		double key = Metering.start();

		view.getGrid().removeAllRows();
		view.addTableHeader();

		int c = 1;
		for (AssignmentItem ai : assignmentItems2) {
			view.addItem(c, ai);
			c += 2;
		}

		Metering.stop(key);
	}

	public void onValueChange(AssignmentItem item, String value) {
		LOGGER.fine(value);
	}
}
