package org.sopeco.frontend.client.layout.center.experiment.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentRenderedEvent;
import org.sopeco.frontend.client.event.ExperimentAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.PreperationAssignmentRenderedEvent;
import org.sopeco.frontend.client.event.PreperationAssignmentsChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentAssignmentRenderedEventHandler;
import org.sopeco.frontend.client.event.handler.ExperimentAssignmentsChangedEventHandler;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.event.handler.PreperationAssignmentRenderedEventHandler;
import org.sopeco.frontend.client.event.handler.PreperationAssignmentsChangedEventHandler;
import org.sopeco.frontend.client.helper.ElementPropertyAligner;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class AssignmentController {

	/** Assignment Type. */
	public enum Type {
		/** */
		EXPERIMENT, PREPERATION
	}

	private Type assignmentType;
	private AssignmentView view;

	private List<AssignmentItem> assignmentItems;
	private ElementPropertyAligner namespacePropertyAligner, typePropertyAligner;

	public AssignmentController(Type type) {
		assignmentType = type;

		String headline = "";
		switch (type) {
		case PREPERATION:
			headline = R.get("prepAssignments");
			break;
		default:
		case EXPERIMENT:
			headline = R.get("expAssignsments");
		}

		assignmentItems = new ArrayList<AssignmentItem>();
		namespacePropertyAligner = new ElementPropertyAligner();
		typePropertyAligner = new ElementPropertyAligner();

		view = new AssignmentView(headline);

		if (type == Type.PREPERATION) {
			registerHandlerPreperation();
		} else if (type == Type.EXPERIMENT) {
			registerHandlerExperiment();
		}
		registerHandlerCommon();
	}

	/**
	 * 
	 */
	private void registerHandlerPreperation() {
		EventControl.get().addHandler(PreperationAssignmentRenderedEvent.TYPE,
				new PreperationAssignmentRenderedEventHandler() {
					@Override
					public void onAssignmentLoadedEvent(PreperationAssignmentRenderedEvent event) {
						assignmentRenderedEvent();
					}
				});
		EventControl.get().addHandler(PreperationAssignmentsChangedEvent.TYPE,
				new PreperationAssignmentsChangedEventHandler() {
					@Override
					public void onAssignmentChangedEvent(PreperationAssignmentsChangedEvent event) {
						assignmentsChanged();
					}
				});
	}

	/**
	 * 
	 */
	private void registerHandlerExperiment() {
		EventControl.get().addHandler(ExperimentAssignmentRenderedEvent.TYPE,
				new ExperimentAssignmentRenderedEventHandler() {
					@Override
					public void onExperimentAssignmentLoadedEvent(ExperimentAssignmentRenderedEvent event) {
						assignmentRenderedEvent();
					}
				});
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
	 * The called method at an PreperationAssignmentRenderedEvent.
	 */
	private void assignmentRenderedEvent() {
		namespacePropertyAligner.alignWith();
		typePropertyAligner.offsetWidth();
	}

	/**
	 * @return the view
	 */
	public AssignmentView getView() {
		return view;
	}

	/**
	 * Removes all added assignments.
	 */
	private void clearAssignments() {
		for (AssignmentItem item : assignmentItems) {
			item.removeFromParent();
		}
		assignmentItems.clear();
	}

	/**
	 * Adds all existing prepreration assignments to the list.
	 */
	private void addExisitngAssignments() {
		if (ScenarioManager.get().experiment().getCurrentExperiment() == null) {
			return;
		}
		clearAssignments();

		Map<String, ParameterValueAssignment> sortedMap = new TreeMap<String, ParameterValueAssignment>();

		if (assignmentType == Type.EXPERIMENT) {
			for (ParameterValueAssignment pva : ScenarioManager.get().experiment().getCurrentExperiment()
					.getExperimentAssignments()) {
				sortedMap.put(pva.getParameter().getFullName(), pva);
			}
		} else {
			for (ConstantValueAssignment cva : ScenarioManager.get().experiment().getCurrentExperiment()
					.getPreperationAssignments()) {
				sortedMap.put(cva.getParameter().getFullName(), cva);
			}
		}

		for (ParameterValueAssignment pva : sortedMap.values()) {
			addAssignment(pva);
		}
	}

	/**
	 * 
	 * @param definition
	 * @param value
	 */
	public void addAssignment(ParameterValueAssignment valueAssignment) {
		AssignmentItem item;

		if (assignmentType == Type.EXPERIMENT) {
			item = new ExperimentAssignmentItem(valueAssignment);
		} else {
			item = new PreperationAssignmentItem(valueAssignment);
		}

		assignmentItems.add(item);

		namespacePropertyAligner.addElement(item.getHtmlNamespace().getElement());
		typePropertyAligner.addElement(item.getHtmlType().getElement(), item.getHtmlNamespace().getElement(), item
				.getHtmlName().getElement());

		getView().add(item);
	}
}
