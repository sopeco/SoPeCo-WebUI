package org.sopeco.frontend.client.layout.center.specification;

import java.util.List;

import org.sopeco.frontend.client.event.EventControl;
import org.sopeco.frontend.client.event.SpecificationChangedEvent;
import org.sopeco.frontend.client.event.handler.SpecificationChangedEventHandler;
import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.ICenterController;
import org.sopeco.frontend.client.layout.popups.Loader;
import org.sopeco.frontend.client.layout.popups.Message;
import org.sopeco.frontend.client.model.ScenarioManager;
import org.sopeco.frontend.client.rpc.RPC;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class SpecificationController implements ICenterController {

	private SpecificationView view;
	private AssignmentController assignmentController;
	private SelectionController selectionController;

	public SpecificationController() {
		reset();

		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
			@Override
			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
				setCurrentSpecificationName(event.getSelectedSpecification());
			}
		});
	}

	@Override
	public void reset() {
		if (assignmentController == null) {
			assignmentController = new AssignmentController();
		} else {
			assignmentController.reset();
		}

		if (selectionController == null) {
			selectionController = new SelectionController();
		} else {
			selectionController.reset();
		}

		view = new SpecificationView(assignmentController.getAssignmentView(), selectionController.getView());

		addExistingAssignments();
		addRenameSpecificationHandler();
	}

	@Override
	public Widget getView() {
		return view;
	}

	/**
	 * Adds to the textbox of the specification name a blureHandler, which
	 * renames the selected specification on blur.
	 */
	private void addRenameSpecificationHandler() {
		view.getSpecificationNameTextbox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				final String textboxName = view.getSpecificationNameTextbox().getText();

				if (!textboxName.equals(ScenarioManager.get().getWorkingSpecificationName())) {
					ScenarioManager.get().renameWorkingSpecification(textboxName);
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
	 * Adding the initial assignment of the current model to the
	 * assignmenListPanel.
	 */
	private void addExistingAssignments() {
		AssignmentItem ai = new AssignmentItem("name.space.", "test", "STRING");
		AssignmentItem ai2 = new AssignmentItem("name.asdasdas.", "adsadsd", "INTEGER");
		AssignmentItem ai3 = new AssignmentItem("name.aasd.", "addasd", "STRING");

		assignmentController.addAssignment(ai);
		assignmentController.addAssignment(ai2);
		assignmentController.addAssignment(ai3);
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
					for (String sName : result) {
						// MainLayoutPanel.get().getNavigationController().addSpecifications(sName);
					}

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
