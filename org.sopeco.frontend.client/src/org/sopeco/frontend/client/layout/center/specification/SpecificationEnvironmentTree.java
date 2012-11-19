package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.frontend.client.layout.environment.EnvTreeItem;
import org.sopeco.frontend.client.layout.environment.EnvironmentTree;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

import com.google.gwt.core.client.GWT;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationEnvironmentTree extends EnvironmentTree {

	public SpecificationEnvironmentTree() {
		super(false);
		
		getView().setFirstInfoText(R.get("addToInitAssignments"));
	}

	@Override
	public void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value) {
		ChangeType type;
		if (value) {
			type = ChangeType.Added;
		} else {
			type = ChangeType.Removed;
		}

		GWT.log("add: " + item.getPath());
		InitialAssignmentChangedEvent changeEvent = new InitialAssignmentChangedEvent(item.getPath(), type);
		EventControl.get().fireEvent(changeEvent);
	}

	@Override
	public boolean isFirstChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().getBuilder().getSpecificationBuilder().containsInitialAssignment(parameter);
	}

	@Override
	public boolean isSecondChecked(ParameterDefinition parameter) {
		return false;
	}
}
