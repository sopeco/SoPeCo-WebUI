package org.sopeco.frontend.client.layout.center.specification;

import java.util.TreeMap;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.shared.helper.Metering;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentController implements BlurHandler {

	private AssignmentView view;
	private TreeMap<String, AssignmentItem> assignmentMap;

	public AssignmentController() {
		reset();
	}

	/**
	 * Creates a new assignmentView.
	 */
	public void reset() {
		assignmentMap = new TreeMap<String, AssignmentItem>();

		view = new AssignmentView();
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
		double metering = Metering.start();

		assignmentMap.put(assignment.getFullName(), assignment);

		assignment.addBlurHandler(this);

		Metering.stop(metering);
	}

	/**
	 * Clear the assignmentListPanel and adds all assignments again.
	 */
	public void refreshAssignmentListPanel() {
		double metering = Metering.start();

		view.clearAssignments();
		for (String key : assignmentMap.keySet()) {
			view.addAssignmentitem(assignmentMap.get(key));
		}

		Metering.stop(metering);
	}

	/**
	 * Remmoves the assignment which have the same namespace + name.
	 */
	public void removeAssignment(AssignmentItem assignment) {
		if (assignmentMap.containsKey(assignment.getFullName())) {
			view.removeAssignmentitem(assignmentMap.get(assignment.getFullName()));
			assignmentMap.remove(assignment.getFullName());
		}
	}

	@Override
	public void onBlur(BlurEvent event) {
		AssignmentItem item = (AssignmentItem) event.getSource();

		if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(), item.getName(),
				item.getTextboxValue().getText())) {
			ScenarioManager.get().storeScenario();
		} else {
			Message.error("error");
		}
	}

	/**
	 * Removes all existing assignments.
	 */
	public void clearAssignments() {
		assignmentMap.clear();
		view.clearAssignments();
	}
}
