package org.sopeco.webui.client.layout.center.experiment;

import java.util.ArrayList;
import java.util.List;

import org.sopeco.persistence.entities.definition.AnalysisConfiguration;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.webui.client.layout.environment.EnvTreeItem;
import org.sopeco.webui.client.layout.environment.EnvironmentTree;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;
import org.sopeco.webui.client.widget.TreeItem;

public class AnalysisDefinitionTree extends EnvironmentTree {
	private AnalysisController analysisController;

	public AnalysisDefinitionTree() {
		super(false, R.lang.independentParameters());
		getView().setFirstInfoText(R.lang.addAsIndependentParameter());
		getView().setShowObservableButtonVisible(false);
		
	}

	@Override
	public boolean isFirstChecked(ParameterDefinition parameter) {
		List<AnalysisConfiguration> analysisConfigs = ScenarioManager.get().experiment().getCurrentExperiment()
				.getExplorationStrategy().getAnalysisConfigurations();
		if (!analysisConfigs.isEmpty()) {
			for (ParameterDefinition pDef : analysisConfigs.get(0).getIndependentParameters()) {
				if (parameter.equals(pDef)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSecondChecked(ParameterDefinition parameter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(ECheckBox checkbox, EnvTreeItem item, boolean value) {
		if (value) {
			getAnalysisController().getIndependentParameters().addAll(getParameters(item));
		} else {
			getAnalysisController().getIndependentParameters().removeAll(getParameters(item));
		}
		ScenarioManager.get().experiment().saveExperimentConfig(getAnalysisController().getParentController());
	}

	private List<ParameterDefinition> getParameters(TreeItem item) {
		List<ParameterDefinition> list = new ArrayList<ParameterDefinition>();
		if ((item instanceof EnvTreeItem) && (((EnvTreeItem) item).getParameter() != null)) {
			list.add(((EnvTreeItem) item).getParameter());
		} else {
			for (TreeItem child : item.getChildrenItems()) {
				list.addAll(getParameters(child));
			}
		}
		return list;
	}

	/**
	 * @return the analysisController
	 */
	public AnalysisController getAnalysisController() {
		return analysisController;
	}

	/**
	 * @param analysisController the analysisController to set
	 */
	public void setAnalysisController(AnalysisController analysisController) {
		this.analysisController = analysisController;
	}

}
