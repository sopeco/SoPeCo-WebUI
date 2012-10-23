package org.sopeco.frontend.client.layout.center.experiment;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.PreperationAssignmentLoadedEvent;
import org.sopeco.frontend.client.event.handler.PreperationAssignmentLoadedEventHandler;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.dom.client.Style.Unit;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentController {

	private PreperationAssignmentView view;
	private List<PreperationAssignmentItem> preperationItems;

	public PreperationAssignmentController() {
		preperationItems = new ArrayList<PreperationAssignmentItem>();

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
		updateHTMLNamespaceWidth();
		updateHTMLTypeWidth();
	}

	/**
	 * Sets the width of all namespace-HTML elements of the assignment items to
	 * the same width.
	 */
	private void updateHTMLNamespaceWidth() {
		int widestHTML = 0;
		for (PreperationAssignmentItem item : preperationItems) {
			item.getHtmlNamespace().getElement().getStyle().clearWidth();

			if (item.getHtmlNamespace().getOffsetWidth() > widestHTML) {
				widestHTML = item.getHtmlNamespace().getOffsetWidth();
			}
		}

		for (PreperationAssignmentItem item : preperationItems) {
			item.getHtmlNamespace().getElement().getStyle().setWidth(widestHTML, Unit.PX);
		}
	}

	/**
	 * Sets the width of all namespace-HTML elements of the assignment items to
	 * the same width.
	 */
	private void updateHTMLTypeWidth() {
		int widestHTML = 0;
		for (PreperationAssignmentItem item : preperationItems) {
			item.getHtmlType().getElement().getStyle().clearWidth();

			int tempWidth = item.getHtmlNamespace().getOffsetWidth();
			tempWidth += item.getHtmlName().getOffsetWidth();
			tempWidth += item.getHtmlType().getOffsetWidth();

			if (tempWidth > widestHTML) {
				widestHTML = tempWidth;
			}
		}

		for (PreperationAssignmentItem item : preperationItems) {
			int widthToSet = widestHTML;
			widthToSet -= item.getHtmlNamespace().getOffsetWidth();
			widthToSet -= item.getHtmlName().getOffsetWidth();
			item.getHtmlType().getElement().getStyle().setWidth(widthToSet, Unit.PX);
		}
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

		getView().add(item);
	}

}
