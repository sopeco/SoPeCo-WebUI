package org.sopeco.frontend.client.layout.center.specification;

import java.util.HashMap;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
class AssignmentController implements BlurHandler {

	private AssignmentView view;
	private HashMap<String, AssignmentItem> assignmentMap;

	public AssignmentController() {
		reset();
	}

	/**
	 * Creates a new assignmentView.
	 */
	public void reset() {
		assignmentMap = new HashMap<String, AssignmentItem>();

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
		assignmentMap.put(assignment.getFullName(), assignment);
		view.add(assignment);

		assignment.addBlurHandler(this);
	}

	/**
	 * Remmoves the assignment which have the same namespace + name.
	 */
	public void removeAssignment(AssignmentItem assignment) {
		if (assignmentMap.containsKey(assignment.getFullName())) {
			assignmentMap.get(assignment.getFullName()).removeFromParent();
			assignmentMap.remove(assignment.getFullName());
		}
	}

	@Override
	public void onBlur(BlurEvent event) {
		AssignmentItem item = (AssignmentItem) event.getSource();
		
		Window.alert(item.getFullName());
	}
}
