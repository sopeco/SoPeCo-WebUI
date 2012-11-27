package org.sopeco.frontend.client.layout.center.experiment.assignment.items;

import org.sopeco.persistence.entities.definition.ParameterValueAssignment;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class EmptyParameterPanel extends ParameterPanel {

	public EmptyParameterPanel(AssignmentItem item) {
		super(item);
		setVisible(false);
	}

	@Override
	public ParameterValueAssignment getValueAssignment() {
		return createDynamicValueAssignment();
	}

}
