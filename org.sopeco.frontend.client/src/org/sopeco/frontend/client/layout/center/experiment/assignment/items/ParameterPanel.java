package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import java.util.Map;

import org.sopeco.persistence.entities.definition.DynamicValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * 
 * @author Marius Oehler
 * 
 */
public abstract class ParameterPanel extends FlowPanel {

	private static final String PANEL_CSS_CLASS = "parameterPanel";
	private AssignmentItem assignment;

	public ParameterPanel(AssignmentItem item) {
		assignment = item;

		addStyleName(PANEL_CSS_CLASS);
	}

	public AssignmentItem getAssignmentItem() {
		return assignment;
	}

	public abstract ParameterValueAssignment getValueAssignment();

	/**
	 * Returns the configuration map of the DynamicValueAssignment which is
	 * stored in the assignment item.
	 * 
	 * @return
	 */
	protected Map<String, String> getDVAConfiguration() {
		if (!(assignment.getAssignment() instanceof DynamicValueAssignment)) {
			throw new IllegalStateException(
					"The ParameterValueAssignment is not an instance of DynamicValueAssignment.");
		}
		return ((DynamicValueAssignment) assignment.getAssignment()).getConfiguration();
	}

	/**
	 * Creates a DynamicValueAssignment with the current parameter and
	 * configuration map.
	 * 
	 * @return
	 */
	protected DynamicValueAssignment createDynamicValueAssignment() {
		if (!(assignment.getAssignment() instanceof DynamicValueAssignment)) {
			throw new IllegalStateException(
					"The ParameterValueAssignment is not an instance of DynamicValueAssignment.");
		}
		DynamicValueAssignment dva = new DynamicValueAssignment();
		dva.setName(getAssignmentItem().getCurrentVariationName());
		dva.setParameter(getAssignmentItem().getAssignment().getParameter());
		dva.getConfiguration().clear();
		dva.getConfiguration().putAll(getDVAConfiguration());
		return dva;
	}
}
