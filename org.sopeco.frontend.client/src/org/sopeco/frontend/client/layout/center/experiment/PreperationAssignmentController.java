package org.sopeco.frontend.client.layout.center.experiment;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.PreperationAssignmentLoadedEvent;
import org.sopeco.frontend.client.event.handler.PreperationAssignmentLoadedEventHandler;
import org.sopeco.frontend.client.helper.ElementPropertyAligner;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentController {

	private PreperationAssignmentView view;
	private List<PreperationAssignmentItem> preperationItems;
	private ElementPropertyAligner namespacePropertyAligner, typePropertyAligner;

	public PreperationAssignmentController() {
		preperationItems = new ArrayList<PreperationAssignmentItem>();

		namespacePropertyAligner = new ElementPropertyAligner();
		typePropertyAligner = new ElementPropertyAligner();

		registerHandler();
	}

	/**
	 * Register some event handlers.
	 */
	private void registerHandler() {
		EventControl.get().addHandler(PreperationAssignmentLoadedEvent.TYPE,
				new PreperationAssignmentLoadedEventHandler() {
					@Override
					public void onAssignmentLoadedEvent(PreperationAssignmentLoadedEvent event) {
						assignmentLoadedEvent(event);
					}
				});
	}

	/**
	 * The called method at an PreperationAssignmentLoadedEvent.
	 */
	private void assignmentLoadedEvent(PreperationAssignmentLoadedEvent event) {
		namespacePropertyAligner.alignWith();
		typePropertyAligner.offsetWidth();
	}

	/**
	 * @return the view
	 */
	public PreperationAssignmentView getView() {
		if (view == null) {
			view = new PreperationAssignmentView();
		}
		return view;
	}

	/**
	 * 
	 * @param definition
	 */
	public void addAssignment(ParameterDefinition definition) {
		addAssignment(definition, "");
	}

	/**
	 * 
	 * @param definition
	 * @param value
	 */
	public void addAssignment(ParameterDefinition definition, String value) {
		PreperationAssignmentItem item = new PreperationAssignmentItem(definition, value);

		preperationItems.add(item);

		namespacePropertyAligner.addElement(item.getHtmlNamespace().getElement());
		typePropertyAligner.addElement(item.getHtmlType().getElement(), item.getHtmlNamespace().getElement(), item
				.getHtmlName().getElement());

		getView().add(item);
	}

}
