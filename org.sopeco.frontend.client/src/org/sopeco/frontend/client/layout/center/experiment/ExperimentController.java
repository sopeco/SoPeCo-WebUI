/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.frontend.client.layout.center.experiment;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.ExperimentChangedEvent;
import org.sopeco.frontend.client.event.handler.ExperimentChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.center.experiment.assignment.AssignmentController;
import org.sopeco.frontend.client.layout.center.experiment.assignment.AssignmentController.Type;
import org.sopeco.frontend.client.layout.center.experiment.assignment.PreparationController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.InputDialog;
import org.sopeco.frontend.client.layout.popups.InputDialogHandler;
import org.sopeco.frontend.client.layout.popups.InputDialogValidator;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.shared.helper.ExtensionTypes;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class ExperimentController implements ICenterController, ValueChangeHandler<String>, ClickHandler,
		ExperimentChangedEventHandler, InputDialogHandler, InputDialogValidator {

	private static final Logger LOGGER = Logger.getLogger(ExperimentController.class.getName());
	private static final String ENV_TREE_CSS_CLASS = "expEnvTree";

	private ExperimentView view;
	private ExperimentExtensionController explorationExtController;
	// private ParameterTreeController treeController;
	private TerminationController terminationController;
	private ExperimentEnvironmentTree expEnvironmentTree;

	private AssignmentController assignmentPreperation, assignmentExperiment;
	private PreparationController preparationController;

	private ExperimentTabPanel tabPanel;

	private InputDialog inputClone, inputRename;

	public ExperimentController() {
		FrontEndResources.loadExperimentCSS();

		tabPanel = new ExperimentTabPanel();

		explorationExtController = new ExperimentExtensionController(this, ExperimentView.EXP_SETTINGS_PANEL_WIDTH);

		// assignmentPreperation = new AssignmentController(Type.PREPERATION);
		assignmentExperiment = new AssignmentController(Type.EXPERIMENT);

		preparationController = new PreparationController();

		terminationController = new TerminationController();

		view = new ExperimentView();
		// treeController = new ParameterTreeController();
		expEnvironmentTree = new ExperimentEnvironmentTree();

		getSettingsView().addExtensionView(explorationExtController.getView());
		getSettingsView().add(terminationController.getView());
		// getParameterView().add(assignmentPreperation.getView());
		// getParameterView().add(preparationController.getView());

		// TODO clean this up
		FlowPanel rightCol = new FlowPanel();
		rightCol.getElement().getStyle().setPosition(Position.ABSOLUTE);
		rightCol.getElement().getStyle().setTop(0, Unit.PX);
		rightCol.getElement().getStyle().setLeft(400, Unit.PX);
		rightCol.getElement().getStyle().setRight(0, Unit.PX);
		rightCol.getElement().getStyle().setBottom(0, Unit.PX);
		rightCol.getElement().getStyle().setOverflow(Overflow.AUTO);

		rightCol.add(preparationController.getView());
		rightCol.add(assignmentExperiment.getView());

		getParameterView().add(rightCol);

		// getParameterView().add(treeController.getView());
		getParameterView().add(expEnvironmentTree.getView());

		expEnvironmentTree.getView().addStyleName(ENV_TREE_CSS_CLASS);

		explorationExtController.setHeadline(R.get("explStrategy"));

		explorationExtController.setExtensionType(ExtensionTypes.EXPLORATIONSTRATEGY);

		getSettingsView().getImgRemove().addClickHandler(this);
		getSettingsView().getImgRename().addClickHandler(this);
		getSettingsView().getImgDuplicate().addClickHandler(this);

		registerEventHandlers();
	}

	/**
	 * Returns the settings view of the experiment series.
	 * 
	 * @return
	 */
	public ExperimentSettingsView getSettingsView() {
		return tabPanel.getSettingsView();
	}

	/**
	 * Returns the parameter view of the experiment series.
	 * 
	 * @return
	 */
	public ExperimentParameterView getParameterView() {
		return tabPanel.getParameterView();
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == getSettingsView().getImgRemove()) {
			removeExperiment();
		} else if (event.getSource() == getSettingsView().getImgRename()) {
			showRenameDialog();
		} else if (event.getSource() == getSettingsView().getImgDuplicate()) {
			duplicateExperiment();
		}
	}

	@Override
	public void onSwitchTo() {
	}
	
	private void duplicateExperiment() {
		if (inputClone == null) {
			inputClone = new InputDialog(R.get("cloneExperiment"), R.get("nameForExperimentClone") + ":");
			inputClone.addHandler(this);
			inputClone.setValidator(this);
		}
		inputClone.setValue("");
		inputClone.center();
	}

	private void showRenameDialog() {
		if (inputRename == null) {
			inputRename = new InputDialog(R.get("renameExperiment"), R.get("newExpName") + ":");
			inputRename.addHandler(this);
			inputRename.setValidator(this);
		}
		inputRename.setValue(Manager.get().getSelectedExperiment());
		inputRename.center();
	}

	@Override
	public void onInput(InputDialog source, String value) {
		if (source == inputClone) {
			ScenarioManager.get().experiment().cloneCurrentExperiment(value);
		} else if (source == inputRename) {
			ScenarioManager.get().experiment().renameCurrentExpSeries(value);
		}
	}

	@Override
	public boolean validate(InputDialog source, String text) {
		// if (source == inputRename) {
		if (text.isEmpty()) {
			source.showWarning("The name of an Experimentseries must not be empty.");
			return false;
		}
		for (ExperimentSeriesDefinition esd : ScenarioManager.get().specification().getSpecification()
				.getExperimentSeriesDefinitions()) {
			if (text.equals(esd.getName())) {
				source.showWarning("There is already a ExperimentSeries with this name.");
				return false;
			}
		}
		// }
		source.hideWarning();
		return true;
	}

	private void removeExperiment() {
		Confirmation.confirm(R.get("removeTihsExp"), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ScenarioManager.get().experiment().removeCurrentExperimentSeries();

				List<ExperimentSeriesDefinition> expList = ScenarioManager.get().experiment()
						.getExperimentsOfCurrentSpecififcation();

				if (expList.isEmpty()) {
					MainLayoutPanel.get().getViewSwitch().switchTo(CenterType.Specification);
				} else {
					MainLayoutPanel.get().getViewSwitch().switchToExperiment(expList.get(0).getName());
				}
			}
		});
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		// if (event.getSource() == getSettingsView().getTextboxName()) {
		// ScenarioManager.get().experiment().renameCurrentExpSeries(event.getValue());
		// }
	}

	/**
	 * Register handlers to catch events.
	 */
	private void registerEventHandlers() {
		EventControl.get().addHandler(ExperimentChangedEvent.TYPE, this);
	}

	/**
	 * Event method of the ExperimentChanged Event.
	 * 
	 * @param experimentName
	 */

	@Override
	public void onExperimentChanged(ExperimentChangedEvent event) {
		LOGGER.fine("Change experiment to '" + event.getExperimentName() + "'");

		ExperimentSeriesDefinition experiment = ScenarioManager.get().getBuilder().getSpecificationBuilder()
				.getExperimentSeries(event.getExperimentName());

		getSettingsView().setExperimentName(experiment.getName());

		String explorationName = ScenarioManager.get().experiment().getCurrentExperiment().getExplorationStrategy()
				.getName();
		Map<String, String> explorationConfig = ScenarioManager.get().experiment().getCurrentExperiment()
				.getExplorationStrategy().getConfiguration();

		explorationExtController.setExtension(explorationName);
		explorationExtController.setConfigMap(explorationConfig);

		expEnvironmentTree.generateTree();
	}

	@Override
	public Widget getView() {
		return tabPanel;
	}

	public ExperimentEnvironmentTree getEnvironmentTree() {
		return expEnvironmentTree;
	}

	@Override
	public void reset() {
		view.reset();
	}

	/**
	 * @return the explorationExtController
	 */
	public ExperimentExtensionController getExplorationExtController() {
		return explorationExtController;
	}

}
