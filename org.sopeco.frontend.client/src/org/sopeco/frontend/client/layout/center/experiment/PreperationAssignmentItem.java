package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.PreperationAssignmentLoadedEvent;
import org.sopeco.frontend.shared.builder.SimpleEntityFactory;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class PreperationAssignmentItem extends FlowPanel {

	private ParameterDefinition definition;
	private String constantValue;

	private HTML htmlNamespace, htmlName, htmlType;
	private TextBox textboxValue;

	public PreperationAssignmentItem(ParameterDefinition parameterDefinition, String value) {
		definition = parameterDefinition;

		initialize();
	}

	/**
	 * Inits
	 */
	private void initialize() {
		addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_CSS_CLASS);

		htmlNamespace = new HTML(definition.getFullName());
		htmlName = new HTML(definition.getName());
		htmlType = new HTML(": " + definition.getType());

		textboxValue = new TextBox();

		htmlNamespace.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAMESPACE_CSS_CLASS);
		htmlName.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAME_CSS_CLASS);
		htmlType.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_TYPE_CSS_CLASS);

		add(htmlNamespace);
		add(htmlName);
		add(htmlType);
		add(textboxValue);

		htmlNamespace.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("clicked");
				EventControl.get().fireEvent(new PreperationAssignmentLoadedEvent());
			}
		});
	}

	/**
	 * Creates a constantvalueassignment of the attributes.
	 * 
	 * @return
	 */
	public ConstantValueAssignment getConstantValueAssignment() {
		return SimpleEntityFactory.createConstantValueAssignment(definition, constantValue);
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				EventControl.get().fireEvent(new PreperationAssignmentLoadedEvent());
			}
		});
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
