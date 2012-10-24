package org.sopeco.frontend.client.layout.center.experiment;

import java.util.Set;
import java.util.TreeSet;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentAssignmentLoadedEvent;
import org.sopeco.frontend.client.extensions.Extensions;
import org.sopeco.frontend.client.widget.ComboBox;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentAssignmentItem extends FlowPanel {

	private ParameterValueAssignment valueAssignment;
	private String constantValue;

	private HTML htmlNamespace, htmlName, htmlType;
	private ComboBox combobox;

	public ExperimentAssignmentItem() {
		initialize();
	}

	/**
	 * Inits
	 */
	private void initialize() {
		addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_CSS_CLASS);

		htmlNamespace = new HTML("{namespace}");
		htmlName = new HTML("{name}");
		htmlType = new HTML(": " + "{type}");
		combobox = new ComboBox();

		htmlNamespace.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAMESPACE_CSS_CLASS);
		htmlName.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_NAME_CSS_CLASS);
		htmlType.addStyleName(ExperimentParameterView.ASSIGNMENT_ITEM_TYPE_CSS_CLASS);

		combobox.setEditable(false);
		initCombobox();

		add(htmlNamespace);
		add(htmlName);
		add(htmlType);
		add(combobox);
	}

	/**
	 * Adds the values to the combobox.
	 */
	private void initCombobox() {

		Set<String> keySetConst = new TreeSet<String>(Extensions.get().getExtensions(ExtensionTypes.CONSTANTASSIGNMENT)
				.keySet());

		for (String key : keySetConst) {
			combobox.addItem("Const: " + key);
		}

		Set<String> keySetVar = new TreeSet<String>(Extensions.get().getExtensions(ExtensionTypes.PARAMETERVARIATION)
				.keySet());

		for (String key : keySetVar) {
			combobox.addItem(key);
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

	@Override
	protected void onLoad() {
		super.onLoad();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				EventControl.get().fireEvent(new ExperimentAssignmentLoadedEvent());
			}
		});
	}
}
