package org.sopeco.frontend.client.layout.center.experiment.assignment;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.layout.center.experiment.ExperimentParameterView;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class AssignmentItem extends FlowPanel {
	/** The ParameterAssignment of this item. */
	protected ParameterValueAssignment assignment;

	private HTML htmlNamespace, htmlName, htmlType;

	private boolean fireOnLoadEvent = true;

	public AssignmentItem(ParameterValueAssignment valueAssignment) {
		assignment = valueAssignment;

		initialize();
	}

	/**
	 * Inits
	 */
	private void initialize() {
		addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_CSS_CLASS);

		htmlNamespace = new HTML(assignment.getParameter().getNamespace().getFullName());
		htmlName = new HTML(assignment.getParameter().getName());
		htmlType = new HTML(": " + assignment.getParameter().getType());

		htmlNamespace.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAMESPACE_CSS_CLASS);
		htmlName.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAME_CSS_CLASS);
		htmlType.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_TYPE_CSS_CLASS);

		add(htmlNamespace);
		add(htmlName);
		add(htmlType);

		initValueArea();
	}

	/**
	 * @param fireOnLoadEvent
	 *            the fireOnLoadEvent to set
	 */
	public void setFireOnLoadEvent(boolean fireOnLoadEvent) {
		this.fireOnLoadEvent = fireOnLoadEvent;
	}

	/**
	 * Initialization of the area where the user can edit the values.
	 */
	protected abstract void initValueArea();

	/**
	 * The Event which is fired, after this element was rendered in browser.
	 * 
	 * @return
	 */
	protected abstract GwtEvent<?> getOnLoadEvent();

	@Override
	protected void onLoad() {
		super.onLoad();

		if (fireOnLoadEvent) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					EventControl.get().fireEvent(getOnLoadEvent());
				}
			});
		}
	}

	/**
	 * @return the htmlNamespace
	 */
	public HTML getHtmlNamespace() {
		return htmlNamespace;
	}

	/**
	 * @return the htmlName
	 */
	public HTML getHtmlName() {
		return htmlName;
	}

	/**
	 * @return the htmlType
	 */
	public HTML getHtmlType() {
		return htmlType;
	}
}
