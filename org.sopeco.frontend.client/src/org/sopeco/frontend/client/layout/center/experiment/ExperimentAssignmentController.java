package org.sopeco.frontend.client.layout.center.experiment;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentLoadedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentAssignmentLoadedEventHandler;
import org.sopeco.frontend.client.helper.ElementPropertyAligner;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentController {
	private ExperimentAssignmentView view;
	private List<ExperimentAssignmentItem> assignmentItems;

	private ElementPropertyAligner namespacePropertyAligner, typePropertyAligner;

	public ExperimentAssignmentController() {
		assignmentItems = new ArrayList<ExperimentAssignmentItem>();

		namespacePropertyAligner = new ElementPropertyAligner();
		typePropertyAligner = new ElementPropertyAligner();

		registerHandler();
	}

	/**
	 * Register some event handlers.
	 */
	private void registerHandler() {
		EventControl.get().addHandler(ExperimentAssignmentLoadedEvent.TYPE,
				new ExperimentAssignmentLoadedEventHandler() {
					@Override
					public void onExperimentAssignmentLoadedEvent(ExperimentAssignmentLoadedEvent event) {
						assignmentLoadedEvent(event);
					}
				});
	}

	/**
	 * The called method at an PreperationAssignmentLoadedEvent.
	 */
	private void assignmentLoadedEvent(ExperimentAssignmentLoadedEvent event) {
		namespacePropertyAligner.alignWith();
		typePropertyAligner.offsetWidth();
	}

	/**
	 * Returns the view.
	 * 
	 * @return
	 */
	public ExperimentAssignmentView getView() {
		if (view == null) {
			view = new ExperimentAssignmentView();
		}
		return view;
	}

	/**
	 * 
	 * @param definition
	 * @param value
	 */
	public void addAssignment(ParameterValueAssignment valueAssignment) {
		ExperimentAssignmentItem item = new ExperimentAssignmentItem();

		assignmentItems.add(item);

		namespacePropertyAligner.addElement(item.getHtmlNamespace().getElement());
		typePropertyAligner.addElement(item.getHtmlType().getElement(), item.getHtmlNamespace().getElement(), item
				.getHtmlName().getElement());

		getView().add(item);
	}
}
