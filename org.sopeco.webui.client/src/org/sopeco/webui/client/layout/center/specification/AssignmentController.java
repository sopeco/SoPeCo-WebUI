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
package org.sopeco.webui.client.layout.center.specification;

import java.util.TreeMap;

import org.sopeco.webui.client.layout.popups.Message;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.widget.grid.EditGridHandler;
import org.sopeco.webui.client.widget.grid.EditGridItem;
import org.sopeco.webui.shared.helper.Metering;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentController implements EditGridHandler {

	private AssignmentView view;
	private TreeMap<String, EditGridItem> assignmentMap;

	public AssignmentController() {
		init();
	}

	private void init() {
		assignmentMap = new TreeMap<String, EditGridItem>();
		view = new AssignmentView();
	}

	public void reset() {

	}

	/**
	 * Returns the current view.
	 * 
	 * @return AssignmentView
	 */
	public Widget getAssignmentView() {
		return view.getInScrollPanel();
	}

	/**
	 * Adding a new assignmentitem to the assignmentList and to the
	 * assignmentListPanel.
	 * 
	 * @param assignment
	 */
	public void addAssignment(EditGridItem item) {
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

	/**
	 * Removes the assignment which have the same FullName.
	 */
	public void removeAssignment(EditGridItem assignment) {
		assignmentMap.remove(assignment.getFullName());
	}

	/**
	 * Removes all existing assignments.
	 */
	public void clearAssignments(boolean clearMap) {
		if (clearMap) {
			assignmentMap.clear();
		}
	}

	// public void onValueChange(AssignmentItem item) {
	// if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(),
	// item.getName(), item.getValue())) {
	// ScenarioManager.get().storeScenario();
	// } else {
	// Message.error("error");
	// }
	// }

	@Override
	public void onValueChange(EditGridItem item) {
		if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(), item.getName(), item.getValue())) {
			ScenarioManager.get().storeScenario();
		} else {
			Message.error("error");
		}
	}
}
