package org.sopeco.frontend.client.layout.center.specification;

import java.util.List;

import org.sopeco.frontend.client.R;
import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent;
import org.sopeco.frontend.client.event.InitialAssignmentChangedEvent.ChangeType;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.InitialAssignmentChangedEventHandler;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.navigation.NavigationController;
import org.sopeco.frontend.client.layout.popups.Confirmation;
import org.sopeco.frontend.client.layout.popups.InputDialogValidator;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.layout.popups.InputDialog;
import org.sopeco.frontend.client.layout.popups.InputDialogHandler;
import org.sopeco.frontend.client.layout.popups.InputDialog.Icon;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.resources.FrontEndResources;
import org.sopeco.frontend.client.rpc.RPC;
import org.sopeco.frontend.client.widget.grid.EditGridItem;
import org.sopeco.frontend.shared.helper.Metering;
import org.sopeco.persistence.entities.definition.ConstantValueAssignment;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.ParameterDefinition;
import org.sopeco.persistence.entities.definition.ParameterNamespace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

		reset();

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				setCurrentSpecificationName(event.getSelectedSpecification());
				addExistingAssignments(event.getSelectedSpecification());
			}
		});

		EventControl.get().addHandler(InitialAssignmentChangedEvent.TYPE, new InitialAssignmentChangedEventHandler() {
			@Override
			public void onInitialAssignmentChanged(InitialAssignmentChangedEvent event) {
				assignmentEvent(event);
			}
		});
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

		if (specificationName == null) {
			return;
		}

		for (ConstantValueAssignment cva : ScenarioManager.get().getBuilder()
				.getMeasurementSpecification(specificationName).getInitializationAssignemts()) {

			// String path = cva.getParameter().getFullName();
			// path = path.substring(0, path.lastIndexOf(".") + 1);

			String path = cva.getParameter().getNamespace().getFullName();

			EditGridItem item = new EditGridItem(cva);
			assignmentController.addAssignment(item);
		}

		assignmentController.refreshUI();
		Metering.stop(metering);
	}

	/**
	 * Loading the specification names of the current scenario.
	 */
	public void loadSpecificationNames() {
		loadSpecificationNames(null);
	}

	/**
	 * Loading the specification names of the current scenario and set the given
	 * specificationName as workingSpecification.
	 */
	public void loadSpecificationNames(String selectSpecification) {
		Loader.showLoader();
		RPC.getMSpecificationRPC().getAllSpecificationNames(getLoadSpecificationNamesCallback(selectSpecification));
	}

	/**
	 * Returns the callback, which is called after receiving the specification
	 * names.
	 * 
	 * @return async.callback
	 */
	private AsyncCallback<List<String>> getLoadSpecificationNamesCallback(final String selectSpecification) {
		return new AsyncCallback<List<String>>() {
			@Override
			public void onSuccess(List<String> result) {
				Loader.hideLoader();

				MainLayoutPanel.get().getNavigationController().removeAllSpecifications();

				if (!result.isEmpty()) {

					if (selectSpecification == null || selectSpecification.isEmpty()) {
						changeWorkingSpecification(result.get(0));
					} else {
						changeWorkingSpecification(selectSpecification);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Loader.hideLoader();
				Message.error(caught.getMessage());
			}
		};
	}

	/**
	 * Change the specification to the specification with the given name.
	 */
	public void changeWorkingSpecification(final String specification) {
		RPC.getMSpecificationRPC().setWorkingSpecification(specification, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					setCurrentSpecificationName(specification);
				} else {
					Message.error("Can't change specification to '" + specification + "'");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Message.error(caught.getMessage());
			}
		});
	}
}
