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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.PreperationAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.event.handler.PreperationAssignmentsChangedEventHandler;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.widget.grid.EditGridHandler;
import org.sopeco.frontend.client.widget.grid.EditGridItem;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreparationController implements EditGridHandler {

	private PreparationView view;

	public PreparationController() {
		init();
	}

	private void init() {
		view = new PreparationView();

		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				addExistingAssignments();
			}
		});

		EventControl.get().addHandler(PreperationAssignmentsChangedEvent.TYPE,
				new PreperationAssignmentsChangedEventHandler() {
					@Override
					public void onAssignmentChangedEvent(PreperationAssignmentsChangedEvent event) {
						addExistingAssignments();
					}
				});
	}

	public PreparationView getView() {
		return view;
	}

	private void addExistingAssignments() {
		if (ScenarioManager.get().experiment().getCurrentExperiment() == null) {
			return;
		}

		assignmentMap.clear();

		Map<String, ParameterValueAssignment> sortedMap = new TreeMap<String, ParameterValueAssignment>();
		for (ConstantValueAssignment cva : ScenarioManager.get().experiment().getCurrentExperiment()
				.getPreperationAssignments()) {
			sortedMap.put(cva.getParameter().getFullName(), cva);
		}

		Iterator<ParameterValueAssignment> iter = sortedMap.values().iterator();
		while (iter.hasNext()) {
			ParameterValueAssignment pva = iter.next();
			addAssignment(pva);
		}

		refreshUI();
	}

	private void addAssignment(ParameterValueAssignment valueAssignment) {
		EditGridItem item;

		if (valueAssignment instanceof ConstantValueAssignment) {
			item = new EditGridItem((ConstantValueAssignment) valueAssignment);
		} else {
			item = new EditGridItem(valueAssignment.getParameter(), "");
		}

		item.setHandler(this);
		addAssignment(item);
	}

	TreeMap<String, EditGridItem> assignmentMap = new TreeMap<String, EditGridItem>();

	/**
	 * Adding a new assignmentitem to the assignmentList and to the
	 * assignmentListPanel.
	 * 
	 * @param assignment
	 */
	private void addAssignment(EditGridItem item) {
		item.setHandler(this);
		assignmentMap.put(item.getFullName(), item);
	}

	/**
	 * Clear the assignmentListPanel and adds all assignments again.
	 */
	public void refreshUI() {
		double metering = Metering.start();

		view.getGrid().resizeRows(assignmentMap.size() + 1);
		int c = 1;
		for (String key : assignmentMap.keySet()) {
			view.getGrid().addItem(c++, assignmentMap.get(key));
		}
		view.getHtmlNoAssignments().setVisible(c == 1);

		Metering.stop(metering);
	}

	@Override
	public void onValueChange(EditGridItem item) {
		ScenarioManager.get().experiment().setPreperationAssignmentValue(item.getParameter(), item.getValue());
	}
}
