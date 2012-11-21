package org.sopeco.frontend.client.layout.center.specification;

import java.util.TreeMap;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentController {

	private AssignmentView view;
	private TreeMap<String, AssignmentItem> assignmentMap;

	public AssignmentController() {
		init();
	}

	private void init() {
		assignmentMap = new TreeMap<String, AssignmentItem>();
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
	public void addAssignment(AssignmentItem assignment) {
		assignment.setController(this);
		assignmentMap.put(assignment.getFullName(), assignment);
	}

	/**
	 * Clear the assignmentListPanel and adds all assignments again.
	 */
	public void refreshUI() {
		double metering = Metering.start();

		view.getItemTable().resizeRows(assignmentMap.size() + 1);
		int c = 1;
		for (String key : assignmentMap.keySet()) {
			view.setAssignmentItem(c++, assignmentMap.get(key));
		}
		view.getHtmlNoAssignments().setVisible(c == 1);

		Metering.stop(metering);
	}

	/**
	 * Removes the assignment which have the same FullName.
	 */
	public void removeAssignment(AssignmentItem assignment) {
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

	public void onValueChange(AssignmentItem item) {
		if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(), item.getName(), item.getValue())) {
			ScenarioManager.get().storeScenario();
		} else {
			Message.error("error");
		}
	}
}
