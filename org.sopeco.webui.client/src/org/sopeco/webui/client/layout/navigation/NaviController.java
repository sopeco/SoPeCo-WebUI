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
package org.sopeco.webui.client.layout.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sopeco.persistence.entities.definition.ExperimentSeriesDefinition;
import org.sopeco.persistence.entities.definition.MeasurementSpecification;
import org.sopeco.webui.client.layout.MainLayoutPanel;
import org.sopeco.webui.client.layout.center.ICenterController;
import org.sopeco.webui.client.layout.center.experiment.ExperimentController;
import org.sopeco.webui.client.layout.center.specification.SpecificationController;
import org.sopeco.webui.client.layout.popups.InputDialog;
import org.sopeco.webui.client.layout.popups.InputDialog.Icon;
import org.sopeco.webui.client.layout.popups.InputDialogHandler;
import org.sopeco.webui.client.layout.popups.InputDialogValidator;
import org.sopeco.webui.client.manager.Manager;
import org.sopeco.webui.client.manager.ScenarioManager;
import org.sopeco.webui.client.resources.R;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * 
 * @author Marius Oehler
 * 
 */
public class NaviController implements ClickHandler, InputDialogHandler, InputDialogValidator {

	private NaviView naviView;
	private Map<Class, List<NaviItem>> itemMap;
	private NaviItem selectedItem;

	private NaviItem addExpSeriesItem;

	private InputDialog inputAddExperiment;
	private InputDialog inputAddSpecification;

	private SpecificationPopup specificationPopup;

	/**
	 * Constructor.
	 */
	public NaviController() {
		naviView = new NaviView();
		itemMap = new HashMap<Class, List<NaviItem>>();

		specificationPopup = new SpecificationPopup();
		specificationPopup.getAddItem().addClickHandler(this);
	}

	public void addExperiments() {
		for (ExperimentSeriesDefinition experiment : ScenarioManager.get().experiment()
				.getExperimentsOfCurrentSpecififcation()) {
			addItem(ExperimentController.class, experiment.getName(), null).setAsExperiment();
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		if (event.getSource() == selectedItem) {
			return;
		}

		specificationPopup.removeFromParent();

		if (event.getSource() == addExpSeriesItem) {
			addExperimentSeries();
			return;
		}

		if (event.getSource() == specificationPopup.getAddItem()) {
			addSpecification();
			return;
		}

		NaviItem src = ((NaviItem) event.getSource());
		setSelectedItem(src);

		for (Class clazz : itemMap.keySet()) {
			if (itemMap.get(clazz) != null && itemMap.get(clazz).contains(event.getSource())) {
				if (clazz.equals(ExperimentController.class)) {
					MainLayoutPanel.get().switchToExperiment(src.getText());
				} else {
					MainLayoutPanel.get().switchView(clazz);
				}
			}
		}
	}

	public SpecificationPopup getSpecificationPopup() {
		return specificationPopup;
	}

	private void addSpecification() {
		if (inputAddSpecification == null) {
			inputAddSpecification = new InputDialog(R.lang.AddSpecification(), R.lang.addSpecText() + ":", false,
					Icon.Add);
			inputAddSpecification.addHandler(this);
			inputAddSpecification.setValidator(this);
		}
		inputAddSpecification.setValue("");
		inputAddSpecification.center();
	}

	public void clear() {
		naviView.clear();
		itemMap.clear();
	}

	public NaviView getView() {
		return naviView;
	}

	public NaviItem addItem(Class clazz, String text, String subText) {
		NaviItem item = new NaviItem(text, subText);
		item.addClickHandler(this);

		if (clazz.equals(SpecificationController.class)) {
			item.addChangeSpecificationIcon(specificationPopup);
		}

		putItem(clazz, item);
		return item;
	}

	public void addAddExpSeriesItem() {
		if (addExpSeriesItem == null) {
			addExpSeriesItem = new NaviItem(R.lang.addExperimentSeries());
			addExpSeriesItem.addClickHandler(this);
			addExpSeriesItem.setAsSubItem();
			addExpSeriesItem.setAsAddItem();
		}
		naviView.add(addExpSeriesItem);
	}

	private void putItem(Class clazz, NaviItem item) {
		if (itemMap.get(clazz) == null) {
			itemMap.put(clazz, new ArrayList<NaviItem>());
		}
		itemMap.get(clazz).add(item);
		naviView.add(item);
	}

	public <T extends ICenterController> void setSelectedItem(Class<T> controllerClazz) {
		if (controllerClazz == null) {
			return;
		}

		List<NaviItem> list = getItem(controllerClazz);
		if (list == null || list.isEmpty()) {
			return;
		} else if (controllerClazz.equals(ExperimentController.class)) {
			for (NaviItem item : list) {
				if (item.getText().equals(Manager.get().getSelectedExperiment())) {
					setSelectedItem(item);
					break;
				}
			}
		} else {
			setSelectedItem(list.get(0));
		}
	}

	public void setSelectedItem(NaviItem item) {
		if (selectedItem != null) {
			selectedItem.setSelected(false);
		}
		item.setSelected(true);
		selectedItem = item;
	}

	public <T extends ICenterController> List<NaviItem> getItem(Class<T> clazz) {
		if (!itemMap.containsKey(clazz)) {
			return null;
		}
		return itemMap.get(clazz);
	}

	public void refreshSpecificationPopup() {
		specificationPopup.clear();

		for (MeasurementSpecification ms : ScenarioManager.get().getCurrentScenarioDefinition()
				.getMeasurementSpecifications()) {
			specificationPopup.addItem(ms.getName());
		}

		specificationPopup.addAddItem();

		specificationPopup.setSelectedItem(Manager.get().getCurrentScenarioDetails().getSelectedSpecification());
	}

	//
	public void addExperimentSeries() {
		if (inputAddExperiment == null) {
			inputAddExperiment = new InputDialog(R.lang.addExperiment(), R.lang.addExpText() + ":", false, Icon.Add);
			inputAddExperiment.addHandler(this);
			inputAddExperiment.setValidator(this);
		}
		inputAddExperiment.setValue("");
		inputAddExperiment.center();
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
}
