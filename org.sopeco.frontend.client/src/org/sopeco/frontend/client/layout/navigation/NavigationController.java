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
package org.sopeco.frontend.client.layout.navigation;

import java.util.HashMap;
import java.util.logging.Logger;

import org.sopeco.frontend.client.layout.MainLayoutPanel;
import org.sopeco.frontend.client.layout.center.CenterType;
import org.sopeco.frontend.client.layout.popups.InputDialog;
import org.sopeco.frontend.client.layout.popups.InputDialog.Icon;
import org.sopeco.frontend.client.layout.popups.InputDialogHandler;
import org.sopeco.frontend.client.layout.popups.InputDialogValidator;
import org.sopeco.frontend.client.manager.Manager;
import org.sopeco.frontend.client.manager.ScenarioManager;
import org.sopeco.frontend.client.resources.R;
import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;

/**
 * The controller class of the navigation.
 * 
 * @author Marius Oehler
 * 
 */
public class NavigationController implements ClickHandler, InputDialogHandler, InputDialogValidator {

	private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());

	private NavigationView view;
	private CenterType currentCenterType;
	private NavigationItem currentActiveNavigationItem;

	private InputDialog inputAddExperiment, inputAddSpecification;

	public NavigationController() {
		view = new NavigationView();

		loadExperiments();
		addCreateSpecificationClickHandler();

		for (NavigationItem item : view.getNaviItemsMap().values()) {
			item.addClickHandler(this);
		}

		// Events

//		EventControl.get().addHandler(SpecificationChangedEvent.TYPE, new SpecificationChangedEventHandler() {
//			@Override
//			public void onSpecificationChangedEvent(SpecificationChangedEvent event) {
//				setActiveSpecification(event.getSelectedSpecification());
//
//				view.getNaviItemsMap().get(CenterType.Specification).setSubText(event.getSelectedSpecification());
//				loadExperiments();
//			}
//		});
	}

	/**
	 * Sets the current specification in the navigation view to the given
	 * specificationName.
	 * 
	 * @param specificationName
	 */
	public void changeSpecification(String specificationName) {
		updateSpecifications();
		setActiveSpecification(specificationName);
		view.getNaviItemsMap().get(CenterType.Specification).setSubText(specificationName);
		loadExperiments();
	}

	@Override
	public void onInput(InputDialog source, String value) {
		if (source == inputAddExperiment) {
			ScenarioManager.get().experiment().createExperimentSeries(value);
		} else if (source == inputAddSpecification) {
			ScenarioManager.get().specification().createNewSpecification(value);
		}
	}

	@Override
	public boolean validate(InputDialog source, String text) {
		if (source == inputAddExperiment) {
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
		} else if (source == inputAddSpecification) {
			if (text.isEmpty()) {
				source.showWarning("The name of a Specification must not be empty.");
				return false;
			}
			if (ScenarioManager.get().getBuilder().getMeasurementSpecification(text) != null) {
				source.showWarning("There is already a Specification with this name.");
				return false;
			}
		}
		source.hideWarning();
		return true;
	}

	@Override
	public void onClick(ClickEvent event) {
		NavigationItem source = (NavigationItem) event.getSource();

		if (source.isActive()) {
			LOGGER.info("navi item is already selected -> skipped");
			return;
		}

		if (source.getType() == CenterType.Experiment) {
			String experimentName = ((NavigationSubItem) source).getExperimentName();
			MainLayoutPanel.get().getViewSwitch().switchToExperiment(experimentName);
			Manager.get().setSelectedExperiment(experimentName);
		} else {
			MainLayoutPanel.get().getViewSwitch().switchTo(source.getType());
		}
	}

	/**
	 * Udpates the "specification-select-panel", where you can select an other
	 * specification.
	 */
	public void updateSpecifications() {
		removeAllSpecifications();
		for (MeasurementSpecification ms : ScenarioManager.get().getCurrentScenarioDefinition()
				.getMeasurementSpecifications()) {
			addSpecifications(ms.getName());
		}
	}

	/**
	 * Add a clickhandler to the "add specification" html-element, which handels
	 * the adding of new specifications.
	 */
	private void addCreateSpecificationClickHandler() {
		view.getChangeSpecificationPanel().getAddSpecificationHTML().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.getChangeSpecificationPanel().setVisible(false);

				if (inputAddSpecification == null) {
					inputAddSpecification = new InputDialog(R.get("AddSpecification"), R.get("addSpecText") + ":",
							false, Icon.Add);
					inputAddSpecification.addHandler(NavigationController.this);
					inputAddSpecification.setValidator(NavigationController.this);
				}
				inputAddSpecification.setValue("");
				inputAddSpecification.center();
			}
		});
	}

	/**
	 * Hides the panel, where the user can change the current specification.
	 */
	public void hideChangeSpecpanel() {
		view.getChangeSpecificationPanel().setVisible(false);
	}

	/**
	 * Add a new specification to the panel, where you can change the current
	 * specification.
	 * 
	 * @param text
	 *            the name of the specification
	 */
	public void addSpecifications(String text) {
		GWT.log(text);
		view.getChangeSpecificationPanel().addItem(text).addClickHandler(getChangeSpecificationClickHandler());
	}

	/**
	 * Removes all entries of the panel, where you can change the current
	 * specification.
	 */
	public void removeAllSpecifications() {
		view.getChangeSpecificationPanel().clear();
		view.getChangeSpecificationPanel().getItemMap().clear();
	}

	/**
	 * The clickhandler of the entries, where you can change the current
	 * specification. This handler changes the current specification.
	 * 
	 * @return the clickhandler
	 */
	private ClickHandler getChangeSpecificationClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				HTML item = (HTML) event.getSource();
				String specificationName = item.getText();

				if (ScenarioManager.get().specification().getSpecificationName().equals(specificationName)) {
					view.getChangeSpecificationPanel().setVisible(false);

					return;
				}

				ScenarioManager.get().specification().changeSpecification(specificationName);
			}
		};
	}

	/**
	 * The specification (in the ChangeSpecificationPanel) with the given name
	 * will be highlighted.
	 */
	private void setActiveSpecification(String name) {
		for (HTML html : view.getChangeSpecificationPanel().getItemMap().values()) {
			html.removeStyleName("marked");
		}

		view.getChangeSpecificationPanel().getItemMap().get(name).addStyleName("marked");
		view.getChangeSpecificationPanel().setVisible(false);
	}

	/**
	 * Adds the existing experiments to the navigation.
	 */
	public void loadExperiments() {
		view.clearExperiments();

		HashMap<String, ExperimentSeriesDefinition> expMap = new HashMap<String, ExperimentSeriesDefinition>();
		for (ExperimentSeriesDefinition experiment : ScenarioManager.get().experiment()
				.getExperimentsOfCurrentSpecififcation()) {
			if (expMap.containsKey(experiment.getName())) {
				if (expMap.get(experiment.getName()).getVersion() < experiment.getVersion()) {
					expMap.put(experiment.getName(), experiment);
				}
			} else {
				expMap.put(experiment.getName(), experiment);
			}
		}

		for (ExperimentSeriesDefinition experiment : expMap.values()) {
			NavigationSubItem expItem = view.addExperimentItem(experiment.getName());
			expItem.setTitle(experiment.getName());
			// expItem.addClickHandler(getNavigationSubItemClickHandler());
			expItem.addClickHandler(this);
		}

		NavigationSubItem addExperiment = view.addExperimentItem(R.get("addExperiment"));
		addExperiment.addStyleName("addExperimentNaviItem");
		addExperiment.addAddImage();
		addExperiment.addClickHandler(getAddExperimentClickHandler());
	}

	/**
	 * 
	 * @return
	 */
	private ClickHandler getAddExperimentClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (inputAddExperiment == null) {
					inputAddExperiment = new InputDialog(R.get("addExperiment"), R.get("addExpText") + ":", false,
							Icon.Add);
					inputAddExperiment.addHandler(NavigationController.this);
					inputAddExperiment.setValidator(NavigationController.this);
				}
				inputAddExperiment.setValue("");
				inputAddExperiment.center();
			}
		};
	}

	/**
	 * Highlights the navigation entry, which having the given type.
	 * 
	 * @param newType
	 *            the new CenterType.
	 */
	public void setCurrentCenterType(CenterType newType) {
		if (view.getNaviItemsMap().containsKey(newType)) {
			setActiveNavigationItem(view.getNaviItemsMap().get(newType));
		}
		currentCenterType = newType;
	}

	/**
	 * Highlights the navigation entry, which having the given type.
	 * 
	 * @param item
	 */
	private void setActiveNavigationItem(NavigationItem item) {
		if (item == null) {
			return;
		}

		if (currentActiveNavigationItem != null) {
			currentActiveNavigationItem.setActive(false);
		}

		currentActiveNavigationItem = item;
		item.setActive(true);
	}

	/**
	 * Highlights the NaviItem, which is related to the experiment with the
	 * given name.
	 * 
	 * @param experimentName
	 */
	public void highlightExperiment(String experimentName) {
		setActiveNavigationItem(view.getExperimentItems().get(experimentName));
	}

	/**
	 * Returns the current CenterType, which is highlighted.
	 * 
	 * @return
	 */
	public CenterType getCurrentCenterType() {
		return currentCenterType;
	}

	/**
	 * Returns the NavigationView of this Controller.
	 * 
	 * @return the nvaigationView
	 */
	public NavigationView getView() {
		return view;
	}
}
