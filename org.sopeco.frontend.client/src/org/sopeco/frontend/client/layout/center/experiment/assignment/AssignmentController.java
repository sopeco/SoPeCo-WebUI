package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentAssignmentsChangedEventHandler;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.experiment.assignment.items.AssignmentItem;
import org.sopeco.frontend.client.model.ScenarioManager;
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
