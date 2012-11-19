package org.sopeco.frontend.client.layout.center.experiment;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.environment.EnvTreeItem;
import org.sopeco.frontend.client.layout.environment.EnvironmentTree;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.persistence.entities.definition.ParameterDefinition;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentEnvironmentTree extends EnvironmentTree {

	public ExperimentEnvironmentTree() {
		super(true);

		getView().setFirstInfoText(R.get("addToExpAssignments"));
		getView().setSecondInfoText(R.get("addToPrepAssignments"));

		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, new ExperimentChangedEventHandler() {
			@Override
			public void onExperimentChanged(ExperimentChangedEvent event) {
				generateTree();
			}
		});
	}

	@Override
	public boolean isFirstChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().experiment().isExperimentAssignment(parameter);
	}

	@Override
	public boolean isSecondChecked(ParameterDefinition parameter) {
		return ScenarioManager.get().experiment().isPreperationAssignment(parameter);
	}

	@Override
	public void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value) {
		if (checkbox == ECheckBox.FIRST) {
			if (value) {
				ScenarioManager.get().experiment().addExperimentAssignment(item.getParameter());
			} else {
				ScenarioManager.get().experiment().removeExperimentAssignment(item.getParameter());
			}
		} else {
			if (value) {
				ScenarioManager.get().experiment().addPreperationAssignment(item.getParameter());
			} else {
				ScenarioManager.get().experiment().removePreperationAssignment(item.getParameter());
			}
		}
	}

}
