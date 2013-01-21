package org.sopeco.frontend.client.layout.center.specification;

import java.util.TreeMap;

import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.widget.grid.EditGridHandler;
import org.sopeco.frontend.client.widget.grid.EditGridItem;
import org.sopeco.frontend.shared.helper.Metering;

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

//	public void onValueChange(AssignmentItem item) {
//		if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(), item.getName(), item.getValue())) {
//			ScenarioManager.get().storeScenario();
//		} else {
//			Message.error("error");
//		}
//	}
	
	@Override
	public void onValueChange(EditGridItem item) {
		if (ScenarioManager.get().changeInitAssignmentValue(item.getNamespace(), item.getName(), item.getValue())) {
			ScenarioManager.get().storeScenario();
		} else {
			Message.error("error");
		}
	}
}
