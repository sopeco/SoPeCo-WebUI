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
package org.sopeco.frontend.client.layout.center.specification;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.frontend.client.event.handler.InitialAssignmentChangedEventHandler;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.InputDialog;
import org.sopeco.frontend.client.layout.popups.InputDialogHandler;
import org.sopeco.frontend.client.layout.popups.InputDialogValidator;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.widget.grid.EditGridItem;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationController implements ICenterController, ClickHandler, InputDialogHandler,
		InputDialogValidator {

	private static final String TREE_CSS_CLASS = "specificationTreeView";
	private SpecificationView view;
	private AssignmentController assignmentController;
	// private SelectionController selectionController;
	private SpecificationEnvironmentTree envTree;
	private InputDialog inputRename;

	public SpecificationController() {
		FrontEndResources.loadSpecificationCSS();

		if (ScenarioManager.get().isScenarioAvailable()) {
			reset();
		}

		// EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new
		// SpecificationChangedEventHandler() {
		// @Override
		// public void onSpecificationChangedEvent(SpecificationChangedEvent
		// event) {
		// setCurrentSpecificationName(event.getSelectedSpecification());
		// addExistingAssignments(event.getSelectedSpecification());
		// }
		// });

		EventControl.get().addHandler(InitialAssignmentChangedEvent.TYPE, new InitialAssignmentChangedEventHandler() {
			@Override
			public void onInitialAssignmentChanged(InitialAssignmentChangedEvent event) {
				assignmentEvent(event);
			}
		});
	}

	/**
	 * Updates the SpecificationView with the specification which has the given
	 * name.
	 * 
	 * @param specificationName
	 */
	public void changeSpecification(String specificationName) {
		setCurrentSpecificationName(specificationName);
		addExistingAssignments(specificationName);
		envTree.generateTree();
	}

	@Override
	public void onSwitchTo() {
	}

	public SpecificationEnvironmentTree getEnvironmentTree() {
		return envTree;
	}

	/**
	 * Called when a initAssignmentChangeevent is fired
	 */
	private void assignmentEvent(InitialAssignmentChangedEvent event) {
		String[] splitted = event.getFullParameterName().split("/");
		String name = splitted[splitted.length - 1];
		String path = event.getFullParameterName().substring(0, event.getFullParameterName().length() - name.length());

		ParameterNamespace namespace = ScenarioManager.get().getBuilder().getEnvironmentBuilder().getNamespace(path);
		ParameterDefinition parameter = ScenarioManager.get().getBuilder().getEnvironmentBuilder()
				.getParameter(name, namespace);

		path = namespace.getFullName();

		if (event.getChangeType() == ChangeType.Added) {
			addNewAssignment(parameter, path.replaceAll("/", "."));
		} else if (event.getChangeType() == ChangeType.Removed) {
			removeAssignment(parameter, path.replaceAll("/", "."));
		} else if (event.getChangeType() == ChangeType.Updated) {
			addExistingAssignments();
		}

		ScenarioManager.get().storeScenario();
	}

	/**
	 * Add new assignment.
	 */
	private void addNewAssignment(ParameterDefinition parameter, String path) {
		// EditGridItem item = new EditGridItem(path, parameter.getName(),
		// parameter.getType());
		EditGridItem item = new EditGridItem(parameter, "");
		assignmentController.addAssignment(item);
		assignmentController.refreshUI();
		ScenarioManager.get().getBuilder().getSpecificationBuilder().addInitAssignment(parameter, "");
	}

	/**
	 * removes initial assignment.
	 */
	private void removeAssignment(ParameterDefinition parameter, String path) {
		// EditGridItem assignment = new EditGridItem(path, parameter.getName(),
		// "");
		EditGridItem item = new EditGridItem(parameter, "");
		assignmentController.removeAssignment(item);
		assignmentController.refreshUI();
		ScenarioManager.get().getBuilder().getSpecificationBuilder().removeInitialAssignment(parameter);
	}

	@Override
	public void reset() {
		if (assignmentController == null) {
			assignmentController = new AssignmentController();
		} else {
			assignmentController.reset();
		}

		// if (selectionController == null) {
		// selectionController = new SelectionController();
		// } else {
		// selectionController.reset();
		// }
		envTree = new SpecificationEnvironmentTree();
		envTree.getView().addStyleName(TREE_CSS_CLASS);

		view = new SpecificationView(assignmentController.getAssignmentView(), envTree.getView());

		view.getImgRename().addClickHandler(this);
		view.getImgRemove().addClickHandler(this);

		addExistingAssignments();
		// addRenameSpecificationHandler();
	}

	@Override
	public Widget getView() {
		return view;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == view.getImgRename()) {
			renameSpecification();
		} else if (event.getSource() == view.getImgRemove()) {
			removeSpecification();
		}
	}

	private void renameSpecification() {
		if (inputRename == null) {
			inputRename = new InputDialog(R.get("renameSpecification"), R.get("renameSpecificationLabel") + ":");
			inputRename.addHandler(this);
			inputRename.setValidator(this);
		}
		inputRename.setValue(ScenarioManager.get().specification().getSpecificationName());
		inputRename.center();
	}

	@Override
	public void onInput(InputDialog source, String value) {
		if (!value.equals(ScenarioManager.get().specification().getSpecificationName())) {
			ScenarioManager.get().specification().renameWorkingSpecification(value);
		}
	}

	@Override
	public boolean validate(InputDialog source, String text) {
		// if (source == inputRename) {
		if (text.isEmpty()) {
			source.showWarning("The name of an Specification must not be empty.");
			return false;
		}
		if (ScenarioManager.get().getBuilder().getMeasurementSpecification(text) != null) {
			source.showWarning("There is already a Specification with this name.");
			return false;
		}
		// }
		source.hideWarning();
		return true;
	}

	private void removeSpecification() {
		Confirmation.confirm(R.get("removeSpecification"), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!ScenarioManager.get().specification().removeCurrentSpecification()) {
					Message.warning("There must be at least one specification available.");
				}
			}
		});
	}

	/**
	 * Sets the current specification name to the given name.
	 * 
	 * @param name
	 */
	private void setCurrentSpecificationName(String name) {
		view.setSpecificationName(name);
	}

	/**
	 * Clean the list of init assignments and adds the initial assignment of the
	 * current model to the assignmenListPanel.
	 */
	public void addExistingAssignments() {
		addExistingAssignments(ScenarioManager.get().specification().getSpecificationName());
	}

	/**
	 * Clean the list of init assignments and adds the initial assignment of the
	 * given specification to the assignmenListPanel.
	 */
	public void addExistingAssignments(String specificationName) {
		double metering = Metering.start();
		assignmentController.clearAssignments(true);

		if (specificationName == null || specificationName.isEmpty()) {
			return;
		}

		for (ConstantValueAssignment cva : ScenarioManager.get().getBuilder()
				.getMeasurementSpecification(specificationName).getInitializationAssignemts()) {

			EditGridItem item = new EditGridItem(cva);
			assignmentController.addAssignment(item);
		}

		assignmentController.refreshUI();
		Metering.stop(metering);
	}

}
